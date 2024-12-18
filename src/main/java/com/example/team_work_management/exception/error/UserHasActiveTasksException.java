package com.example.team_work_management.exception.error;

public class UserHasActiveTasksException extends RuntimeException{
    public UserHasActiveTasksException(String message){
        super(message);
    }
}
