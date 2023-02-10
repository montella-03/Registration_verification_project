package com.Regestration.security.events;

import com.Regestration.security.Entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class RegistrationEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;
    public RegistrationEvent(User user, String applicationUrl) {
        super(user);
        this.user=user;
        this.applicationUrl=applicationUrl;
    }
}
