/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Arrays;
/**
 *
 * @author dgdbp
 */
@Component
public class TokenAuthenFilter extends OncePerRequestFilter{
    private TokenHelper tokenService;

    private PrincipalService principalService;
    @Autowired
    public TokenAuthenFilter(TokenHelper tokenService, PrincipalService principalService) {
        this.tokenService = tokenService;
        this.principalService = principalService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain fc) throws ServletException, IOException {
        
        String username;
        String authToken = tokenService.resolveToken(request);

        //logger.warn("AuthToken: " +authToken);

        if (authToken != null) {
            // get username from token
            username = tokenService.getUsername(authToken);
            //logger.warn("UserName: "+username);
            if (username != null) {
                // get user
                UserDetails userDetails = principalService.loadUserByUsername(username);
                if (tokenService.validateToken(authToken, userDetails)) {
                    // create authentication
                    TokenBaseAuthentication authentication = new TokenBaseAuthentication(userDetails);
                    authentication.setToken(authToken);
                    System.out.println(authentication.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication); // Adding Token in Security COntext
                }
            }else{
                logger.warn("Something is wrong with Token.");
            }
        }
        System.out.println(request.getHeader("Sec-Fetch-Mode"));
        fc.doFilter(request, response);
    }
    
}
