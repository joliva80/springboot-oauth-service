package com.springboot.app.oauth.service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.springboot.app.commonslib.model.entity.User;
import com.springboot.app.oauth.clients.UserFeignClientInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService, UserServiceInterface {

    private Logger log = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserFeignClientInterface userFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = userFeignClient.findByUsername(username);

        if( user == null){
            log.info("Login error, username '"+username+"' does not exist.");
            throw new UsernameNotFoundException("Login error, username '"+username+"' does not exist.");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                                                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                                                            .collect(Collectors.toList());
        
        log.info("User '"+username+"' authenticated successfully.");
        return new org.springframework.security.core.userdetails.User(  user.getUsername(), 
                                                                        user.getPassword(), 
                                                                        authorities);
    }

    @Override
    public User findByUsername(String username) {
        return userFeignClient.findByUsername(username);
    }

  
    
    
}
