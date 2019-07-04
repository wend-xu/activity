package com.learn.activitysample.all;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;

public class GetProcessEngine {
    private static ProcessEngine processEngine;

    public static ProcessEngine getDefaultProcessEngine(){
        if(processEngine == null){
            ProcessEngineConfiguration processEngineConfiguration =
                    ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
            processEngine =  processEngineConfiguration.buildProcessEngine();
        }
        return processEngine;
    }
}
