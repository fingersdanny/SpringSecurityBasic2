package com.practice.springsecuritybasic2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.springsecuritybasic2.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	List<Customer> findByEmail(String email);
}