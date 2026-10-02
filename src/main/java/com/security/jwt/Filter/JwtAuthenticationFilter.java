package com.security.jwt.Filter;

import java.io.IOException;
import java.util.Date;

import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.security.jwt.JwtService;


import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private  JwtService jwtService;
	@Autowired
	private UserDetailsService userDetailService;
	

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
		
		final String authHeader=request.getHeader("Authentication");
		final String jwt;
		final String UserEmail;
		UserDetails userDetails = null;
		
		
		if(authHeader==null || !authHeader.startsWith("Bearer")) {
			
			filterChain.doFilter(request, response);
			return;
			
		}
		jwt=authHeader.substring(7);
		UserEmail=jwtService.extractUsername(jwt);
		if(UserEmail !=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			
			 userDetails = this.userDetailService.loadUserByUsername(UserEmail);	
		}
		//..............................................................................
		
		if(jwtService.isValid(UserEmail,userDetails)) {
			
			UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(userDetailService, UserEmail, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		filterChain.doFilter(request, response);
		
		
		
	}

}
