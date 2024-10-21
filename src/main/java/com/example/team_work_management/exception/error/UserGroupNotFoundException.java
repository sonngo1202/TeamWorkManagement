package com.example.team_work_management.exception.error;

public class UserGroupNotFoundException extends RuntimeException{
    public UserGroupNotFoundException(String message){
        super(message);
    }
}
