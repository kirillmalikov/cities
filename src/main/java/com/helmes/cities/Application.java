package com.helmes.cities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Locale;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableConfigurationProperties
public class Application {

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        SpringApplication.run(Application.class, args);
    }
}
