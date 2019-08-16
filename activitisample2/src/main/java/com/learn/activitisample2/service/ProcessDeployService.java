package com.learn.activitisample2.service;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

import java.util.List;

public interface ProcessDeployService {
    //部署流程
    ProcessDefinition deployProcess(String bpmnUrl, String pngUrl);
    ProcessDefinition deployProcess(String bpmnUrl);

    //获取流程定义
    ProcessDefinition getProcessDefinition(String processDefintionId);
    ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId);
    ProcessDefinition getProcessDefinitionByKeyAndVersion(String processDefintionKey, int version);
    List<ProcessDefinition> getProcessDefinitionsByKey(int page,int size,String processDefinitionKey);

    //挂起流程定义
    void suspendProcessDefinition(String processDefinitionId);

    //激活流程定义
    void activateProcessDefinition(String processDefinitionId);

    //增加开始流程candidate
    void addStartCandidateUser(String processDefinitionId,String ... userIdentities);
    void addStartCandidateUser(String processDefinitionId, List<String> userIdentities);
    void addStartCandidateDept(String processDefinitionId,String ... deptIdentities);

    //开始流程
    ProcessInstance startProcess(String peocessDefintionId);

    //获取流程实例
    ProcessInstance getProcessInstanceById(String processInstanceId);
    List<ProcessInstance> getProcessInstancesIdIn(String ... processInstanceIds);
    List<ProcessInstance> getProcessInstancesIdIn(List<String> processInstanceIds);
    List<ProcessInstance> getProcessInstancesByDefinitionId(int page,int size,String processDefinitionId);
    List<ProcessInstance> getProcessInstancesByDefinitionKey(int page,int size,String processDefinitionKey);

    //暂停流程实例
    void suspendProcess(String processInstanceId);

    //激活流程实例
    void activateProcess(String processInstanceId);
}
