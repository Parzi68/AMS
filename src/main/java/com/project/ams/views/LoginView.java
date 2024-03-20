package com.project.ams.views;

import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("/login")
public class LoginView extends VerticalLayout{

	    public LoginView() {
//	        setSizeFull();
//	        setAlignItems(Alignment.CENTER);
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
	        overlay.getElement().setAttribute("no-autofocus", "");
	        add(overlay);	        
	    }

	    private void handleLogin(String username, String password) {
	        // Perform authentication and email verification logic here
	        // For example, you could send a verification email to the user's email address
	        // and prompt them to click a confirmation link before allowing them to log in
	    }
}
