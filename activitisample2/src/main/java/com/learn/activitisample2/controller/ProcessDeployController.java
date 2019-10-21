package com.learn.activitisample2.controller;

import com.learn.activitisample2.entity.Result;
import com.learn.activitisample2.service.ProcessDeployService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@Controller
@RestController
@RequestMapping("/deploy")
@CrossOrigin
public class ProcessDeployController {
    @Autowired
    ProcessDeployService deployService;

    @RequestMapping("/definition/default")
    public Result deployDefinition(){
        ProcessDefinition definition = deployService.deployProcess("processes/MyProcess.bpmn", "processes/MyProcess.png");
        String msg = MessageFormat.format("部署成功,当前部署流程定义id： {} , 版本： {}", definition.getId(), definition.getVersion());
        return new Result(msg, Result.RESULT_STATE_OK,definition);
    }

}
