package com.learn.activitysample;

import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class configTest {
    @Test
    public void configTest1(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault();
        System.out.println(configuration == null?"创建失败":configuration.toString());
    }

    @Test
    public void configTest2(){
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        System.out.println(configuration == null?"创建失败":configuration.toString());
    }
}
