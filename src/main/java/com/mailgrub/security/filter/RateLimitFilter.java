package com.mailgrub.security.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mailgrub.config.AppRateLimitProperties;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RateLimitFilter extends OncePerRequestFilter {

    private final AppRateLimitProperties props;
    private final Cache<String, Bucket> buckets;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public RateLimitFilter(AppRateLimitProperties props) {
        this.props = props;
        this.buckets = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofHours(1))
                .maximumSize(100_000)
                .build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!props.isEnabled()) return true;
        String path = request.getRequestURI();
        return props.getExcludePaths().stream().anyMatch(p -> matcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String key = resolveKey(req);
        Bucket bucket = buckets.get(key, this::newBucket);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        res.setHeader("X-RateLimit-Limit", Integer.toString(props.getCapacity()));
        res.setHeader("X-RateLimit-Remaining", Long.toString(Math.max(0, probe.getRemainingTokens())));

        if (probe.isConsumed()) {
            chain.doFilter(req, res);
        } else {
            long waitSeconds = Duration.ofNanos(probe.getNanosToWaitForRefill()).toSeconds();
            res.setStatus(429);
            res.setHeader("Retry-After", String.valueOf(waitSeconds));
            res.getWriter().write("Too many requests");
        }
    }

    private Bucket newBucket(String key) {
        Refill refill = Refill.greedy(props.getRefillTokens(), props.getRefillPeriod());
        Bandwidth limit = Bandwidth.classic(props.getCapacity(), refill);
        return Bucket.builder().addLimit(limit).build(); // ✅ Bucket4j builder
    }

    private String resolveKey(HttpServletRequest req) {
        if ("user".equalsIgnoreCase(props.getKeyStrategy()) && req.getUserPrincipal() != null) {
            return "usr:" + req.getUserPrincipal().getName();
        }
        return "ip:" + extractClientIp(req);
    }

    private static String extractClientIp(HttpServletRequest request) {
        String[] headers = {"CF-Connecting-IP", "X-Forwarded-For", "X-Real-IP"};
        for (String h : headers) {
            String v = request.getHeader(h);
            if (StringUtils.hasText(v)) return v.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
