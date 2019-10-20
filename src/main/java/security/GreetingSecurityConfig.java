///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package security;
//
//
//import entity.UserDetailService;
//import javax.sql.DataSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//
///**
// *
// * @author dgdbp
// */
////@Configuration
////@EnableWebSecurity
//public class GreetingSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Value("%{security.signing_key}")
//    private String signingKey;
//    @Value("%{security.encoding_strength}")
//    private String encodingStrength;
//    @Value("%{security.realm}")
//    private String securityRealm;
//    private UserDetailService userDetail;
////    @Autowired
////    DataSource data;
////    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth
////                .inMemoryAuthentication()
////                .withUser("Buzz").password("bee").authorities("TheFirst")
////                .and()
////                .withUser("Ops").password("king").authorities("Meow");
//        auth.userDetailsService(userDetail);
//    }
//    
//    @Bean
//    public PasswordEncoder getEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http
////                .authorizeRequests()
////                .antMatchers(HttpMethod.GET, "/find/all").hasRole("TheFirst")
////                .antMatchers("/").permitAll()
////                .and()
////                .csrf().disable();
//        http
//                .antMatcher("/token")
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic()
//                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//                
////        http.sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
////                .httpBasic()
////                .realmName(securityRealm)
////                .and()
////                .csrf().disable();
//    }
//    
//   
//   
//
//}
