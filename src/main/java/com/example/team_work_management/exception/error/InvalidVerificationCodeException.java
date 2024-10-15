package com.example.team_work_management.exception.error;

public class InvalidVerificationCodeException extends RuntimeException{
    public InvalidVerificationCodeException(String message){
        super(message);
    }
}
