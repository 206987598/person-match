package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.mapper")
@EnableScheduling
public class PersonMatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonMatchApplication.class, args);
    }

}
