package com.honestbite.www.auth.config;

import com.honestbite.www.auth.service.JWTService;
import com.honestbite.www.auth.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        // DEBUG LOG 1
        System.out.println("====== JWT FILTER START ======");
        System.out.println("Authorization Header: " + authHeader);

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            try {
                email = jwtService.extractEmail(token);
                System.out.println("Extracted Email from Token: " + email); // DEBUG LOG 2
            } catch (Exception e) {
                System.out.println("Token parsing failed: " + e.getMessage());
            }
        }

        if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(email);
            boolean isValid = jwtService.validateToken(token, userDetails);

            System.out.println("Is Token Valid against UserDetails? " + isValid); // DEBUG LOG 3

            if(isValid){
                UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authtoken);
                System.out.println("SecurityContext successfully authenticated!");
            }
        }

        System.out.println("====== JWT FILTER END ======");
        filterChain.doFilter(request, response);
    }


}
