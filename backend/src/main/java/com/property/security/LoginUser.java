package com.property.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class LoginUser extends User {
    private final Long userId;
    private final String userType;
    private final String realName;

    public LoginUser(Long userId, String username, String password, String userType,
                     String realName, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.userType = userType;
        this.realName = realName;
    }
}
