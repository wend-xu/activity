package com.learn.helloactivity;

import com.learn.helloactivity.activityDemo.HelloActivity;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class HelloactivityApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloactivityApplication.class, args);
        try {
            HelloActivity.approve();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

}
