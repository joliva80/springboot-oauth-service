package com.springboot.app.oauth.service;

import com.springboot.app.commonslib.model.entity.User;

public interface UserServiceInterface {
    public User findByUsername(String username);
}
