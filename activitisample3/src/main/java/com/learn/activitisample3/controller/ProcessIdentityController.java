package com.learn.activitisample3.controller;

import com.learn.activitisample3.entity.Result;
import com.learn.activitisample3.service.DeptService;
import com.learn.activitisample3.service.ProcessIdentityService;
import com.learn.activitisample3.service.UserService;
import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RestController
@RequestMapping("/identity")
@CrossOrigin
public class ProcessIdentityController {
    @Autowired
    ProcessIdentityService identityService;
    @Autowired
    UserService userService;
    @Autowired
    DeptService deptService;

    @PostMapping("/add/user")
    public Result addUser(HttpServletRequest request){
        return Result.error("接口尚未開發完成");
    }

    @RequestMapping("/user/remove")
    public Result removeUser(@RequestParam(value = "userId") String userId){
        return Result.error("接口尚未開發完成");
    }

    @PostMapping("/add/dept")
    public Result addDept(HttpServletRequest request){
        return Result.error("接口尚未開發完成");
    }

    @RequestMapping("/dept/remove")
    public Result removeDept(@RequestParam(value = "deptIdentity") String deptIdentity){
        return Result.error("接口尚未開發完成");
    }

    @RequestMapping("/membership/add")
    public Result createMembership(@RequestParam(value = "deptIdentity")String deptIdentity,
                                   @RequestParam(value = "userIdentity")String userIdentity){
        return Result.error("接口尚未開發完成");
    }

    @RequestMapping("/membership/remove")
    public Result removeMembership(@RequestParam(value = "deptIdentity")String deptIdentity,
                                   @RequestParam(value = "userIdentity")String userIdentity){
        return Result.error("接口尚未開發完成");
    }

    @RequestMapping("/delegate")
    public Result delegateTask(@RequestParam(value = "taskId")String taskId,
                               @RequestParam(value = "userIdentity")String userIdentity){
        return Result.error("接口尚未開發完成");
    }

    @RequestMapping("/task/replace/assign")
    public Result replaceAssign(@RequestParam(value = "userIdentity")String userIdentity,
                                @RequestParam(value = "replacerIdentity")String replacerIdentity){
        if(StringUtils.isEmpty(userIdentity) || StringUtils.isEmpty(replacerIdentity)){
            return Result.error("请传入当前任务委派人【userIdentity】，新的任务委托人 【replacerIdentity】");
        }
        identityService.replaceAssignUser(userIdentity,replacerIdentity);
        return Result.success();
    }

    @RequestMapping("/task/replace/candidate")
    public Result replaceCandidate(@RequestParam(value = "userIdentity")String userIdentity,
                                   @RequestParam(value = "replacerIdentity")String replacerIdentity){
        if(StringUtils.isEmpty(userIdentity) || StringUtils.isEmpty(replacerIdentity)){
            return Result.error("请传入当前任务委派人【userIdentity】，新的任务委托人 【replacerIdentity】");
        }
        identityService.replaceCandidateUser(userIdentity,replacerIdentity);
        return Result.success();
    }

    @RequestMapping("/task/replace")
    public Result replaceCandidateAndAssign(@RequestParam(value = "userIdentity")String userIdentity,
                                            @RequestParam(value = "replacerIdentity")String replacerIdentity){
        if(StringUtils.isEmpty(userIdentity) || StringUtils.isEmpty(replacerIdentity)){
            return Result.error("请传入当前任务委派人【userIdentity】，新的任务委托人 【replacerIdentity】");
        }
        identityService.replaceAssignAndCandidate(userIdentity,replacerIdentity);
        return Result.success();
    }
}
