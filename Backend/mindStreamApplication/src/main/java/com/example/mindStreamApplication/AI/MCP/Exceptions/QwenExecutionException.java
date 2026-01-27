package com.example.mindStreamApplication.AI.MCP.Exceptions;

public class QwenExecutionException extends RuntimeException{
    public QwenExecutionException(String message){
        super("Qwen Execution failed"+message);

    }
    public QwenExecutionException(String message, Throwable cause){
        super("Qwen execution failed"+message,cause);
        
    }
    
}
