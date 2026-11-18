package com.example.Deskservice.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class PolicyConfig {

    @Value("${policy.base-url}")
    private String policyBaseUrl;

    @Bean
    public RestClient policyRestClient() {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        // Connection timeout: 1 second
        factory.setConnectTimeout(Duration.ofMillis(1000));

        // Response timeout: 2 seconds
        factory.setReadTimeout(Duration.ofMillis(2000));

        return RestClient.builder()
                .baseUrl(policyBaseUrl)
                .requestFactory(factory)
                .build();
    }
}
