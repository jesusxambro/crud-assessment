package com.crud.hillcountry.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.NonNull;

public class Authentication {
    boolean authenticated;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    UserPublic user;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public UserPublic getUser() {
        return user;
    }

    public void setUser(UserPublic user) {
        this.user = user;
    }
}
