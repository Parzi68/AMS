package com.project.ams.Security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.project.ams.spring.model.Userdetails;

@Component
public class CustomUserDetailsManager implements UserDetailsService{
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		String sql = "SELECT * FROM userdetails where email = ?";
		
		List<Userdetails> userdetailslist = jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Userdetails.class),username);
		
		if(userdetailslist.size() == 0) {
			throw new RuntimeException("User not found");
		}
		
		UserDetails userdetails = User.withUsername(userdetailslist.get(0).getEmail())
									.password(userdetailslist.get(0).getPassword())
									.roles(userdetailslist.get(0).getRole())
									.build();
		
		return userdetails;
	}
}
	