package com.springboot.app.oauth.security.events;

import com.springboot.app.commonslib.model.entity.User;
import com.springboot.app.oauth.service.UserServiceInterface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import feign.FeignException;


@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher{

    private Logger logger = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

    @Autowired
    private UserServiceInterface userService;

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        
        // Some action here when error...
        logger.info("Error login: " + exception.getMessage());

        try{
            User user = userService.findByUsername(authentication.getName());
            if( user.getRetries() == null ){
                user.setRetries(0); // initialization
            }
            user.setRetries(user.getRetries() +1);
            logger.error("Retry number --> " + user.getRetries());

            if( user.getRetries() >= 3 ){
                user.setEnabled(false);
                logger.error("User has been disabled: " + user.getUsername());
            }

            userService.update(user, user.getId());
        }
        catch (FeignException e){
            logger.error("Unknown user: " + authentication.getName());
        }
        
        
    }

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {

        // Check if the user is the client or the app, otherwise will raise this event twice.
        if(authentication.getDetails() instanceof WebAuthenticationDetails) {
            return;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Some action here when success...
        logger.info("Success login: " + userDetails.getUsername());

        User user = userService.findByUsername(authentication.getName());
        if( user.getRetries() != null && user.getRetries() > 0){
            logger.info("Login retries reset for user: "+ authentication.getName());
            user.setRetries(0); // Reset retries
            userService.update(user, user.getId());
        }
    }

    
}
