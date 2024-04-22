package com.project.ams.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.project.ams.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityAppConfig extends VaadinWebSecurity{

//	private final UserDetailsService userDetailsService;
//
//    public SecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
	@Autowired
	HttpSecurity httpSecurity;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails adminUser = User.withUsername("smit")
                                    .password(passwordEncoder().encode("admin123"))
                                    .roles("ADMIN")
                                    .build();
        return new InMemoryUserDetailsManager(adminUser);
    }
    
	@Bean
    SecurityFilterChain HttpSec() throws Exception {
    	
//    	httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
    	httpSecurity.authorizeHttpRequests(auth -> {
//    		auth.requestMatchers("/login", "/logout").permitAll();
    		auth.requestMatchers(new AntPathRequestMatcher("/public/**")).permitAll();
//    		customizer.anyRequest().hasRole("ADMIN");
    	});
//    	httpSecurity.formLogin().loginPage("/login");
    	
    	httpSecurity.formLogin(login ->{
    		login.loginPage("/login");
    		login.permitAll(true);
    		login.defaultSuccessUrl("/");
    	});
    	httpSecurity.httpBasic(Customizer.withDefaults());
    	super.configure(httpSecurity);
    	setLoginView(httpSecurity, LoginView.class);
		return httpSecurity.build();	
    }
    

}
