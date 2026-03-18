package ru.ssau.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class StaticResourceConfig {
    @Bean
    public RouterFunction<ServerResponse> staticResourceRouter() {
        ClassPathResource index = new ClassPathResource("static/index.html");
        RequestPredicate spaPredicate = RequestPredicates.GET("/**")
                .and(RequestPredicates.path("/api/**").negate())
                .and(request -> !request.path().contains("."));

        return RouterFunctions.route().resource(spaPredicate, index).build();
    }
}