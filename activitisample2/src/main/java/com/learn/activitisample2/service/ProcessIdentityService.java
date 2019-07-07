package com.learn.activitisample2.service;

import com.learn.activitisample2.entity.Dept;
import com.learn.activitisample2.entity.User;

public interface ProcessIdentityService {
    //用户操作
    void addUser(User user);
    void removeUser(String userIdentity);
    //替换用户，将当前用户的任务转移到replaceer
    void replaceUser(User user , User replacer);

    //用户组操作
    void addGroup(Dept dept);
    void removeDept(String deptIdentity);

    //用户、用户组关联操作
    void createMenberShip(Dept dept,User ... users);
    void removeMenberShio(String deptIdentity,String userIdentity);
}
