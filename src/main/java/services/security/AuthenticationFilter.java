/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import entities.Account;
import entities.AuthenticationImp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.stream.Collectors;
import javax.servlet.http.Cookie;

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
            
            Account acc = new ObjectMapper().readValue(request.getInputStream(), Account.class);
            System.out.println(acc.getPassword() + "/" + acc.getUserName() + "/" + acc.getRole());
            System.out.println(request.getHeader("Sec-Fetch-Mode")+"auth filter"); 
            return authenticationManager.authenticate(new AuthenticationImp(acc));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        
            System.out.println(authResult.getPrincipal().toString());
            String token = TokenHelper.createToken(authResult.getPrincipal().toString());
            response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.setStatus(HttpServletResponse.SC_OK);
            Cookie sidCookie = new Cookie("token",token);
            sidCookie.setPath("/");
//            sidCookie.setSecure(true);
            sidCookie.setHttpOnly(true);
            sidCookie.setDomain("localhost");
            response.addCookie(sidCookie);
            System.out.println(request.getHeader("Sec-Fetch-Mode")+"authen success");
            Account res = new Account(Integer.parseInt(authResult.getCredentials().toString())
                    ,authResult.getPrincipal().toString(), authResult.getAuthorities().toArray()[0].toString());
            response.getWriter().write(new Gson().toJson(res));
            //response.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}

// String body = null;
//            StringBuilder stringBuilder = new StringBuilder();
//            BufferedReader bufferedReader = null;
//
//            try {
//                InputStream inputStream = request.getInputStream();
//                if (inputStream != null) {
//                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                    char[] charBuffer = new char[128];
//                    int bytesRead = -1;
//                    while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
//                         System.out.println(stringBuilder.toString());
//                        stringBuilder.append(charBuffer, 0, bytesRead);
//                    }
//                } else {
//                    stringBuilder.append("how?");
//                }
//            } catch (IOException ex) {
//                throw ex;
//            } finally {
//                if (bufferedReader != null) {
//                    try {
//                        bufferedReader.close();
//                    } catch (IOException ex) {
//                        throw ex;
//                    }
//                }
//            }
//
//            body = stringBuilder.toString();
