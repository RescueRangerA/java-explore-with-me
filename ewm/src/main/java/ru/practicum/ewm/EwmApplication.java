package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.client.StatsControllerApi;
import ru.practicum.ewm.client.model.EndpointHit;

@SpringBootApplication
@ComponentScan(
        basePackages = {"ru.practicum"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class EwmApplication {

    public static void main(String[] args) {
        SpringApplication.run(EwmApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // example of client usage
    public static void addStatsHit() {
        StatsControllerApi client = new StatsControllerApi();
        client.hit(
                EndpointHit.builder()
                        .id(1L)
                        .ip("192.163.0.1")
                        .uri("/events/1")
                        .app("ewm-main-service")
                        .timestamp("2022-09-06 11:00:23")
                        .build()
        );
    }
}
