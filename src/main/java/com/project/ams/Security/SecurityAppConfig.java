//package com.project.ams.Security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityAppConfig {
//
////	private final UserDetailsService userDetailsService;
////
////    public SecurityConfig(UserDetailsService userDetailsService) {
////        this.userDetailsService = userDetailsService;
////    }
//	@Autowired
//	HttpSecurity httpSecurity;
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//    @Bean
//    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        UserDetails adminUser = User.withUsername("smit")
//                                    .password(passwordEncoder().encode("admin123"))
//                                    .roles("ADMIN")
//                                    .build();
//        return new InMemoryUserDetailsManager(adminUser);
//    }
//    
//	@Bean
//    SecurityFilterChain HttpSec() throws Exception {
//    	
////    	httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
//    	httpSecurity.authorizeHttpRequests(customizer -> {
//    		customizer.anyRequest().permitAll();
////    		customizer.anyRequest().hasRole("ADMIN");
//    	});
////    	httpSecurity.formLogin().loginPage("/login");
//    	
//    	httpSecurity.formLogin(customizer -> {
//    		customizer.loginPage("/login");
//    		customizer.isCustomLoginPage();	
//    		customizer.loginProcessingUrl("/login");
////    		UI.getCurrent().getPage().executeJs("window.open('http://localhost:8081/');");
//    		customizer.defaultSuccessUrl("/");
//    	});
////    	httpSecurity.httpBasic();
//    	httpSecurity.httpBasic(Customizer.withDefaults());
//    	
//		return httpSecurity.build();	
//    }
//    
//
//}
