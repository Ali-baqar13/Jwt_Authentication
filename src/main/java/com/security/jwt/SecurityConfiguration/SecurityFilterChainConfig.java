package com.security.jwt.SecurityConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityFilterChainConfig {
	private final AuthenticationProvider authenticationProvider ;
	private final Filter jwtAuthenticationFilter;
	
	@Bean
	protected SecurityFilterChain securityFilterChaining(HttpSecurity http) throws Exception{

		System.out.println("hello");
		
		return http
				.csrf(cs->cs.disable())
				.authorizeHttpRequests(authz->authz.requestMatchers("/api/v1/auth/**").permitAll()
				.anyRequest().authenticated())
				.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
				.build();
				
				
	}
	
	 

	  

}

	
