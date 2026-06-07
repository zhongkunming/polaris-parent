package com.github.polaris.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.polaris"
})
public class SSOServerApplication {
    static void main(String[] args) {
        SpringApplication.run(SSOServerApplication.class, args);
    }
}
