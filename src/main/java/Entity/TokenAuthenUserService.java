///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package entity;
//
//import com.auth0.jwt.exceptions.InvalidClaimException;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
//import org.springframework.stereotype.Service;
//
///**
// *
// * @author dgdbp
// */
//@Service
//public class TokenAuthenUserService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>{
//    private TokenService tokenServe;
//    @Autowired
//    public TokenAuthenUserService(TokenService token) {
//        this.tokenServe = token;
//    }
//    
//    @Override
//    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken t) throws UsernameNotFoundException {
//        if (t.getPrincipal() != null && t.getPrincipal() instanceof String && t.getCredentials() instanceof String) {
//            DecodedJWT token;
//            try {
//                token = tokenServe.decode((String) t.getPrincipal());
//            } catch (InvalidClaimException ex) {
//                throw new UsernameNotFoundException("Token has been expired", ex);
//            }
////            return new User(token.getSubject(), token.getClaim("usr").asString(), (String) t.getCredentials(), token.getToken(), true, token.getClaim("role"));
//            return null;
//        } else {
//            throw new UsernameNotFoundException("Could not retrieve user details for '" + t.getPrincipal() + "'");
//        }
//    }
//    
//}
