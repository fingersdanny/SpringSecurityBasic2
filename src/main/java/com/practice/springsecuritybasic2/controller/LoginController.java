package com.practice.springsecuritybasic2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.practice.springsecuritybasic2.model.Customer;
import com.practice.springsecuritybasic2.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {

	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
		Customer savedCustomer = null;
		ResponseEntity response = null;
		try {
			String hashPwd = passwordEncoder.encode(customer.getPwd());
			customer.setPwd(hashPwd);
			savedCustomer = customerRepository.save(customer);
			if (savedCustomer.getId() > 0) {
				response = ResponseEntity
					.status(HttpStatus.OK)
					.body("Given user details are successfully registered");
			}
		} catch (Exception e) {
			response = ResponseEntity.status(
				HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An exception occured due to" + e.getMessage());
		}
		return response;
	}
}
