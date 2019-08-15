package com.learn.activitisample2.service.impl;

import com.learn.activitisample2.service.ProcessBaseService;
import org.activiti.engine.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProcessBaseServiceImpl implements ProcessBaseService {
    private ProcessEngine processEngine;

    /**
     * 关于此处ProcessEngines.getDefaultProcessEngine();获取对象为什么会为空的原因
     * 因为该bean加载时机的问题
     * 此时通过ProcessEngines.getDefaultProcessEngine();获取到的流程引擎为空，只能通过配置文件的方式加载
     * 解决该问题需要研究bean初始化的顺序
     * */
    public ProcessBaseServiceImpl(@Value("${activiti.process-engine.config}") String processEngineConfigUrl) {
            processEngine = ProcessEngineConfiguration
                    .createProcessEngineConfigurationFromResource(processEngineConfigUrl)
                    .setProcessEngineName("BaseServiceProcessEngine").buildProcessEngine();
            System.out.println("===============>>>baseService 初始化完成");
    }

    @Override
    public ProcessEngine getProcessEngine() {
        if(processEngine == null)
            throw new NullPointerException("流程引擎对象为空，获取失败");
        return processEngine;
    }
}
