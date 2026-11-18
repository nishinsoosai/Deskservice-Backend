package com.example.Deskservice.client;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.Deskservice.dto.PolicyResponse;

@Component
public class PolicyClient {

    private final RestClient restClient;

    public PolicyClient(RestClient policyRestClient) {
        this.restClient = policyRestClient;
    }

    public PolicyResponse getReviewRoute(BigDecimal amount) {

        String requestId = MDC.get("requestId");

        if (requestId == null) {
            requestId = UUID.randomUUID().toString();
        }

        try {

            return restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/policy")
                            .queryParam("amount", amount)
                            .build())
                    .header("X-Request-Id", requestId)
                    .retrieve()
                    .body(PolicyResponse.class);

        } catch (ResourceAccessException ex) {

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Policy service is currently unavailable. Please try again."
            );
        }
    }
}