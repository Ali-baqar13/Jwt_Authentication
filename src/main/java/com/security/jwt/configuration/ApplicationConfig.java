package com.security.jwt.configuration;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.security.jwt.UserRepository;
import com.security.jwt.User.User;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class ApplicationConfig  {
    @Autowired
    private final UserRepository userRepo;

    @Bean
    public UserDetailsService userDetailservice() {
        
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                User user = userRepo.findByEmail(username)
//                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//
//                // Convert your User entity to Spring Security UserDetails
//                return new org.springframework.security.core.userdetails.User(
//                        user.getEmail(),
//                        user.getPassword(),
//                        true, true, true, true,
//                        new ArrayList<>() // Here you should convert roles/authorities if you have them
//                );
//            }
 //       	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                User user = userRepo.findByEmail(username)
//                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
//				return user;}
        	
        	
        	
        	

         return username->userRepo.findByEmail(username)
        		 .orElseThrow(()->new UsernameNotFoundException("user not found"));
        		 
         
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    
   

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailservice());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
   
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
   
    
}
