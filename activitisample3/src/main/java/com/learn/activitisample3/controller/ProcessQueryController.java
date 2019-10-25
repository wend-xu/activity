package com.learn.activitisample3.controller;

import com.learn.activitisample3.entity.Result;
import com.learn.activitisample3.service.ProcessDeployService;
import com.learn.activitisample3.service.ProcessTaskService;
import com.learn.activitisample3.util.BaseTools;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/query")
@RestController
@CrossOrigin
public class ProcessQueryController {
    @Autowired
    ProcessDeployService deployService;
    @Autowired
    ProcessTaskService taskService;

    @RequestMapping("/pd/processDefinitionId")
    public Result queryDefinition(@RequestParam("definitionId") String definitionId){
        if(StringUtils.isEmpty(definitionId)){
            return Result.error("查询需要流程定义ID");
        }
        ProcessDefinition definition = deployService.getProcessDefinition(definitionId);
        return Result.success("查询成功，流程定义ID："+definition.getId(),BaseTools.outputEntityInfo2(definition));
    }

    @RequestMapping("/pd/definitionKey")
    public Result queryDefinitionByKey(@RequestParam(value = "definitionKey",defaultValue = "")String definitionKey,
                                       @RequestParam(value = "version",defaultValue = "-1")int version,
                                       @RequestParam(value = "page",defaultValue = "0")int page,
                                       @RequestParam(value = "size",defaultValue = "20")int size){
        if(StringUtils.isEmpty(definitionKey)){
            return Result.error("查询需要流程定义的key");
        }
        if(version != -1){
            ProcessDefinition definition = deployService.getProcessDefinitionByKeyAndVersion(definitionKey, version);
            Map<String,Object> data = BaseTools.outputEntityInfo2(definition);
            return Result.success("查询成功，流程定义ID："+definition.getId(),data);
        }else{
            List<ProcessDefinition> definitionList = deployService.getProcessDefinitionsByKey(page, size, definitionKey);
            return Result.success("查询成功",BaseTools.outputListInfo(definitionList));
        }
    }

    @RequestMapping("/pd/deploymentId")
    public Result queryDefinitionByDeploymentId(@RequestParam(value = "deploymentId",defaultValue = "")String deploymentId) throws IllegalAccessException{
        if (StringUtils.isEmpty(deploymentId)){
            return Result.error("查询需要部署ID");
        }
        ProcessDefinition definition = deployService.getProcessDefinitionByDeploymentId(deploymentId);
        //return Result.success("查询成功，流程定义ID："+definition.getId(),BaseTools.outputEntityInfo2(definition));
        return Result.success("查询成功，流程定义ID："+definition.getId());
    }

    @RequestMapping("/pi/processInstanceId")
    public Result queryProcessInstanceById(@RequestParam(value = "processInstanceId")String id){
        ProcessInstance processInstance = deployService.getProcessInstanceById(id);
        return Result.success("查询成功",BaseTools.outputEntityInfo2(processInstance));
    }

    @RequestMapping("/pi/processDefinitionId")
    public Result queryProcessInstanceByDefinitionId(@RequestParam(value = "processDefinitionId")String processDefinitionId,
                                                     @RequestParam(value = "page",defaultValue = "0")int page,
                                                     @RequestParam(value = "size",defaultValue = "20")int size){
        List<ProcessInstance> processInstanceList = deployService.getProcessInstancesByDefinitionId(page, size, processDefinitionId);
        return Result.success("查询成功",BaseTools.outputListInfo(processInstanceList));
    }

    @RequestMapping("/pi/processDefinitionKey")
    public Result queryProcessInstanceByDefinitionKey(@RequestParam(value = "processDefinitionKey")String processDefinitionKey,
                                                      @RequestParam(value = "page",defaultValue = "0")int page,
                                                      @RequestParam(value = "size",defaultValue = "20")int size){
        List<ProcessInstance> processInstanceList = deployService.getProcessInstancesByDefinitionKey(page, size, processDefinitionKey);
        return Result.success("查询成功",BaseTools.outputListInfo(processInstanceList));
    }

    @RequestMapping("/task/processInstanceId")
    public Result queryTaskById(@RequestParam(value = "processInstanceId")String processInstanceId,
                                @RequestParam(value = "page",defaultValue = "0")int page,
                                @RequestParam(value = "size",defaultValue = "20")int size){
        List<Task> taskList = taskService.getTaskByProcessInstanceId(page, size, processInstanceId);
        return Result.success("查询成功",BaseTools.outputListInfo2(taskList));
    }

    @RequestMapping("/task/candidate/user")
    public Result queryTaskByCandidateUserIdentity(@RequestParam(value = "userIdentity")String userIdentity,
                                                   @RequestParam(value = "page",defaultValue = "0")int page,
                                                   @RequestParam(value = "size",defaultValue = "20")int size){
        List<Task> taskList = taskService.getTaskByCandidateUser(page, size, userIdentity);
        return Result.success("查询成功",BaseTools.outputListInfo2(taskList));
    }

    @RequestMapping("/task/candidate/dept")
    public Result queryTaskByCandidateDeptIdentity(@RequestParam(value = "deptIdentity")String deptIdentity,
                                                   @RequestParam(value = "page",defaultValue = "0")int page,
                                                   @RequestParam(value = "size",defaultValue = "20")int size){
        List<Task> taskList = taskService.getTaskByCandidateDept(page, size, deptIdentity);
        return Result.success("查询成功",BaseTools.outputListInfo2(taskList));
    }

    @RequestMapping("/task/assign/user")
    public Result queryTaskByAssignUserIdentity(@RequestParam(value = "deptIdentity")String userIdentity,
                                                @RequestParam(value = "page",defaultValue = "0")int page,
                                                @RequestParam(value = "size",defaultValue = "20")int size){
        List<Task> taskList = taskService.getTaskAssign(page, size, userIdentity);
        return Result.success("查询成功",BaseTools.outputListInfo2(taskList));
    }

    @RequestMapping("/task/form")
    public Result queryFormPropertiesById(@RequestParam(value = "taskId")String taskId){
        List<FormProperty> taskFormProperties = taskService.getTaskFormProperties(taskId);
        return Result.success("查询成功",BaseTools.outputListInfo2(taskFormProperties));
    }
}
