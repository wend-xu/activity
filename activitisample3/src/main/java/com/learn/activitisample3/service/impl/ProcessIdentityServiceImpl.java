package com.learn.activitisample3.service.impl;

import com.learn.activitisample3.dao.DeptDao;
import com.learn.activitisample3.dao.UserDao;
import com.learn.activitisample3.entity.Dept;
import com.learn.activitisample3.entity.User;
import com.learn.activitisample3.service.ProcessBaseService;
import com.learn.activitisample3.service.ProcessIdentityService;
import com.learn.activitisample3.util.SystemConstant;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessIdentityServiceImpl implements ProcessIdentityService {
    private IdentityService identityService;
    private TaskService taskService;

    @Autowired
    UserDao userDao;
    @Autowired
    DeptDao deptDao;

    @Autowired
    public ProcessIdentityServiceImpl(ProcessBaseService processBaseService){
        identityService = processBaseService.getProcessEngine().getIdentityService();
        taskService = processBaseService.getProcessEngine().getTaskService();
    }

    @Override
    public void addUser(User user) {
        userDao.save(user);
        identityService.saveUser(identityService.newUser(user.getUserIdentity()));
    }

    @Override
    public void removeUser(String userIdentity) {
        User user = userDao.findByUserIdentity(userIdentity);
        List<Task> assigneeList = taskService.createTaskQuery().taskAssignee(userIdentity).list();
        if(user != null && assigneeList.isEmpty()){
            List<Task> taskCandidateList = taskService.createTaskQuery().taskCandidateUser(userIdentity).list();
            if(!taskCandidateList.isEmpty()){
                taskCandidateList.forEach(task -> taskService.deleteCandidateUser(task.getId(),userIdentity));
            }
            identityService.deleteUser(userIdentity);
            user.setActive(false);
            userDao.save(user);
        }else{
            StringBuilder tip = new StringBuilder();
            if(user == null){
                tip.append("待删除用户为空；");
            }
            if(!assigneeList.isEmpty()){
                assigneeList.forEach(task -> tip.append("存在待办任务："+task.getId()+";"));
            }
            throw new ActivitiException(tip.toString());
        }
    }

    @Override
    public void replaceCandidateUser(String userIdentity , String replacerIdentity) {
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(userIdentity).list();
        taskList.forEach(task -> {
            String taskId = task.getId();
            taskService.deleteCandidateUser(taskId,userIdentity);
            taskService.addCandidateUser(taskId,replacerIdentity);
        });
    }

    @Override
    public void replaceAssignUser(String userIdentity , String replacerIdentity) {
        List<Task> taskList = taskService.createTaskQuery().taskAssignee(userIdentity).list();
        taskList.forEach(task -> {
            String taskId = task.getId();
            taskService.unclaim(taskId);
            taskService.claim(taskId,replacerIdentity);
        });
    }

    @Override
    public void delegateTask(String taskId,String assignIdentity) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(!task.getDelegationState().equals(SystemConstant.TASK_DELEGATION_STATE_RESOLVED)){
            taskService.delegateTask(taskId,assignIdentity);
        }
    }

    @Override
    public void replaceAssignAndCandidate(String userIdentity , String replacerIdentity) {
        replaceCandidateUser(userIdentity,replacerIdentity);
        replaceAssignUser(userIdentity,replacerIdentity);
    }

    @Override
    public void addDept(Dept dept) {
        deptDao.save(dept);
        identityService.saveGroup(identityService.newGroup(dept.getDeptIdentity()));
    }

    @Override
    public void removeDept(String deptIdentity) {
        Dept dept = deptDao.findByDeptIdentity(deptIdentity);
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(deptIdentity).list();
        if(dept != null && taskList.isEmpty()){
            dept.setActive(false);
            deptDao.save(dept);
            identityService.deleteGroup(deptIdentity);
        }else{
            String taskIds = "";
            for(Task task:taskList){
                taskIds += (task.getId()+";");
            }
            throw new ActivitiException("当前部门未如下任务的候选部门："+taskIds);
        }
    }

    @Override
    public void createMemberShip(String deptIdentity,String ... userIdentities) {
        for(String userIdentity:userIdentities)
            identityService.createMembership(deptIdentity,userIdentity);
    }

    @Override
    public void removeMemberShip(String deptIdentity, String userIdentity) {
        identityService.createMembership(deptIdentity,userIdentity);
    }
}
