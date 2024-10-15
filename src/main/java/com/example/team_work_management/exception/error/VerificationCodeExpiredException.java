package com.example.team_work_management.exception.error;

public class VerificationCodeExpiredException extends RuntimeException{
    public VerificationCodeExpiredException(String message){
        super(message);
    }
}
