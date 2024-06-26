package hu.StudentSpace.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    @Bean
    @LoadBalanced
    @Qualifier("webClientBuilder")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
