package com.security.jwt.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DemoController {
	@GetMapping("/democontroller")
	public ResponseEntity<String> Hello(){
		
		return ResponseEntity.ok("Hello Secured Network");
	}
	
	
	
	

}
