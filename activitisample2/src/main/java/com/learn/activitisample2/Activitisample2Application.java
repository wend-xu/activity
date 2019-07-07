package com.learn.activitisample2;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Activitisample2Application {

    public static void main(String[] args) {
        SpringApplication.run(Activitisample2Application.class, args);
    }

}
