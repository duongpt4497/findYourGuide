package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationProcessingFilter extends AbstractPreAuthenticatedProcessingFilter {
private static Logger logger = LoggerFactory.getLogger(AuthenticationProcessingFilter.class);
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
logger.info("hihi");
		ObjectMapper mapper = new ObjectMapper();

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

		String user = httpRequest.getHeader("user");
		String password = httpRequest.getHeader("password");


		if(user != null && password != null) {
			List<GrantedAuthority> authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, user, authorities);
			SecurityContextHolder.getContext().setAuthentication(auth);

		}
		logger.info("haha");
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
		return null;
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
		return null;
	}
}
