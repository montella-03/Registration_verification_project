package com.Regestration.security.events.EventListener;

import com.Regestration.security.Entity.User;
import com.Regestration.security.events.RegistrationEvent;
import com.Regestration.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
@Slf4j
public class EventListener implements ApplicationListener<RegistrationEvent> {
    @Autowired
    private UserService userService;
    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        //creating the verification token for the user here
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationToken(token,user);
        String url = event.getApplicationUrl()+"verification token?token= "+token;
        log.info("click the link to verify your account:{}",url);

    }
}
