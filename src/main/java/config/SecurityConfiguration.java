/*
package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
private static Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);
	@Override
	protected  void configure(HttpSecurity http) throws Exception {
		logger.info("hyhy");
		http.addFilter(new AuthenticationProcessingFilter()).authorizeRequests().antMatchers("/**").permitAll().anyRequest().permitAll();
		logger.info("hqhq");
	}
}
*/
