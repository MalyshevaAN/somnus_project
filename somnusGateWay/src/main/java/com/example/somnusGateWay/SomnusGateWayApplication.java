package com.example.somnusGateWay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SomnusGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SomnusGateWayApplication.class, args);
    }

}
