package com.security.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.security.jwt.JwtService;
import com.security.jwt.UserRepository;
import com.security.jwt.User.User;

import com.security.jwt.configuration.Role;
import com.security.jwt.model.AuthenticationRequest;
import com.security.jwt.model.AuthenticationResponse;
import com.security.jwt.model.RegisterRequest;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.var;
@AllArgsConstructor
@org.springframework.stereotype.Service

public class Service {

	
	@Autowired
	JwtService jwtService;
    @Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	UserRepository repo;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {

//		User user = User.builder()
//				.firstname(request.getFirstName())
//                .lastname(request.getLastName())
//                .UserEmail(request.getEmail())
//                .UserPassword(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();

		var custom = User.builder().firstName(request.getFirstname()).lastName(request.getLastname())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).role(Role.USER)
				.build();

		repo.save(custom);

		String jwtToken = jwtService.generateToken(custom);

		return AuthenticationResponse.builder().token(jwtToken).build();

	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		
		var custom=repo.findByEmail(request.getEmail())
				.orElseThrow();
		String jwtToken = jwtService.generateToken(custom);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}

}
