package com.learn.activitisample2.service;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ProcessTaskService {
    //获取待办任务
    Task getTaskById(String taskId);
    List<Task> getTaskByProcessInstanceId(String processInstanceId);
    List<Task> getTaskByCandidateUser(int page, int size, String userIdentity);
    List<Task> getTaskByCandidateDept(int page, int size, String deptIdentity);
    List<Task> getTaskAssign(int page, int size, String userIdentity);

    //获取task需要的表单数据
    List<FormProperty> getTaskFormProperties(Task task);
    List<FormProperty> getTaskFormProperties(String taskId);

    //分配候选者
    void taskAddCadidateUser(String taskId,String ... userIdentity);
    void taskAddCadidateUsers(String taskId,List<String> userIdentitys);
    void taskAddCadidateDept(String taskId,String ... deptIdentity);

    //处理待办任务
    void handleTask(Task task,Map<String,Object> variable);
    void handleTask(String taskId,Map<String,Object> variable);
}
