package com.mateus.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.demo.exceptions.GlobalExceptionHandler;
import com.mateus.demo.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

//login
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtil jwtUtil;


	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new GlobalExceptionHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		User userCredentials = null;
		try {
			userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword());

			return this.authenticationManager.authenticate(authToken);

	}

	protected void successfulAuthentication(HttpServletRequest request,
	                                       HttpServletResponse response,
	                                       FilterChain filterChain,
	                                       Authentication authentication)
			throws IOException, ServletException {


		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		String token = this.jwtUtil.generateToken(username);

		response.addHeader("Authorization", "Bearer" + token);
		response.addHeader("access-control-expose-headers", "Authorization");

	}
}
