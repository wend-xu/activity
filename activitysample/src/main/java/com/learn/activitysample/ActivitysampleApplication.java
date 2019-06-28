package com.learn.activitysample;

import com.learn.activitysample.all.DeployProcess;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class ActivitysampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitysampleApplication.class, args);
    }

}
