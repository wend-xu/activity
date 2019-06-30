package com.learn.activitysample.all;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

public class OutPutInfo {
    public static void outputDeploy(Deployment deployment){
        System.out.println("=========>>>>outputDeploy");
        System.out.println("deployment.getName():"+deployment.getName());
        System.out.println("deployment.getKey():"+deployment.getKey());
        System.out.println("deployment.getId():"+deployment.getId());
        System.out.println("deployment.getTenantId():"+deployment.getTenantId());
        System.out.println("deployment.getDeploymentTime():"+deployment.getDeploymentTime());
        System.out.println("deployment.getCategory():"+deployment.getCategory());
    }

    public static void outputProcessDefinition(ProcessDefinition processDefinition){
        System.out.println("=========>>>>outputProcessDefinition");
        System.out.println("processDefinition.getDeploymentId():"+processDefinition.getDeploymentId());
        System.out.println("processDefinition.getKey():"+processDefinition.getKey());
        System.out.println("processDefinition.getId():"+processDefinition.getId());
        System.out.println("processDefinition.getVersion():"+processDefinition.getVersion());
        System.out.println("processDefinition.getName():"+processDefinition.getName());
    }

    public static void outputProcessInstance(ProcessInstance processInstance){
        System.out.println("=========>>>>outputProcessInstance");
        System.out.println("processInstance.getId():"+processInstance.getId());
        System.out.println("processInstance.getName():"+processInstance.getName());
        System.out.println("processInstance.getStartUserId():"+processInstance.getStartUserId());
        System.out.println("processInstance.getDeploymentId():"+processInstance.getDeploymentId());
        System.out.println("processInstance.getProcessDefinitionKey():"+processInstance.getProcessDefinitionKey());
        System.out.println("processInstance.getProcessDefinitionName():"+processInstance.getProcessDefinitionName());
        System.out.println("processInstance.getProcessDefinitionId():"+processInstance.getProcessDefinitionId());
        System.out.println("processInstance.getProcessDefinitionVersion():"+processInstance.getProcessDefinitionVersion());
    }
}
