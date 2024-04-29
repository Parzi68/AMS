package com.project.ams.Security;

import java.io.IOException;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class CustomAuthenticationHandler implements AuthenticationSuccessHandler {

	private static final String ADMIN_ROLE = "ROLE_ADMIN";
	private static final String ADMIN_REDIRECT_URL = "/";
	private static final String NON_ADMIN_REDIRECT_URL = "/user";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException {

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

		if (roles.contains(ADMIN_ROLE)) {
			httpServletResponse.sendRedirect(ADMIN_REDIRECT_URL);
		} else {
			httpServletResponse.sendRedirect(NON_ADMIN_REDIRECT_URL);
		}
	}
}
