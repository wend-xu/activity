package com.learn.activitisample2.dao;

import com.learn.activitisample2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User,Long>{
    User findByUserIdentity(String userIdentity);
}
