package ru.practicum.ewm.statsclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.client.invoke.ApiClient;

@Configuration
public class StatsClientConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public ApiClient apiClient(@Value("${ru.practicum.stats.server.url}") String basePath) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(basePath);

        return apiClient;
    }
}
