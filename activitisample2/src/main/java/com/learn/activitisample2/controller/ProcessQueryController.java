package com.learn.activitisample2.controller;

import com.learn.activitisample2.entity.Result;
import com.learn.activitisample2.service.ProcessDeployService;
import com.learn.activitisample2.util.BaseTools;
import org.activiti.engine.repository.ProcessDefinition;
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
    BaseTools baseTools;

    @RequestMapping("/definition/definitionId/{definitionId}")
    public Result queryDefinition(@PathVariable String definitionId){
        if(StringUtils.isEmpty(definitionId)){
            return new Result("查询需要流程定义ID",Result.RESULT_STATE_ERROR,null);
        }
        ProcessDefinition definition = deployService.getProcessDefinition(definitionId);
        return new Result("查询成功",Result.RESULT_STATE_OK,baseTools.outputEntityInfo2(definition));
    }

    @RequestMapping("/definition/definitionKey/version")
    public Result queryDefinition(@RequestParam(value = "definitionKey",defaultValue = "")String definitionKey,
                                  @RequestParam(value = "version")int version){
        if(StringUtils.isEmpty(definitionKey)||version==0){
            return new Result("查询需要流程定义的key以及version",Result.RESULT_STATE_ERROR,null);
        }
        ProcessDefinition definition = deployService.getProcessDefinitionByKeyAndVersion(definitionKey, version);
        return new Result("查询成功",Result.RESULT_STATE_OK,baseTools.outputEntityInfo2(definition));
    }

    @RequestMapping("/definition/deploymentId")
    public Result queryDefinitionByDeploymentId(@RequestParam(value = "deploymentId",defaultValue = "")String deploymentId){
        if (StringUtils.isEmpty(deploymentId)){
            return new Result("查询需要部署ID",Result.RESULT_STATE_ERROR,null);
        }
        ProcessDefinition definition = deployService.getProcessDefinitionByDeploymentId(deploymentId);
        return new Result("查询成功",Result.RESULT_STATE_OK,baseTools.outputEntityInfo2(definition));
    }

    @RequestMapping("/definition/definitionKey")
    public Result queryDefinitionByDefinitionKey(@RequestParam(value = "definitionKey",defaultValue = "")String definitionKey,
                                                 @RequestParam(value = "page",defaultValue = "1")int page,
                                                 @RequestParam(value = "size",defaultValue = "10")int size){
        if(StringUtils.isEmpty(definitionKey)){
            return new Result("查询需要流程定义的key",Result.RESULT_STATE_ERROR,null);
        }
        if(page>0){page -= 1;}
        List<ProcessDefinition> definitions = deployService.getProcessDefinitionsByKey(page, size, definitionKey);
        List<Map<String,Object>> result = new ArrayList<>();
        definitions.forEach(processDefinition -> result.add(baseTools.outputEntityInfo2(processDefinition)));
        return new Result("查询成功",Result.RESULT_STATE_OK,result);
    }
}
