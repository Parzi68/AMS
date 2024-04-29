package com.project.ams.vaadin;

import com.project.ams.views.UserLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style.JustifyContent;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;

@Route(value ="/user", layout = UserLayout.class)
@RolesAllowed("OPERATOR")
public class UserPage extends VerticalLayout{

	Button dashboard = new Button("View Dashboard");
	
	@PostConstruct
	public void init(){
		setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		setPadding(true);
		
		
		H1 header = new H1("Welcome to the User Dashboard!");
		
		H4 text = new H4("Explore your assets and monitor their performance with ease. Click the button below to access the dashboard and start tracking.");
		
		dashboard.addClickListener(e -> {
			UI.getCurrent().getPage().open("http://localhost:3000/public-dashboards/9291274f7efb4a4aa775f5fee9114bd0");
		});
		dashboard.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		dashboard.setAutofocus(true);
		
		add(header,text,dashboard);
		}
}
