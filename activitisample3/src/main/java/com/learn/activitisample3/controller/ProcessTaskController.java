package com.learn.activitisample3.controller;

import com.learn.activitisample3.entity.Result;
import com.learn.activitisample3.service.ProcessTaskService;
import org.activiti.engine.form.FormProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RestController
@RequestMapping("/task")
@CrossOrigin
public class ProcessTaskController {
    @Autowired
    ProcessTaskService taskService;

    @RequestMapping("/{taskId}/candidate/add")
    public Result addCandidate(@PathVariable String taskId,
                                   @RequestParam(value = "userIdentity",defaultValue = "") String userIdentity,
                                   @RequestParam(value = "deptIdentity",defaultValue = "") String deptIdentity){
        if(StringUtils.isEmpty(taskId)){
            return Result.error("taskID为空");
        }

        int flag = 0;
        if(!StringUtils.isEmpty(userIdentity)){
            if(userIdentity.contains(".")){
                taskService.taskAddCandidateUser(taskId,userIdentity.split(","));
            }else{
                taskService.taskAddCandidateUser(taskId,userIdentity);
            }
            flag += 1;
        }
        if(!StringUtils.isEmpty(deptIdentity)){
            if(deptIdentity.contains(".")){
                taskService.taskAddCandidateDept(taskId,deptIdentity.split(","));
            }else{
                taskService.taskAddCandidateDept(taskId,deptIdentity);
            }
            flag += 1;
        }
        return flag != 0?Result.success():
                Result.error("候選用戶參數 【userIdentity】及 候選部門參數【deptIdentity】爲空，請傳入合法參數，可傳入任一或同時傳入，可使用【,】分隔同类参数");
    }


    @RequestMapping("/{taskId}/assign")
    public Result assignTask(@PathVariable String taskId,@RequestParam(value = "userIdentity") String userIdentity){
        if(!StringUtils.isEmpty(taskId) && !StringUtils.isEmpty(userIdentity)){
           taskService.taskAssign(taskId,userIdentity);
           return Result.success();
        }
        return Result.error("请传入【taskId】及【userIdentity】");

    }

    //處理待辦任務，通過表單提交數據
    @RequestMapping("/{taskId}/handle")
    public Result handleTask(@PathVariable String taskId,
                             HttpServletRequest request, HttpServletResponse response){
        Map<String, String[]> parameterMap = request.getParameterMap();

        List<FormProperty> taskFormProperties = taskService.getTaskFormProperties(taskId);

        StringBuilder errMsg = new StringBuilder();
        Map<String,Object> variable = new HashMap<>();
        for(FormProperty formProperty:taskFormProperties){
            String propertyId = formProperty.getId();//流程定义文件有空格，这里拿到的ID也会带空格
            String propertyValue = request.getParameter(propertyId.trim());
            if(StringUtils.isEmpty(propertyValue)){
                errMsg.append("获取参数["+propertyId+"]为空，请确认是否传入该参数\n");
                continue;
            }
            variable.put(propertyId,propertyValue);
        }
        String errorMsgStr = errMsg.toString();
        if(StringUtils.isEmpty(errorMsgStr)){
            taskService.handleTask(taskId,variable);
            return Result.success();
        }
        return Result.error(errorMsgStr);
    }

}
