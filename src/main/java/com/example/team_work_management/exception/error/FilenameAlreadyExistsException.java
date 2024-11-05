package com.example.team_work_management.exception.error;

public class FilenameAlreadyExistsException extends RuntimeException{
    public FilenameAlreadyExistsException(String message){
        super(message);
    }
}
