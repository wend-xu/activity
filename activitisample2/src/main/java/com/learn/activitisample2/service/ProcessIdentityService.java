package com.learn.activitisample2.service;

import com.learn.activitisample2.entity.Dept;
import com.learn.activitisample2.entity.User;

public interface ProcessIdentityService {
    //用户操作
    void addUser(User user);
    void removeUser(String userIdentity);

    //替换用户，将当前用户的任务转移到replaceer
    void replaceCandidateUser(User user , User replacer);
    void replaceAssignUser(User user , User replacer);
    void replaceAssignAndCandidate(User user , User replacer);
    //委托任务 委托任务后原来的办理人将变成owner
    void delegateTask(String taskId,User assign);

    //用户组操作
    void addDept(Dept dept);
    void removeDept(String deptIdentity);

    //用户、用户组关联操作
    void createMemberShip(Dept dept,User ... users);
    void removeMemberShip(String deptIdentity, String userIdentity);
}
