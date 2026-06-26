package com.example.pingstack.configuration;

import com.example.pingstack.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JWT FILTER HIT: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            String path = request.getServletPath(); // This extract sth lik /api/login
            if(path.equals("/webhook/paystack")){
                filterChain.doFilter(request, response);
            }
            try {
                email = jwtService.extractUserName(token);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            if(jwtService.validateToken(token, email)){
                List<String> roles = jwtService.extractRoles(token);
                List<SimpleGrantedAuthority> authorities = roles.stream().map(role-> role.startsWith("ROLE_")? new SimpleGrantedAuthority(role): new SimpleGrantedAuthority("ROLE_" + role)).toList();

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request,response);


    }
}
