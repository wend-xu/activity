package com.learn.activitisample3.service;

import com.learn.activitisample3.entity.Dept;

public interface DeptService {
    Dept findDeptById(String deptId);
    Dept findDeptByIndetity(String deptIdentity);
}
