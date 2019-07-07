package com.learn.activitisample2.service.impl;

import com.learn.activitisample2.service.ProcessBaseService;
import com.learn.activitisample2.util.SystemConstant;
import org.activiti.engine.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProcessBaseServiceImpl implements ProcessBaseService {
    private ProcessEngine processEngine;

    public ProcessBaseServiceImpl(@Value("${activiti.process-engine.config}") String processEngineConfigUrl) {
        if(processEngineConfigUrl.equals(SystemConstant.PROCESS_ENGINE_CONFIG_DEFAULT) || StringUtils.isEmpty(processEngineConfigUrl)){
            processEngine = ProcessEngines.getDefaultProcessEngine();
        }else{
            processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(processEngineConfigUrl).buildProcessEngine();
        }
    }

    @Override
    public ProcessEngine getProcessEngine() {
        if(processEngine == null)
            throw new NullPointerException("流程引擎对象为空，获取失败");
        return processEngine;
    }
}
