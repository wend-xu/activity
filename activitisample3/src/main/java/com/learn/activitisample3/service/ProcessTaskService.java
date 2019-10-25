package com.learn.activitisample3.service;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ProcessTaskService {
    //获取待办任务
    Task getTaskById(String taskId);
    List<Task> getAllActivityTask();
    List<Task> getTaskByProcessInstanceId(int page, int size, String processInstanceId);
    List<Task> getTaskByCandidateUser(int page, int size, String userIdentity);
    List<Task> getTaskByCandidateDept(int page, int size, String deptIdentity);
    List<Task> getTaskAssign(int page, int size, String userIdentity);

    //获取task需要的表单数据
    List<FormProperty> getTaskFormProperties(Task task);
    List<FormProperty> getTaskFormProperties(String taskId);

    //分配候选者
    void taskAddCandidateUser(String taskId,String ... userIdentities);
    void taskAddCandidateUsers(String taskId,List<String> userIdentities);
    void taskAddCandidateDept(String taskId,String ... deptIdentities);

    void taskAssign(String taskId,String userIdentity);

    //处理待办任务
    void handleTask(Task task,Map<String,Object> variable);
    void handleTask(String taskId,Map<String,Object> variable);
}
