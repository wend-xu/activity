package com.learn.activitisample3.dao;

import com.learn.activitisample3.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptDao extends JpaRepository<Dept, Long>, JpaSpecificationExecutor<Dept> {
    Dept findByDeptIdentity(String deptIdentity);
}
