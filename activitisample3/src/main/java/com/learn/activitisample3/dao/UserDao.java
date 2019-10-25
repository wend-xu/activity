package com.learn.activitisample3.dao;

import com.learn.activitisample3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User,Long>{
    User findByUserIdentity(String userIdentity);
}
