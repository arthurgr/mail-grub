package com.mailgrub.controller;

import com.mailgrub.model.Tax;
import com.mailgrub.repository.TaxRepository;
import com.mailgrub.dto.PagedResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TaxControllerTest.MockConfig.class)
class TaxControllerTest {

    @TestConfiguration
    static class MockConfig {
        @Bean
        public TaxRepository taxRepository() {
            Tax tax = new Tax();
            tax.setId(1);
            tax.setJurisdiction("Colorado");
            tax.setTaxRate(0.0825);

            Page<Tax> page = new PageImpl<>(List.of(tax), PageRequest.of(0, 10), 1);
            TaxRepository mockRepo = Mockito.mock(TaxRepository.class);
            Mockito.when(mockRepo.findAll(Mockito.any(Pageable.class))).thenReturn(page);

            return mockRepo;
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testGetTaxes() {
        ResponseEntity<PagedResponse> response = restTemplate.getForEntity("/taxes", PagedResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
