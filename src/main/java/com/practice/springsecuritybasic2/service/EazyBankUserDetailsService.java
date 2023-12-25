package com.practice.springsecuritybasic2.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.practice.springsecuritybasic2.model.Customer;
import com.practice.springsecuritybasic2.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

/**
 * DaoAuthenticationProvider가 현재 상태에서 구현된 구현체를 찾아서 사용함 이 경우 (JdbcUserDetailsManager와
 * EazyBankUserDetailsService) 하지만 구현체가 여러 개라면 어떤 구현체를 쓸지 모른다.
 *  -> 따라서 다음과 같은 에러 발생
 *  No AuthenticationProvider found for org.springframework.security.authentication.UsernamePasswordAuthenticationToken
 */

@Service
@RequiredArgsConstructor
public class EazyBankUserDetailsService implements UserDetailsService {
	private final CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String userName, password = null;
		List<GrantedAuthority> authorities = null;
		List<Customer> customer = customerRepository.findByEmail(username);

		if (customer.size() == 0) {
			throw new UsernameNotFoundException("User details not found for the user:" + username);
		} else {
			userName = customer.get(0).getEmail();
			password = customer.get(0).getPwd();
			authorities = new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole()));
		}
		return new User(username, password, authorities);
	}
}
