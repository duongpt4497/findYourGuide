///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package winter.findGuider.web.api;
//
//import javax.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
//
///**
// *
// * @author dgdbp
// */
//public class TokenAuthenFilter extends AbstractPreAuthenticatedProcessingFilter {
//
//    @Override
//    protected Object getPreAuthenticatedPrincipal(HttpServletRequest hsr) {
//        logger.debug("Retrieving principal from token");
//        return hsr.getHeader("X-Token");
//    }
//
//    @Override
//    protected Object getPreAuthenticatedCredentials(HttpServletRequest hsr) {
//        logger.debug("Retrieving credentials from token");
//        return hsr.getHeader("X-Token");
//    }
//
//    @Override
//    //@Autowired
//    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//        super.setAuthenticationManager(authenticationManager); //To change body of generated methods, choose Tools | Templates.
//    }
//    
//    
//}
