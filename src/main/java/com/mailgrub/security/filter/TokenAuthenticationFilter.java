package com.mailgrub.security.filter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private static final String AUTH = "Authorization";
  private static final String BEARER = "Bearer ";

  private final FirebaseAuth firebaseAuth;

  public TokenAuthenticationFilter(FirebaseAuth firebaseAuth) {
    this.firebaseAuth = firebaseAuth;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    String header = req.getHeader(AUTH);
    if (header != null && header.startsWith(BEARER)) {
      String token = header.substring(BEARER.length());
      try {
        FirebaseToken decoded = firebaseAuth.verifyIdToken(token);
        String uid = decoded.getUid();
        Collection<GrantedAuthority> auths = Collections.emptyList();
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(uid, token, auths);
        SecurityContextHolder.getContext().setAuthentication(auth);
      } catch (FirebaseAuthException e) {
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return;
      }
    }
    chain.doFilter(req, res);
  }
}
