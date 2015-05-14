package com.goodvideotutorials.spring.config;

import javax.annotation.Resource;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Resource
	private UserDetailsService userService;
	
	@Value("${rememberMe.privateKey}")
	private String rememberMeKey;
	
	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		logger.info("Creating password encoder bean");
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RememberMeServices rememberMeServices(){
		TokenBasedRememberMeServices rememberMeServices = new 
				TokenBasedRememberMeServices(rememberMeKey, userService);
		return rememberMeServices;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.authorizeRequests().antMatchers("/",
				"/home",
				"/error",
				"/signup",
				"/forgot-password",
				"/reset-password",
				"/public/**").permitAll().anyRequest().authenticated();
		
		http
			.formLogin()
			.loginPage("/login")
			.permitAll().and()
			.rememberMe().key(rememberMeKey).rememberMeServices(rememberMeServices()).and()
			.logout().permitAll();
				
	}
	
	@Autowired
	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
		authManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder()  );
	}
	
}