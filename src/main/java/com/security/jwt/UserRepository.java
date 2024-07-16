package com.security.jwt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.jwt.User.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional <User> findByEmail(String Email);

}
