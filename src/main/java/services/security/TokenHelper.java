/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services.security;

import entities.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.WebUtils;

/**
 *
 * @author dgdbp
 */
@Service
public class TokenHelper {

    @Value("${jwt.secret}")
    private String secretKey;

//    @Value("${jwt.expire:3600000}")
    private long validityInMilliseconds = 1000*60*60*2; // 2h

    @Autowired
    private PrincipalService principalService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username) {

        //The JWT signature algorithm we will be using to sign the token    
        Claims claims = Jwts.claims().setSubject(username);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        System.out.println(username + "|" + now + "|" + validity);
        //Builds the JWT and serializes it to a compact, URL-safe string
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)//
                .signWith(SignatureAlgorithm.HS256, secretKey)//
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = principalService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(e);
        }
        return null;
    }

    public String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
        String bearerToken = null;
        try{
            bearerToken = WebUtils.getCookie(request, "token").getValue();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        System.out.println("bearerToken: " + bearerToken);
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
        if (bearerToken != null) {
            return bearerToken;
        }
        return null;
    }

    public boolean validateToken(String token, UserDetails principal) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            System.out.println("ID: " + claims.getId());
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("Issuer: " + claims.getIssuer());
            System.out.println("Expiration: " + claims.getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(e);
        }

        return false;
    }
}
