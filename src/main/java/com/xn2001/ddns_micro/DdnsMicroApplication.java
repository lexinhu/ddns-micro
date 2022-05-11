package com.xn2001.ddns_micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DdnsMicroApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdnsMicroApplication.class, args);
    }

}
