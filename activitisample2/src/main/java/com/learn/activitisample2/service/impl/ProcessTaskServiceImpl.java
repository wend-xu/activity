package com.learn.activitisample2.service.impl;

import com.learn.activitisample2.service.ProcessBaseService;
import com.learn.activitisample2.service.ProcessTaskService;
import com.learn.activitisample2.util.QueryTool;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ProcessTaskServiceImpl implements ProcessTaskService{
    @Autowired
    QueryTool queryTool;

    private TaskService taskService;
    private FormService formService;
    private TaskQuery taskQuery;

    @Autowired
    public ProcessTaskServiceImpl(ProcessBaseService processBaseService) {
        ProcessEngine processEngine = processBaseService.getProcessEngine();
        taskService = processEngine.getTaskService();
        taskQuery = taskService.createTaskQuery();
        formService = processEngine.getFormService();
    }

    @Override
    public Task getTaskById(String taskId) {
        return taskQuery.taskId(taskId).singleResult();
    }

    public List<Task> getTaskByProcessInstanceId(String processInstanceId) {
        return taskQuery.processInstanceId(processInstanceId).list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Task> getTaskByCandidateUser(int page, int size, String userIdentity) {
        return queryTool.query(page,size,userIdentity,queryTool.QUERY_TASK_BY_CANDIDATE_USER);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Task> getTaskByCandidateDept(int page, int size, String deptIdentity) {
        return queryTool.query(page,size,deptIdentity,queryTool.QUERY_TASK_BY_CANDIDATE_DEPT);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Task> getTaskAssign(int page, int size, String userIdentity) {
        return queryTool.query(page,size,userIdentity,queryTool.QUERY_TASK_BY_ASSIGN_USER);
    }

    @Override
    public List<FormProperty> getTaskFormProperties(Task task) {
        return getTaskFormProperties(task.getId());
    }

    @Override
    public List<FormProperty> getTaskFormProperties(String taskId) {
        return formService.getTaskFormData(taskId).getFormProperties();
    }

    @Override
    public void taskAddCandidateUser(String taskId, String... userIdentities) {
        for(String userIdentity:userIdentities){
            taskService.addCandidateUser(taskId,userIdentity);
        }
    }

    @Override
    public void taskAddCandidateUsers(String taskId, List<String> userIdentities) {
        userIdentities.forEach(userIdentity -> taskService.addCandidateUser(taskId,userIdentity));
    }

    @Override
    public void taskAddCandidateDept(String taskId, String... deptIdentities) {
        for(String deptIdentity:deptIdentities){
            taskService.addCandidateGroup(taskId,deptIdentity);
        }
    }

    @Override
    public void taskAssign(String taskId, String userIdentity) {
        taskService.claim(taskId,userIdentity);
    }

    @Override
    public void handleTask(Task task, Map<String, Object> variable) {
        taskService.complete(task.getId(),variable);
    }

    @Override
    public void handleTask(String taskId, Map<String, Object> variable) {
        taskService.complete(taskId,variable);
    }
}
