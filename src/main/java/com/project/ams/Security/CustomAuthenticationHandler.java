//package com.project.ams.Security;
//
//import java.io.IOException;
//import java.util.Set;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Configuration
//public class CustomAuthenticationHandler implements AuthenticationSuccessHandler {
//
//	private static final String ADMIN_ROLE = "ADMIN";
//	private static final String ADMIN_REDIRECT_URL = "/";
//	private static final String NON_ADMIN_REDIRECT_URL = "/user";
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
//			Authentication authentication) throws IOException {
//
//		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
//		System.out.println(roles);
//
//		if (roles.contains(ADMIN_ROLE)) {
//			System.out.println("Redirecting admin user to admin urls.....");
//			httpServletResponse.sendRedirect(ADMIN_REDIRECT_URL);
//		} else {
//			System.out.println("Redirecting operator user to user urls.....");
//			httpServletResponse.sendRedirect(NON_ADMIN_REDIRECT_URL);
//		}
//	}
//}
//
////import java.io.IOException;
////import java.util.ArrayList;
////import java.util.Collection;
////import java.util.List;
////
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.web.DefaultRedirectStrategy;
////import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
////
////import com.vaadin.flow.spring.security.VaadinSavedRequestAwareAuthenticationSuccessHandler.RedirectStrategy;
////
////import jakarta.servlet.http.HttpServletRequest;
////import jakarta.servlet.http.HttpServletResponse;
////
////@Configuration
////public class CustomAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {
////	
////	@Override
////	public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
////		
////		String targetUrl = determineTargetUrl(authentication);
////		
////		if(response.isCommitted()) {
////			return;
////		}
////		
////		DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
////		redirectStrategy.sendRedirect(request, response, targetUrl);
////	}
////	
////	protected String determineTargetUrl(Authentication authentication) {
////		
////		String url = "/login?error=true";
////		
////		//fetch the roles from Authentication object
////		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
////		List<String> roles = new ArrayList<String>();
////		
////		for(GrantedAuthority a : authorities) {
////			roles.add(a.getAuthority());
////			System.out.println(roles);
////		}
////		
////		
////		//check user role and redirect url
////		if(roles.contains("ADMIN")) {
////			url="/source";
////		}
////		else if(roles.contains("OPERATOR")) {
////			url="/user";
////		}
////		
////		return url;
////	}
////}
