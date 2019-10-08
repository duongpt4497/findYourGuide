///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package entity;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTCreationException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//
//import entity.TokenProperties;
//import entity.User;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//
///**
// *
// * @author dgdbp
// */
//@Service
//public class TokenService {
//
//    //private final Logger logger = LoggerFactory.getLogger(getClass());
//    private String issuer;
//    private Algorithm algo;
//    private JWTVerifier verifier;
//    private TokenProperties token;
//
//    @Autowired
//    public TokenService(TokenProperties token, @Value("${spring.application.name}") String issuer) {
//        this.token = token;
//        this.issuer = issuer;
//        this.algo = Algorithm.HMAC256(token.getSecret());
//        this.verifier = JWT.require(algo).acceptExpiresAt(0).build();
//    }
//
//    public String genToken(User user) {
//        LocalDateTime now = LocalDateTime.now();
//        try {
//            return JWT.create()
//                    .withIssuer(issuer)
//                    .withIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
//                    .withExpiresAt(Date.from(now.plusSeconds(token.getMaxAge()).atZone(ZoneId.systemDefault()).toInstant()))
//                    .withClaim("role", user.getRole())
//                    .withClaim("usr", user.getUsername())
//                    .sign(algo);
//        } catch (JWTCreationException e) {
//
//            throw new JWTCreationException("-----error on create token-------", e);
//        }
//
//    }
//
//    public DecodedJWT decode(String token) {
//        return this.verifier.verify(token);
//    }
//}
