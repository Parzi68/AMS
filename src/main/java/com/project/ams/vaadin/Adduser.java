package com.project.ams.vaadin;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.spring.Repository.UserRepository;
import com.project.ams.spring.model.User;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Add User")
@Route(value="/Adduser", layout=MainLayout.class)
@RolesAllowed("ADMIN")
public class Adduser extends VerticalLayout
{
	@Autowired
	private UserRepository userRepository;
	User user = new User();
	EmailField email = new EmailField("Enter e-mail");
	TextField username = new TextField("Enter Username");
	PasswordField pass = new PasswordField("Enter Password");
	Button add = new Button("Add User");
	Button cancel = new Button("Cancel");
	
	@PostConstruct
	public void init() {
		
		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Add User  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");
		
		add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		add.getElement().getStyle().set("margin-top", "15px");
		add.addClickListener(e -> {
			AddUser();
		});
		
		cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancel.getElement().getStyle().set("margin-top", "15px");
		
		HorizontalLayout buttonLayout = new HorizontalLayout(add,cancel);
		FormLayout form = new FormLayout();
		form.getStyle().setPadding("10px");
//		form.getStyle().setAlignItems(AlignItems.CENTER);
		form.add(email,username,pass,buttonLayout);
		form.setResponsiveSteps(
		        // Use one column by default
		        new ResponsiveStep("0", 1)
		        );
		add(navbar, hr,form);
	}

	public void AddUser() {
		user.setEmail(email.getValue());
		user.setUsername(username.getValue());
		user.setPassword(pass.getValue());
		userRepository.save(user);
		System.out.println("-------- Adding User -------");
	}

}
