package com.mateus.demo.config;


import com.mateus.demo.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = false)
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] PUBLIC_MATCHERS = {
			"/"
	};
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/user",
			"/login"
	};

	@Autowired
	private JWTUtil jwtUtil;

	private AuthenticationManager authenticationManager;
	@Autowired
	private UserDetailsService userDetailsService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.userDetailsService(this.userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder());
		this.authenticationManager = authenticationManagerBuilder.build();

		http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> {
					authorize
							.requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
							.requestMatchers(PUBLIC_MATCHERS).permitAll()
							.anyRequest().authenticated();
				})
				.sessionManagement(s ->
						s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	;

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("*/**", configuration);

		return source;
	}


	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
