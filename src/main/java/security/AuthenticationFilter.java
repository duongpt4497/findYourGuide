/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import entities.Account;
import entities.AuthenticationImp;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.Cookie;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dgdbp
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());
    private AuthenProvider authenticationManager;
    private TokenHelper TokenHelper;

    public AuthenticationFilter(AuthenProvider authenticationManager, TokenHelper tokenService) {
        this.authenticationManager = authenticationManager;
        this.TokenHelper = tokenService;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
        try {
            
            Account acc = new ObjectMapper().readValue(request.getInputStream(), Account.class);
            return authenticationManager.authenticate(new AuthenticationImp(acc));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        
     
            String token = TokenHelper.createToken(authResult.getPrincipal().toString());
            response.addHeader("Access-Control-Allow-Origin", "http://23.94.188.212:3000");
            response.addHeader("Access-Control-Allow-Credentials", "true");
            response.setStatus(HttpServletResponse.SC_OK);
            Cookie sidCookie = new Cookie("token",token);
            sidCookie.setPath("/");
//            sidCookie.setSecure(true);
            sidCookie.setHttpOnly(true);
            sidCookie.setDomain("23.94.188.212");
            response.addCookie(sidCookie);
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
