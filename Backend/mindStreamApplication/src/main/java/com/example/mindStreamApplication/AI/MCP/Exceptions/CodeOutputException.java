package com.example.mindStreamApplication.AI.MCP.Exceptions;

public class CodeOutputException extends RuntimeException {
    public CodeOutputException(String message){
        super(message);
    }

    public CodeOutputException(String message, Throwable cause){
        super(message, cause);
    }

    
}
