package com.learn.activitisample3.service.impl;

import com.learn.activitisample3.dao.DeptDao;
import com.learn.activitisample3.entity.Dept;
import com.learn.activitisample3.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    DeptDao deptDao;

    @Override
    public Dept findDeptById(String deptId) {
        return deptDao.getOne(Long.valueOf(deptId));
    }

    @Override
    public Dept findDeptByIndetity(String deptIdentity) {
        return deptDao.findByDeptIdentity(deptIdentity);
    }
}
