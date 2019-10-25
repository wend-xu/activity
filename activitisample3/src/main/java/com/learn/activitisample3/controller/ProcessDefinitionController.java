package com.learn.activitisample3.controller;

import com.learn.activitisample3.entity.Result;
import com.learn.activitisample3.service.ProcessBaseService;
import com.learn.activitisample3.service.ProcessDeployService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

@Controller
@RestController
@RequestMapping("/definition")
@CrossOrigin
public class ProcessDefinitionController {
    @Autowired
    ProcessDeployService deployService;
    @Autowired
    ProcessBaseService baseService;

    //測試接口，部署系統自帶的bpmn流程文件
    @RequestMapping("/default")
    public Result deployDefinitionExample(){
        Deployment deployment = deployService.deployProcess("processes/MyProcess.bpmn", "processes/MyProcess.png");
        String msg = MessageFormat.format("流程部署成功 deploymentID： {0} , 部署時間： {1}", deployment.getId(), deployment.getDeploymentTime());
        return deployment == null?Result.error("部署失敗"):Result.success(msg);
    }

    //上傳流程定義文件方式部署
    @PostMapping("/upload")
    public Result uploadDefinition(@RequestParam(value = "bpmn") MultipartFile bpmnFile) throws IOException {
        InputStream bpmnStream = bpmnFile.getInputStream();
        Deployment deployment = deployService.deployProcess(bpmnFile.getName(), bpmnStream);
        String msg = MessageFormat.format("流程部署成功 deploymentID： {0} , 部署時間： {1}", deployment.getId(), deployment.getDeploymentTime());
        return deployment == null?Result.error("部署失敗"):Result.success(msg);
    }

    //部署使用modeler繪製的流程圖
    @RequestMapping("/{modelId}/deploy")
    public Result deployDefinition(@PathVariable String modelId){
        Deployment deployment = deployService.deployProcessByModel(modelId);
        String msg = MessageFormat.format("流程部署成功 deploymentID： {0} , 部署時間： {1}", deployment.getId(), deployment.getDeploymentTime());
        return deployment == null?Result.error("部署失敗"):Result.success(msg);
    }

    @RequestMapping("/{processDefinitionId}/suspend")
    public Result definitionSuspend(@PathVariable String processDefinitionId){
        deployService.suspendProcessDefinition(processDefinitionId);
        ProcessDefinition definition = deployService.getProcessDefinition(processDefinitionId);
        return definition.isSuspended()?Result.success("流程已挂起"):Result.error("流程挂起失敗");
    }

    @RequestMapping("/{processDefinitionId}/activity")
    public Result definitionAcvitity(@PathVariable String processDefinitionId){
        deployService.activateProcessDefinition(processDefinitionId);
        ProcessDefinition definition = deployService.getProcessDefinition(processDefinitionId);
        return (!definition.isSuspended())?Result.success("流程已激活"):Result.error("流程激活失敗");
    }

    @RequestMapping("/{processDefinitionId}/candidate")
    public Result setDefinitionCandidate(@PathVariable String processDefinitionId,
                                             @RequestParam(value = "userIdentity",defaultValue = "") String userIdentity,
                                             @RequestParam(value = "deptIdentity",defaultValue = "") String deptIdentity){
        int flag = 0;
        if(!StringUtils.isEmpty(userIdentity)){
            if(userIdentity.contains(",")){
                String[] userIdentities = userIdentity.split(",");
                deployService.addStartCandidateUser(processDefinitionId,userIdentities);
            }else{
                deployService.addStartCandidateUser(processDefinitionId,userIdentity);
            }
            flag += 1;
        }

        if(!StringUtils.isEmpty(deptIdentity)){
            if(userIdentity.contains(",")){
                String[] deptIdentities = deptIdentity.split(",");
                deployService.addStartCandidateDept(processDefinitionId,deptIdentities);
            }else{
                deployService.addStartCandidateDept(processDefinitionId,deptIdentity);
            }
            flag += 1;
        }

        return flag != 0?Result.success():
                Result.error("候選用戶參數 【userIdentity】及 候選部門參數【deptIdentity】爲空，請傳入合法參數，可傳入任一或同時傳入，可使用【,】分隔同类参数");
    }

}
