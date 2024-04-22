package com.project.ams.views;

import org.springframework.web.bind.annotation.CrossOrigin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login | AMS")
@Route("login")
@RouteAlias("logout")
@CrossOrigin(origins = "*")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver{

	private LoginForm login = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        getStyle().setBackgroundColor("#202024");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");
        login.getStyle().setBorder("3px");
        login.getStyle().setBorderRadius("3px");
        login.setForgotPasswordButtonVisible(false);

        H1 header = new H1("AMS");
        header.getStyle().setColor("#fff");
        
        add(header, login);
    }

		@Override
		public void beforeEnter(BeforeEnterEvent event) {
			 if(event.getLocation()
			            .getQueryParameters()
			            .getParameters()
			            .containsKey("error")) {
			            login.setError(true);
			        }
			
		}
}
