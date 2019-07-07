package com.learn.activitisample2.dao;

import com.learn.activitisample2.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptDao extends JpaRepository<Dept, Long>, JpaSpecificationExecutor<Dept> {
}
