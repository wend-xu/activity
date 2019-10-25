package com.learn.activitisample3.service;

import com.learn.activitisample3.entity.Dept;
import com.learn.activitisample3.entity.User;

public interface ProcessIdentityService {
    //用户操作
    void addUser(User user);
    void removeUser(String userIdentity);

    //替换用户，将当前用户的任务转移到replaceer
    void replaceCandidateUser(String userIdentity , String replacerIdentity);
    void replaceAssignUser(String userIdentity , String replacerIdentity);
    void replaceAssignAndCandidate(String userIdentity , String replacerIdentity);
    //委托任务 委托任务后原来的办理人将变成owner
    void delegateTask(String taskId,String assignIdentity);

    //用户组操作
    void addDept(Dept dept);
    void removeDept(String deptIdentity);

    //用户、用户组关联操作
    void createMemberShip(String deptIdentity,String ... userIdentities);
    void removeMemberShip(String deptIdentity, String userIdentity);
}
