package com.example.somnusGateWay;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class ProxyConfig {

    @Bean
    RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user_route",
                        route -> route.path("/user/**")
                                .filters(filter -> filter.stripPrefix(1))
                                .uri("lb://user"))
                .route("dream_route", route -> route.path("/dream/**")
                        .filters(filter -> filter.stripPrefix(1)).uri("lb://dream")).build();
    }
}
