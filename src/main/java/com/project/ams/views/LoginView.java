package com.project.ams.views;

import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login")
@Route("/login")
@AnonymousAllowed
public class LoginView extends VerticalLayout{

	    public LoginView() {
	        setPadding(true);
	        setAlignItems(Alignment.CENTER);
//	        setJustifyContentMode(JustifyContentMode.CENTER);

//	        LoginForm loginForm = new LoginForm();
//	        loginForm.setForgotPasswordButtonVisible(true);
//	        loginForm.addLoginListener(e -> handleLogin(e.getUsername(), e.getPassword()));

//	        VerticalLayout layout = new VerticalLayout();
//	        layout.setPadding(false);
//	        layout.setSpacing(false);
//	        layout.getStyle().set("background-color", "blue");
//	        layout.getStyle().set("color", "white");
//	        layout.getStyle().set("padding", "20px");
	        LoginOverlay overlay = new LoginOverlay();
	        overlay.setTitle("AMS");
	        overlay.setDescription(null);
//	        overlay.addForgotPasswordListener(e -> UI.getCurrent().navigate(ForgetPassword.class) );
//	        overlay.setError(true);
//	        overlay.setForgotPasswordButtonVisible(false);
	        overlay.setOpened(true);
	        overlay.setForgotPasswordButtonVisible(false);
	        overlay.setAction("/");
	        overlay.getElement().setAttribute("no-autofocus", "");
	        add(overlay);
	        overlay.setError(true);
	    }
}
