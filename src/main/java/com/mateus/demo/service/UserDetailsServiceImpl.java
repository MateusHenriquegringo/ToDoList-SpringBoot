package com.mateus.demo.service;

import com.mateus.demo.model.User;
import com.mateus.demo.repository.UserRepository;
import com.mateus.demo.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;


	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (Objects.isNull(user)){
			throw new UsernameNotFoundException("user not found");
		}
		return new UserDetails(user.getId(), user.getUsername(), user.getPassword(), user.getProfiles());
	}
}
