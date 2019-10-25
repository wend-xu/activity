package com.learn.activitisample3.controller;


import com.learn.activitisample3.entity.Result;
import com.learn.activitisample3.service.ProcessDeployService;
import com.learn.activitisample3.util.BaseTools;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RestController
@RequestMapping("/deploy")
@CrossOrigin
public class ProcessDeployController {
    @Autowired
    ProcessDeployService deployService;

   @RequestMapping("/start")
    public Result startProcess(@RequestParam(value = "processDefinitionId") String processDefinitionId){
       ProcessInstance processInstance = deployService.startProcess(processDefinitionId);
       if(processInstance != null){
           Result result = Result.success();
           //executions
           Map<String, Object> map = BaseTools.outputEntityInfo2(processInstance);
           map.remove("executions");
           result.setData(map);
           return result;
       }
       return Result.error("启动流程实例失败");
   }

    @RequestMapping("/{processInstanceId}/suspend")
    public Result suspendProcess(@PathVariable String processInstanceId){
       deployService.suspendProcess(processInstanceId);
       ProcessInstance processInstance = deployService.getProcessInstanceById(processInstanceId);
       return processInstance.isSuspended()?Result.success("挂起流程实例成功"):Result.error("挂起流程实例失败");
    }

    @RequestMapping("/{processInstanceId}/activity")
    public Result activityProcess(@PathVariable String processInstanceId){
       deployService.activateProcess(processInstanceId);
        ProcessInstance processInstance = deployService.getProcessInstanceById(processInstanceId);
        return !processInstance.isSuspended()?Result.success("激活流程实例成功"):Result.error("激活流程实例失败");
    }
}
