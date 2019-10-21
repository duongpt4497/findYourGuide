/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Account;
import entity.AuthenticationImp;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author dgdbp
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthProvider authenticationManager;
    private TokenHelper TokenHelper;

    public AuthenticationFilter(AuthProvider authenticationManager, TokenHelper tokenService) {
        this.authenticationManager = authenticationManager;
        this.TokenHelper = tokenService;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            System.out.println("to Authen filter");
            Account acc = new ObjectMapper().readValue(request.getInputStream(), Account.class);
            System.out.println(acc.getPassword() + "/" + acc.getUserName() + "/" + acc.getRole());
            return authenticationManager.authenticate(new AuthenticationImp(acc));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        try {
            System.out.println(authResult.getCredentials().toString());
            String token = TokenHelper.createToken(authResult.getCredentials().toString());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(token);
            response.getWriter().flush();
            response.getWriter().close();
            //response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        } catch (IOException ex) {
            Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
