package com.learn.activitysample.all;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartHasAssign {
    private ProcessEngine processEngine;
    private Map<String,Object> assignInfo;
    private String processDefinitionId;
    private int processDefinitionVersion;

    public StartHasAssign(){
        init();
    }

    //初始化一些数据
    private void init(){
        processEngine = GetProcessEngine.getDefaultProcessEngine();
        assignInfo = new HashMap<>();
        assignInfo.put("tlGroupId","TLIds");
        assignInfo.put("hrGroupId","HRIds");
        assignInfo.put("userGroupId","UserIds");
        //查询流程定义
        List<ProcessDefinition> myProcess
                = processEngine.getRepositoryService().createProcessDefinitionQuery()
                .processDefinitionKey("myProcess").orderByProcessDefinitionVersion().asc().list();
        //若为空部署流程，不为空获取最新版本流程定义
        ProcessDefinition processDefinition = null;
        if(myProcess.size() == 0){
            processDefinition = deployProcess("processes/MyProcessHasAssign.bpmn",null);
        }else if(myProcess.size() > 0){
            processDefinition = myProcess.get(myProcess.size() - 1);
        }else {
            throw new NullPointerException("==================》查询流程定义异常");
        }
        processDefinitionId = processDefinition.getId();
        processDefinitionVersion = processDefinition.getVersion();
    }

    public void initUserAndGroup(String groupId,String[] userIds){
        IdentityService identityService = processEngine.getIdentityService();

        Group group = identityService.newGroup(groupId);
        identityService.saveGroup(group);

        for(String userId:userIds){
            User user = identityService.newUser(userId);
            identityService.saveUser(user);
            identityService.createMembership(userId,groupId);
        }
    }

    //部署流程
    private ProcessDefinition deployProcess(String bpmnUrl, String pngUrl){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        if(bpmnUrl == null){
            throw new NullPointerException("BPMN文件路径为空，请传入正确地文件路径");
        }
        deploymentBuilder.addClasspathResource(bpmnUrl);
        if(pngUrl != null){
            deploymentBuilder.addClasspathResource(pngUrl);
        }

        Deployment deploy = deploymentBuilder.deploy();
        OutPutInfo.outputDeploy(deploy);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        OutPutInfo.outputProcessDefinition(processDefinition);
        return processDefinition;
    }

    //用于手动重新部署流程的方法
    public ProcessDefinition deployProcessRe(String bpmnUrl, String pngUrl){
        ProcessDefinition processDefinition = deployProcess(bpmnUrl, pngUrl);
        processDefinitionVersion = processDefinition.getVersion();
        processDefinitionId = processDefinition.getId();
        return processDefinition;
    }

    //启动一个流程
    public ProcessInstance startProcessSetCandidate(String id){
        if(id == null){
            id = processDefinitionId;
        }
        //assignInfo.put("userIDs","");
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(id,assignInfo);
        return processInstance;
    }

    public ProcessInstance startProcessSetAssign(String id){
        if(id == null){
            id = processDefinitionId;
        }
        assignInfo.put("userIDs","我是,天才");
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceById(id,assignInfo);
        return processInstance;
    }

    public List<ProcessInstance> queryProcessInstanceByKey(String key){
        return processEngine.getRuntimeService().createProcessInstanceQuery().processDefinitionKey(key).list();
    }

    public List<ProcessInstance> queryProcessInstanceByDefinitionId(String id){
        return processEngine.getRuntimeService().createProcessInstanceQuery().processDefinitionId(id).list();
    }

    public int getNowVersion(){
        return processDefinitionVersion;
    }

    public String getProcessDefinitionId(){
        return processDefinitionId;
    }

    public List<Task> queryTaskByUser(String userId){
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        return userId == null?taskQuery.list():taskQuery.taskCandidateUser(userId).list();
    }

    public List<Task> queryTaskByGroup(String groupId){
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        return groupId == null?taskQuery.list():taskQuery.taskCandidateGroup(groupId).list();
    }

    public List<Task> queryTaskByAssign(String assignId){
        TaskQuery taskQuery = processEngine.getTaskService().createTaskQuery();
        List<Task> xm = taskQuery.taskAssignee(assignId).list();
        return xm;
    }

    public void claimTask(Task task,String userId){
        processEngine.getTaskService().claim(task.getId(),userId);
    }

}
