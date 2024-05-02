//package com.project.ams.Security;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import com.project.ams.views.LoginView;
//import com.vaadin.flow.spring.security.VaadinWebSecurity;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityAppConfig extends VaadinWebSecurity {
//
////	private final UserDetailsService userDetailsService;
////
////    public SecurityConfig(UserDetailsService userDetailsService) {
////        this.userDetailsService = userDetailsService;
////    }
//	
//	@Autowired
//	DataSource dataSource;
//	
////	@Autowired
////	private CustomAuthenticationHandler customAuthenticationHandler;
//	
//	@Autowired
//	HttpSecurity httpSecurity;
//
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
////	@Bean
////	JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource2) {
////		UserDetails adminUser = User.withUsername("smit").password(passwordEncoder().encode("admin123")).roles("ADMIN")
////				.build();
////
////		UserDetails Operator = User.withUsername("user").password(passwordEncoder().encode("user123")).roles("OPERATOR")
////				.build();
////
////		return jdbcUserDetailsManager(dataSource);
////	}
//
//	@Bean
//	SecurityFilterChain HttpSec() throws Exception {
//
//        httpSecurity.csrf(csrf -> csrf.disable());
//
////		httpSecurity.authorizeHttpRequests(auth -> {
////			auth.anyRequest().authenticated();
////		});
//        
////        httpSecurity.authorizeHttpRequests(auth -> {
////        	auth.requestMatchers("/source","/asset","/tagMapping","dashboard","/rtuconfig","/Adduser").hasAnyRole("ADMIN");
////        	auth.requestMatchers("/user").hasAnyRole("OPERATOR");
////        });
//
//		httpSecurity.formLogin(login -> {
//			login.loginPage("/login");
//			login.permitAll(true);
////    		login.defaultSuccessUrl("/",false);
////    		login.successHandler(customAuthenticationHandler);
//		});
//
//		httpSecurity.httpBasic(Customizer.withDefaults());
//
//		super.configure(httpSecurity);
//		setLoginView(httpSecurity, LoginView.class);
//		return httpSecurity.build();
//	}
//
//}
