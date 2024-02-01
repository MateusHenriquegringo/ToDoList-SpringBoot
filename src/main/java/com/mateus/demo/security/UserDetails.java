package com.mateus.demo.security;

import com.mateus.demo.model.enums.ProfileEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

	private Long id;
	private String username;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public UserDetails(Long id, String username, String password, Set<ProfileEnum> profileEnum) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.authorities = profileEnum.stream().map(x -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toList());
	}

	public boolean hasRole(ProfileEnum profileEnum){
		return getAuthorities().contains(new SimpleGrantedAuthority(profileEnum.getDescription()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
