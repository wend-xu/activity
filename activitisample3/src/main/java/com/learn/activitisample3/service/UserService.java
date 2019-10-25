package com.learn.activitisample3.service;

import com.learn.activitisample3.entity.User;

public interface UserService {
    User getUserById(String userId);
    User getUserByIdentity(String userIdentity);
}
