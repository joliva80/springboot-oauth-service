package com.springboot.app.oauth.clients;

import com.springboot.app.commonslib.model.entity.User;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="users-service")
public interface UserFeignClientInterface {
    
    @GetMapping("/users/search/username")
    public User findByUsername(@RequestParam String username);

}
