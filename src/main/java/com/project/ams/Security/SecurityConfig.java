//package com.project.ams.Security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@Configuration
//public class SecurityConfig{
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception  {
//		http
//		.authorizeHttpRequests((authorize) ->
//        authorize.anyRequest().authenticated()
//).formLogin(
//        form -> form
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/")
//                .permitAll()
//)
//		.logout(
//        logout -> logout
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .permitAll()
//);
//return http.build();
//		
//	}
//    
////    @Bean
////    public UserDetailsService userDetailsService(){
////
////        UserDetails admin = User.builder()
////                .username("admin")
////                .password(passwordEncoder().encode("admin"))
////                .roles("ADMIN")
////                .build();
////
////        return new InMemoryUserDetailsManager(admin);
////    }
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//}
