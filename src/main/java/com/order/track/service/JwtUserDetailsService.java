package com.order.track.service;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.order.track.configuration.GlobalConfiguration;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private GlobalConfiguration globalConfiguration;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	// $2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6 = password

	// Here user details with encrypted password is returned and password is matched
	// with input

	// Return user with encrypted password from database

	final Map<String, String> user = globalConfiguration.fetchUserDetails(username);

	if (MapUtils.isNotEmpty(user)) {

	    return new User(username, user.get("Password"),
		    Arrays.asList(new SimpleGrantedAuthority(user.get("Role"))));

	} else {
	    throw new UsernameNotFoundException("User not found with username: " + username);
	}

	/*
	 * if ("Fulfiller".equals(username)) { return new User("Fulfiller",
	 * "$2a$10$UCTSges79ri6QZ5t7MHRQOuQFZLeTBxyVpi/uC/kVtWodC7H33Do6",
	 * Arrays.asList(new SimpleGrantedAuthority("Fufiller"))); } else { throw new
	 * UsernameNotFoundException("User not found with username: " + username); }
	 */

    }
}