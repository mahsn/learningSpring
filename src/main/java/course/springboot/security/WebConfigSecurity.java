package course.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementationUsersDetailsService implementationUsersDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
			
		http.csrf()
		.disable() // Disable default configurations of memory 
		.authorizeRequests() // permit restrict users
		.antMatchers(HttpMethod.GET, "/").permitAll() // any users can access to page 
		.antMatchers(HttpMethod.GET, "/students").hasAnyRole("ADMIN")
		.anyRequest().authenticated()
		.and().formLogin().permitAll() // permit any users
		.loginPage("/login")
		.defaultSuccessUrl("/students")
		.failureUrl("/login?error=true") 
		.and().logout().logoutSuccessUrl("/login") // maps URL of logout and invalid users authenticated
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}
	
	@Override // create authentication of user with database and memory
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.getImplementationUsersDetailsService())
		.passwordEncoder(new BCryptPasswordEncoder());
		
		/*
			 auth.inMemoryAuthentication()
			.passwordEncoder(NoOpPasswordEncoder.getInstance())
			.withUser("mhsn")
			.password("123")
			.roles("ADMIN");
		*/
	}
	
	@Override // ignore url specification
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/materialize/**");
	}
	
	public ImplementationUsersDetailsService getImplementationUsersDetailsService() {
		return implementationUsersDetailsService;
	}
}
