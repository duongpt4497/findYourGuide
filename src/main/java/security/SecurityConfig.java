///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package security;
//
//import entity.TokenAuthenUserService;
//import entity.UserDetailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
//import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
//import winter.findGuider.web.api.TokenAuthenFilter;
//
///**
// *
// * @author dgdbp
// */
////@Configuration
////@EnableWebSecurity
////@EnableGlobalMethodSecurity(prePostEnabled=true)
//public class SecurityConfig {
//
//    private UserDetailService userdetail;
//    private PasswordEncoder encoder;
//
//    //@Autowired
//    public SecurityConfig(UserDetailService userdetail, PasswordEncoder encoder) {
//        this.userdetail = userdetail;
//        this.encoder = encoder;
//    }
//
//    public class BasicAuthConfig extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .antMatcher("/token")
//                    .authorizeRequests()
//                    .anyRequest()
//                    .authenticated()
//                    .and()
//                    .httpBasic()
//                    .and()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        }
//
//        @Override
//        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userdetail).passwordEncoder(encoder);
//        }
//
//    }
//
//    public class TokenAuthConfig extends WebSecurityConfigurerAdapter {
//
//        private TokenAuthenUserService service;
//
//        //@Autowired
//        public TokenAuthConfig(TokenAuthenUserService service) {
//            this.service = service;
//        }
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .antMatcher("/find/*")
//                    .authorizeRequests()
//                    .anyRequest().authenticated()
//                    .and()
//                    .addFilterBefore(authFilter(), RequestHeaderAuthenticationFilter.class)
//                    .authenticationProvider(preAuthProvider())
//                    .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                    .csrf().disable();
//
//        }
//
//        //@Bean
//        public TokenAuthenFilter authFilter() {
//            return new TokenAuthenFilter();
//        }
//
//        //@Bean
//        public AuthenticationProvider preAuthProvider() {
//            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
//            provider.setPreAuthenticatedUserDetailsService(service);
//            return provider;
//        }
//
//    }
//
//}
