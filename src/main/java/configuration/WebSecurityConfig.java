/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import services.*;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import entity.Principal;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author dgdbp
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Value("%{security.signing_key}")
//    private String signingKey;
//    @Value("%{security.encoding_strength}")
//    private String encodingStrength;
//    @Value("%{security.realm}")
//    private String securityRealm;
    private PrincipalService userDetail;
    @Autowired
    private AuthProvider authProvide;
    @Autowired
    private PrincipalService principalService;
    @Autowired
    TokenHelper tokenHelper;
    @Autowired
    private Principal principal;
    @Autowired
    private UnauthenEndpoint unauthenEndpoint;

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

//    @Bean
//    public PasswordEncoder getEncoder() {
//        return new BCryptPasswordEncoder(10);
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("to http configure");

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //                .antMatchers("/**/**").permitAll()
                .anyRequest().authenticated().and()
                .addFilter(getAuthenticationFilter())
                .addFilterBefore(new TokenAuthenFilter(tokenHelper, principalService), BasicAuthenticationFilter.class);

        http.csrf().disable();
//                .exceptionHandling()
//                .authenticationEntryPoint(unauthenEndpoint)
//                .and()
//                .formLogin()
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
//                .successHandler(new SuccessHandler());

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
