package com.example.team_work_management.exception.error;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(String message){
        super(message);
    }
}
