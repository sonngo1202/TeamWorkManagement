package com.example.team_work_management.exception.error;

public class UserIsNotInGroupException extends RuntimeException{
    public UserIsNotInGroupException(String message){
        super(message);
    }
}
