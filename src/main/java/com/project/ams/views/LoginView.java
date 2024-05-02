package com.project.ams.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.ams.SessionController;
import com.project.ams.spring.Repository.UserRepository;
import com.project.ams.spring.model.Userdetails;
import com.project.ams.vaadin.CustomAccessDeniedError;
import com.project.ams.vaadin.Index;
import com.project.ams.vaadin.UserPage;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;
import com.vaadin.flow.server.VaadinService;

@SuppressWarnings("serial")
@Route(LoginView.NAME)
@RouteAlias("")
@PageTitle("Login")
public class LoginView extends VerticalLayout {

	public static final String NAME = "login";

	@Autowired
	UserRepository users;

	Binder<Userdetails> binder = new Binder<>();

	private final LoginForm login = new LoginForm();
	private Image companyLogo;

	LoginOverlay overlay;
	Button btnSignin = new Button("Create New User");

	Button btnLogout = new Button("Logout");

	Boolean isLogedIn = false;

	SessionController session = new SessionController();

	String sesionId;

	public LoginView() {
		try {
			addClassName("login-view");
			setSizeFull();
			setAlignItems(Alignment.CENTER);
			setJustifyContentMode(JustifyContentMode.CENTER);

			// for background image
			//getElement().getStyle().set("background", "url(../frontend/img/1.png)");

			btnLogout.addClickListener(e -> {

				login.setAction("");

			});
//			companyLogo = new Image();
//			companyLogo.setSrc("images/logos/TP.png");
////			companyLogo.setWidth("30%");
//			companyLogo.setHeight("40%");
			login.setForgotPasswordButtonVisible(false);
	        getStyle().setBackgroundColor("#202024");
	        
	        H1 header = new H1("AMS");
	        header.getStyle().setColor("#fff");
	        
			add(header,login);
			// click on login button
			login.addLoginListener(e -> {

				/*
				 * Get username and password and compar with database it will take username and
				 * pass to udp.getPassword() function, which will get password of that username
				 * If the password and password that is returned from udp.getPassword() , it
				 * will login else show "Something is wrong"
				 * 
				 */

				
				Userdetails udma = users.getDetails(e.getUsername());
				
				BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

				if (udma != null) {
					if (e.getUsername().equals(udma.getUsername()) && bcrypt.matches(e.getPassword(), udma.getPassword())) {

						isLogedIn = session.isLoggedIn(udma.getId());
						String role=users.getrole(e.getUsername());
						if (!isLogedIn) {

							session.setSessionAttributes(udma.getId(), udma.getUsername(), role);
							Notification.show("Loged In");
							sesionId = UI.getCurrent().getSession().getSession().getId();
							
							System.out.println("Welcome"+role);
							if(role.equals("ADMIN"))
							{
								UI.getCurrent().navigate(Index.class);
							}
							else if(role.equals("OPERATOR"))
							{
								UI.getCurrent().navigate(UserPage.class);
							}
							else
							{
								UI.getCurrent().navigate(CustomAccessDeniedError.class);
							}
							
							
						} else {
							System.out.println("Sorry"+role);
							UI.getCurrent().navigate(CustomAccessDeniedError.class);
						}

					} else
						login.setError(true);

				} else {
					login.setError(false);
					Notification notification = new Notification();
					notification.setDuration(2000);
					notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
					notification.setText("User does not exist");
					notification.open();
					login.setError(true);

//					UI.getCurrent().getPage().reload();
				}
			});

			// On click of forgot password link
//			login.addForgotPasswordListener(e -> {
//
//				UI.getCurrent().navigate("forgotPassword/");
//
//			});
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("eeeeeeeeeeeee" + e.toString());
		}

		VaadinService.getCurrent().addSessionDestroyListener(new SessionDestroyListener() {

			@Override
			public void sessionDestroy(SessionDestroyEvent event) {
				// TODO Auto-generated method stub

				if (sesionId != null && session.getUsername() != null) {
					
				}
			}
		});

	}

	public void logout() {
		btnLogout.click();
	}
}