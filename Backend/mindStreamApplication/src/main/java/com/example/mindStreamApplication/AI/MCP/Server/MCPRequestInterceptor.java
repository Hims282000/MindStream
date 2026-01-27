package com.example.mindStreamApplication.AI.MCP.Server;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MCPRequestInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String path= request.getRequestURI();
        if(path.contains("/api/ai/")){
            String mcpVersion= request.getHeader("X-MCP-Version");
            if (mcpVersion ==null) {
                response.getStatus(400);
                return false;
                
            }
        }
        return true;
    }
    
}
