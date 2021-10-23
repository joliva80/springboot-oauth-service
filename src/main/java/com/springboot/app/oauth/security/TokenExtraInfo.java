package com.springboot.app.oauth.security;

import java.util.HashMap;
import java.util.Map;

import com.springboot.app.commonslib.model.entity.User;
import com.springboot.app.oauth.service.UserServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;


@Component
public class TokenExtraInfo implements TokenEnhancer {

    @Autowired
    private UserServiceInterface userService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        
        Map<String, Object> infoMap = new HashMap<String, Object>();

        User user = userService.findByUsername(authentication.getName());
        infoMap.put("name", user.getName());
        infoMap.put("mail", user.getEmail());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(infoMap);
        
        return accessToken;
    }
    

}
