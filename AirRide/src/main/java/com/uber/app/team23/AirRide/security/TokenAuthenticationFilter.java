package com.uber.app.team23.AirRide.security;

import com.uber.app.team23.AirRide.Utils.TokenUtils;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private TokenUtils tokenUtils;
    private UserDetailsService userDetailsService;

    protected final Log LOGGER = LogFactory.getLog(getClass());

    public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService) {
        this.tokenUtils = tokenHelper;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String email;

        String authToken = tokenUtils.getToken(request);
        try {
            if (authToken != null) {

                // 2. Citanje korisnickog imena iz tokena
                email = tokenUtils.getEmailFromToken(authToken);

                if (email != null) {

                    // 3. Preuzimanje korisnika na osnovu username-a
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    // 4. Provera da li je prosledjeni token validan
                    System.err.println("BEFORE VALIDATION");
                    if (tokenUtils.validateToken(authToken, userDetails)) {
                        // 5. Kreiraj autentifikaciju
                        System.err.println("VALID TOKEN");
                        TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
                        authentication.setToken(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.err.println(userDetails.getUsername());
                        for(Object r:  SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
                            Role role = (Role) r;
                            System.err.println(role.getAuthority());
                        }
                    }
                }
            } else {
            }
        } catch (ExpiredJwtException ex) {
            LOGGER.debug("Token expired");
        }

        filterChain.doFilter(request, response);
    }
}
