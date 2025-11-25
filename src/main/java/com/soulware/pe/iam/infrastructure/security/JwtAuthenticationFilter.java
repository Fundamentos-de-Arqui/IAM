package com.soulware.pe.iam.infrastructure.security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(urlPatterns = {"/api/protected/*"})
public class JwtAuthenticationFilter implements Filter {
    
    private final JwtUtil jwtUtil = new JwtUtil();
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Allow OPTIONS requests (CORS preflight)
        if ("OPTIONS".equals(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        
        String authHeader = httpRequest.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\":\"Missing or invalid Authorization header\"}");
            return;
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        
        if (!jwtUtil.validateToken(token) || jwtUtil.isTokenExpired(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"error\":\"Invalid or expired token\"}");
            return;
        }
        
        // Add user info to request attributes for use in controllers
        String identityDocumentNumber = jwtUtil.getIdentityDocumentNumberFromToken(token);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        httpRequest.setAttribute("identityDocumentNumber", identityDocumentNumber);
        httpRequest.setAttribute("userId", userId);
        
        chain.doFilter(request, response);
    }
}
