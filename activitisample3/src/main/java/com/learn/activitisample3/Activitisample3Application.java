package com.learn.activitisample3;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.activiti.spring.boot.SecurityAutoConfiguration.class,})

public class Activitisample3Application {

    public static void main(String[] args) {
        SpringApplication.run(Activitisample3Application.class, args);
    }

}
