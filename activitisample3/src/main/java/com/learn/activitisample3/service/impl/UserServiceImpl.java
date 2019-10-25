package com.learn.activitisample3.service.impl;

import com.learn.activitisample3.dao.UserDao;
import com.learn.activitisample3.entity.User;
import com.learn.activitisample3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User getUserById(String userId) {
        return userDao.getOne(Long.valueOf(userId));
    }

    @Override
    public User getUserByIdentity(String userIdentity) {
        return userDao.findByUserIdentity(userIdentity);
    }
}
