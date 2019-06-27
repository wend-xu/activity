package com.learn.activitysample.all;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeployProcess {
    ProcessEngine processEngine;

    public DeployProcess() {
        if (processEngine == null){
            processEngine = GetProcessEngine.getDefaultProcessEngine();
        }
    }

    public ProcessDefinition deploy(String bpmnUrl, String pngUrl){
        RepositoryService repositoryService = processEngine.getRepositoryService();

        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
                .addClasspathResource(bpmnUrl)
                .addClasspathResource(pngUrl);
        Deployment deploy = deploymentBuilder.deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        return processDefinition;
    }

    public ProcessInstance startById(String id){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(id);
        System.out.println("流程启动："+processInstance.getProcessDefinitionKey()+":"+processInstance.getDeploymentId());
        return processInstance;
    }

    public ProcessInstance startById(ProcessDefinition processDefinition){
        return startById(processDefinition.getId());
    }

    public ProcessInstance startByKey(String key){
        return processEngine.getRuntimeService().startProcessInstanceByKey(key);
    }

    public List<ProcessDefinition> queryProcessDefinition(String key){
        return processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(key).list();
    }

    public void outputProcessInstance(ProcessInstance processInstance){
        System.out.println(processInstance.getName());
        System.out.println(processInstance.getBusinessKey());
        System.out.println(processInstance.getDeploymentId());
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getProcessDefinitionKey());
    }
}
