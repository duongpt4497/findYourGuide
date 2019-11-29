/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import entities.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import services.security.*;

import java.util.Arrays;

/**
 * @author dgdbp
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    TokenHelper tokenHelper;
    private PrincipalService userDetail;
    @Autowired
    private AuthenProvider authProvide;
    @Autowired
    private PrincipalService principalService;
    @Autowired
    private Principal principal;


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("to configureGlobal");
        auth.userDetailsService(principalService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("to Simson");
        auth
                .inMemoryAuthentication()
                .withUser("Simson").password("123").roles("GUIDER");
        auth.authenticationProvider(authProvide);
        auth.userDetailsService(principalService);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        System.out.println("to cors");
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTION"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("to http configure");

        http
//                .cors().and()
                .cors().configurationSource(corsConfigurationSource()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
//                .antMatchers("/location/**").hasAuthority("TRAVELER")
                .antMatchers("/location/**").permitAll()
                .antMatchers("/**/*.jpg").permitAll()
                .antMatchers("/account/**").permitAll()
                .antMatchers("/category/**").permitAll()
                .antMatchers("/guiderpost/**").permitAll()
                .antMatchers("/Feedback/**").permitAll()
                .antMatchers("/Guider/**").permitAll()
                .antMatchers("/guider/**").permitAll()
                .antMatchers("/Order/**").permitAll()
                .antMatchers("/Payment/**").permitAll()
                .antMatchers("/plan/**").permitAll()
                .antMatchers("/review/**").permitAll()
                .antMatchers("/Traveler/**").permitAll()
                .anyRequest().authenticated().and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/account/logout")).and()
                .addFilter(getAuthenticationFilter())
                .addFilterBefore(new TokenAuthenFilter(tokenHelper, principalService), BasicAuthenticationFilter.class)
        ;

        http.csrf().disable();


    }

    //  Patterns to ignore from JWT security check
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                HttpMethod.POST,
                "/account/**"
        );
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/assets/**",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/account/**"
        );
    }

    @Bean
    public AuthenticationFilter getAuthenticationFilter() {
        final AuthenticationFilter filter = new AuthenticationFilter(authProvide, tokenHelper);
        filter.setFilterProcessesUrl("/account/login");
        return filter;
    }

}
