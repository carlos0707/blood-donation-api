package br.com.carlos.blooddonationapi.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	  	@Override
	    protected void configure(HttpSecurity http) throws Exception
	    {	
	        http
	         	.csrf().disable()
	         	.httpBasic()
	         	.and()
				.addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
             	.sessionManagement()
             	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    }

		@Override
		public void configure(WebSecurity web) throws Exception {

			web.ignoring().antMatchers("/docs/*", "/v3/api-docs", "/v3/api-docs/*");
			 web.ignoring()
	            .antMatchers(HttpMethod.OPTIONS);
		}

}

class JwtTokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		try {  
		    
			String bearerToken = request.getHeader("Authorization");						
			
			if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
				
				response.setStatus(401);
				return;
			}

			bearerToken = bearerToken.substring(7);

			Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(Config.getProperty("jwt.secret").getBytes()))
					     .parseClaimsJws(bearerToken);

			filterChain.doFilter(request, response);
			
		} catch (Exception ex) {
			response.setStatus(401);
			ex.printStackTrace();
		}
	}
}
