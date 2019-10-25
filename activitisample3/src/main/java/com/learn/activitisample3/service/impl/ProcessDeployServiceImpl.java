package com.learn.activitisample3.service.impl;

import com.learn.activitisample3.service.ProcessBaseService;
import com.learn.activitisample3.service.ProcessDeployService;
import com.learn.activitisample3.util.ConvertTool;
import com.learn.activitisample3.util.QueryTool;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.*;

@Service
public class ProcessDeployServiceImpl implements ProcessDeployService{
    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private ProcessDefinitionQuery processDefinitionQuery;
    private ProcessInstanceQuery processInstanceQuery;
    private ModelQuery modelQuery;

    @Autowired
    QueryTool queryTool;

    @Autowired
    public ProcessDeployServiceImpl(ProcessBaseService processBaseService){
        processEngine = processBaseService.getProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processInstanceQuery = runtimeService.createProcessInstanceQuery();
        modelQuery = repositoryService.createModelQuery();
    }

    @Override
    public Deployment deployProcess(String bpmnUrl, String pngUrl) {
        DeploymentBuilder builder = Optional.ofNullable(bpmnUrl).filter(bpmnUrlTemp -> !bpmnUrlTemp.equals(""))
                .map(bpmnUrlNonNull -> repositoryService.createDeployment().addClasspathResource(bpmnUrlNonNull))
                .orElseThrow(() -> new NullPointerException("流程部署失败，请检查是否存在对应流程定义文件资源"));

        if(!StringUtils.isEmpty(pngUrl)){
            builder.addClasspathResource(pngUrl);
        }

        return builder.deploy();
    }

    @Override
    public Deployment deployProcess(String bpmnUrl) {
        return deployProcess(bpmnUrl,"");
    }

    @Override
    public Deployment deployProcess(String resourceName,InputStream bpmnFile) {
        DeploymentBuilder builder = repositoryService.createDeployment().addInputStream(resourceName, bpmnFile);
        return builder.deploy();
    }

    @Override
    public Deployment deployProcessByModel(String modelId) {
        Model model = modelQuery.modelId(modelId).singleResult();
        model.getName();
        byte[] source = repositoryService.getModelEditorSource(model.getId());
        BpmnModel bpmnModel = ConvertTool.modelEditorSourceToBpmnModel(source);
        DeploymentBuilder builder = repositoryService.createDeployment().addBpmnModel(model.getName() + ".bpmn", bpmnModel);
        return builder.deploy();
    }

    /*
    eg:
      processDefinition.getDeploymentId():7501
      processDefinition.getKey():myProcess
      processDefinition.getId():myProcess:3:7504
      processDebpmnFilefinition.getVersion():3 */
    @Override
    public ProcessDefinition getProcessDefinition(String processDefinitionId) {
        return processDefinitionQuery.processDefinitionId(processDefinitionId).singleResult();
    }

    @Override
    public ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId) {
        return processDefinitionQuery.deploymentId(deploymentId).singleResult();
    }

    @Override
    public ProcessDefinition getProcessDefinitionByKeyAndVersion(String processDefintionKey, int version) {
        return processDefinitionQuery.
                processDefinitionKey(processDefintionKey).processDefinitionVersion(version).singleResult();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProcessDefinition> getProcessDefinitionsByKey(int page,int size,String processDefinitionKey) {
        return queryTool.query(page,size,processDefinitionKey,queryTool.QUERY_PROCESS_DEFINITION_BY_DEFINITION_KEY);
    }

    @Override
    public void suspendProcessDefinition(String processDefinitionId) {
        Optional.of(getProcessDefinition(processDefinitionId))
                .filter(processDefinition -> !processDefinition.isSuspended())
                .ifPresent(processDefinition -> repositoryService.suspendProcessDefinitionById(processDefinition.getId()));
    }

    @Override
    public void activateProcessDefinition(String processDefinitionId) {
        Optional.of(getProcessDefinition(processDefinitionId))
                .filter(processDefinition -> processDefinition.isSuspended())
                .ifPresent(processDefinition -> repositoryService.activateProcessDefinitionById(processDefinition.getId()));
    }

    @Override
    public void addStartCandidateUser(String processDefinitionId, String... userIdentities) {
        for(String userIdentity:userIdentities){
            repositoryService.addCandidateStarterUser(processDefinitionId,userIdentity);
        }
    }

    @Override
    public void addStartCandidateDept(String processDefinitionId, String... deptIdentities) {
        for(String deptIdentity:deptIdentities){
            repositoryService.addCandidateStarterGroup(processDefinitionId,deptIdentity);
        }
    }

    @Override
    public void deleteStartCandidateUser(String processDefinitionId, String... userIdentities) {
        for(String userIdentity:userIdentities){
            repositoryService.deleteCandidateStarterUser(processDefinitionId,userIdentity);
        }
    }

    @Override
    public void deleteStartCandidateDept(String processDefinitionId, String... deptIdentities) {
        for(String deptIdentity:deptIdentities){
            repositoryService.deleteCandidateStarterGroup(processDefinitionId,deptIdentity);
        }
    }

    @Override
    public ProcessInstance startProcess(String processDefinitionId) {
        return runtimeService.startProcessInstanceById(processDefinitionId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProcessInstance> getProcessInstancesByDefinitionId(int page, int size, String processDefinitionId) {
        return queryTool.query(page,size,processDefinitionId,queryTool.QUERY_PROCESS_INSTANCE_BY_DEFINITION_ID);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ProcessInstance> getProcessInstancesByDefinitionKey(int page, int size, String processDefinitionKey) {
        return queryTool.query(page,size,processDefinitionKey,queryTool.QUERY_PROCESS_INSTANCE_BY_DEFINTION_KEY);
    }

    @Override
    public ProcessInstance getProcessInstanceById(String processInstanceId) {
        return processInstanceQuery.processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public List<ProcessInstance> getProcessInstancesIdIn(String... processInstanceIds) {
        return getProcessInstancesIdIn(Arrays.asList(processInstanceIds));
    }

    @Override
    public List<ProcessInstance> getProcessInstancesIdIn(List<String> processInstanceIds) {
        return queryTool.QUERY_PROCESS_INSTANCE_BY_IDS
                .apply(new HashSet<String>(processInstanceIds)).list();
    }

    @Override
    public void suspendProcess(String processInstanceId) {
        Optional.of(getProcessInstanceById(processInstanceId))
                .filter(processInstance -> !processInstance.isSuspended())
                .ifPresent(processInstance -> runtimeService.suspendProcessInstanceById(processInstance.getId()));
    }

    @Override
    public void activateProcess(String processInstanceId) {
        Optional.of(getProcessInstanceById(processInstanceId))
                .filter(processInstance -> processInstance.isSuspended())
                .ifPresent(processInstance -> runtimeService.activateProcessInstanceById(processInstance.getId()));
    }
}
