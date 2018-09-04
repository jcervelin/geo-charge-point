package io.jcervelin.evchargingapi;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class EvChargingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EvChargingApiApplication.class, args);
    }

}
