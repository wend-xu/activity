package com.learn.activitisample2.service.impl;

import com.learn.activitisample2.service.ProcessBaseService;
import com.learn.activitisample2.service.ProcessDeployService;
import com.learn.activitisample2.util.QueryTool;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ProcessDeployServiceImpl implements ProcessDeployService{
    private ProcessEngine processEngine;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private ProcessDefinitionQuery processDefinitionQuery;
    private ProcessInstanceQuery processInstanceQuery;

    @Autowired
    QueryTool queryTool;

    @Autowired
    public ProcessDeployServiceImpl(ProcessBaseService processBaseService){
        processEngine = processBaseService.getProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        processInstanceQuery = runtimeService.createProcessInstanceQuery();
    }

    @Override
    public ProcessDefinition deployProcess(String bpmnUrl, String pngUrl) {
        DeploymentBuilder builder = Optional.ofNullable(bpmnUrl).filter(bpmnUrlTemp -> !bpmnUrlTemp.equals(""))
                .map(bpmnUrlNonNull -> repositoryService.createDeployment().addClasspathResource(bpmnUrlNonNull))
                .orElseThrow(() -> new NullPointerException("流程部署失败，请检查是否存在对应流程定义文件资源"));

        if(!StringUtils.isEmpty(pngUrl)){
            builder.addClasspathResource(pngUrl);
        }
        Deployment deploy = builder.deploy();

        return repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
    }

    @Override
    public ProcessDefinition deployProcess(String bpmnUrl) {
        return deployProcess(bpmnUrl,null);
    }

    /*
    eg:
      processDefinition.getDeploymentId():7501
      processDefinition.getKey():myProcess
      processDefinition.getId():myProcess:3:7504
      processDefinition.getVersion():3 */
    @Override
    public ProcessDefinition getProcessDefinition(String processDefintionId) {
        return processDefinitionQuery.processDefinitionId(processDefintionId).singleResult();
    }

    @Override
    public ProcessDefinition getProcessDefinition(String processDefintionKey, int version) {
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
        addStartCandidateUser(processDefinitionId, Arrays.asList(userIdentities));
    }

    @Override
    public void addStartCandidateUser(String processDefinitionId, List<String> userIdentities) {
        userIdentities.forEach(
                userIdentity -> repositoryService.addCandidateStarterUser(processDefinitionId,userIdentity));
    }

    @Override
    public void addStartCandidateDept(String processDefinitionId, String... deptIdentities) {
        Arrays.stream(deptIdentities).forEach(
                deptIdentitiy -> repositoryService.addCandidateStarterGroup(processDefinitionId,deptIdentitiy));
    }

    @Override
    public ProcessInstance startProcess(String processDefinitionId) {
        return runtimeService.startProcessInstanceById(processDefinitionId);
    }

    /*@Override
    public List<ProcessInstance> getProcessInstancesByDefinitionId(int page,int size,String definitionId) {
        int startNum = Tools.countLimitNum(page, size, Tools.startNum);
        int endNum = Tools.countLimitNum(page, size, Tools.endNum);
        return processInstanceQuery.processDefinitionId(definitionId).listPage(startNum,endNum);
    }

    @Override
    public List<ProcessInstance> getProcessInstancesByDefinitionKey(int page,int size,String definitionKey) {
        int startNum = Tools.countLimitNum(page, size, Tools.startNum);
        int endNum = Tools.countLimitNum(page, size, Tools.endNum);
        return processInstanceQuery.processDefinitionKey(definitionKey).listPage(startNum,endNum);
    }*/

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
                .filter(processInstance -> processInstance.isSuspended())
                .ifPresent(processInstance -> runtimeService.suspendProcessInstanceById(processInstance.getId()));
    }

    @Override
    public void activateProcess(String processInstanceId) {
        Optional.of(getProcessInstanceById(processInstanceId))
                .filter(processInstance -> !processInstance.isSuspended())
                .ifPresent(processInstance -> runtimeService.activateProcessInstanceById(processInstance.getId()));
    }
}
