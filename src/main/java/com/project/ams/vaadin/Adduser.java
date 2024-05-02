package com.project.ams.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.ams.spring.Repository.UserRepository;
import com.project.ams.spring.model.Userdetails;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Add User  |  AMS")
@RolesAllowed("ADMIN")
@Route(value = "/Adduser", layout = MainLayout.class)
public class Adduser extends VerticalLayout implements HasUrlParameter<String> {
	
	public static final String ROUTE_NAME = "Adduser";
	
	@Autowired
	private UserRepository userRepository;
	
//	Userdetails userdetails = new Userdetails();
	
	private Userdetails user = new Userdetails();
	TextField id = new TextField("Id");
	EmailField email = new EmailField("Enter email");
	TextField username = new TextField("Enter Username");
	PasswordField pass = new PasswordField("Enter Password");
	Button add = new Button("Add User");
	Button cancel = new Button("Cancel");
	Grid<Userdetails> users = new Grid<>(Userdetails.class, false);
	ListDataProvider<Userdetails> dataProvider;
	
	long main_id = 0;

	@SuppressWarnings("removal")
//	@PostConstruct
	public void init(String param) {
//		Userdetails userdetails = new Userdetails();
		
//		main_id = Long.parseLong(param);

		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Add User  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");

		if (!param.equals("0")) {
			for (Userdetails t1 : userRepository.user_list(main_id)) {
				username.setValue(t1.getUsername());
				email.setValue(t1.getEmail());
				pass.setValue(t1.getPassword());
			}
		}
		
		id.setReadOnly(true);
		id.setValue(String.valueOf(user.getId()));
		// Fetch the latest ID from the database and increment it by 1
		Long nextId = userRepository.findMaxId();

		// If no records exist, set nextId to 1, otherwise increment the found max ID by
		// 1
		nextId = (nextId == null) ? 1L : nextId + 1;
		id.setValue(String.valueOf(nextId));
		
		
		email.setErrorMessage("Please enter email address");
		email.setRequired(true);
		email.setRequiredIndicatorVisible(true);
		
		username.setErrorMessage("Please fill this field!");
		username.setRequired(true);
		username.setRequiredIndicatorVisible(true);
		
		
		pass.setErrorMessage("Please fill this field!");
		pass.setRequired(true);
		pass.setRequiredIndicatorVisible(true);
		
		
		add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		add.getElement().getStyle().set("margin-top", "15px");
		add.addClickListener(e -> {
			AddUser(param);
		});

		cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancel.getElement().getStyle().set("margin-top", "15px");

		HorizontalLayout buttonLayout = new HorizontalLayout(add, cancel);
		FormLayout form = new FormLayout();
		form.getStyle().setPadding("10px");
//		form.getStyle().setAlignItems(AlignItems.CENTER);
		form.add(id, email, username, pass, buttonLayout);
		form.setResponsiveSteps(
				// Use one column by default
				new ResponsiveStep("0", 1));

		update();
		users.setAllRowsVisible(true);
		users.addColumn(Userdetails::getId).setHeader("ID").setAutoWidth(true).setFrozen(true).setSortable(true);
		users.addColumn(Userdetails::getEmail).setHeader("Email").setAutoWidth(true);
		users.addColumn(Userdetails::getUsername).setHeader("UserName").setAutoWidth(true);
		// Edit button
		Grid.Column<Userdetails> editsource = users.addComponentColumn(editdata -> {
			// create edit button for each row
			Button addinst = new Button("EDIT");
			// set icon
			addinst.setIcon(new Icon(VaadinIcon.EDIT));
			// set theme
			addinst.addThemeVariants(ButtonVariant.LUMO_SMALL);
			// on click operation
			addinst.addClickListener(ed -> {
				// Long locationId = editdata.getId()
				main_id = editdata.getId();
				id.setValue(String.valueOf(editdata.getId()));
				email.setValue(editdata.getEmail());
				username.setValue(editdata.getUsername());
				pass.setValue(editdata.getPassword());
			});
			return addinst;
		}).setTextAlign(ColumnTextAlign.CENTER).setAutoWidth(true);

		// Delete
		users.addColumn(new ComponentRenderer<>(item -> {
			Button deletebtn = new Button("Delete");
			deletebtn.setIcon(new Icon(VaadinIcon.TRASH));
			deletebtn.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ERROR);
			deletebtn.setWidthFull();
			deletebtn.addClickListener(even -> {
				Dialog dialog = new Dialog();
				dialog.open();
				dialog.add(
						new VerticalLayout(new H3("Confirm Delete?"), new Label("Are you sure you want to delete?")));
				dialog.setCloseOnEsc(false);
				dialog.setCloseOnOutsideClick(false);
				VerticalLayout layout = new VerticalLayout();
				HorizontalLayout buttons = new HorizontalLayout();
				Button confirmButton = new Button("Confirm", ev -> {
					userRepository.delete(item);
					dialog.close();
					// UI.getCurrent().getPage().reload();
					update();
				});
				Button cancelButton = new Button("Cancel", ev -> {
					dialog.close();
				});
				buttons.add(confirmButton, cancelButton);
				layout.add(buttons);
				layout.setHorizontalComponentAlignment(Alignment.CENTER, buttons);
				confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
				dialog.add(layout);
			});
			return deletebtn;
		})).setAutoWidth(true);

		add(navbar, hr, form, new Hr(), users);
	}

	public void AddUser(String param) {
		if (param.equals("0")) {
			if (!userRepository.check_source(username.getValue())) {
				// Create a new SourceTable object
				Userdetails st = new Userdetails();
				st.setId(Long.parseLong(id.getValue()));
				st.setEmail(email.getValue());
				st.setUsername(username.getValue());
				BCryptPasswordEncoder bycrypt = new BCryptPasswordEncoder();
				String hashPass= bycrypt.encode(pass.getValue());
				st.setPassword(hashPass);
				st.setRole("OPERATOR");
				// Save the source
				userRepository.save(st);
				update();
				Notification.show("Users have been updated successfully!");
				System.out.println("-------- Adding User -------");
//				add.setEnabled(false);
				
			} else {
				Notification.show("User Name or Email Already Exists");
			}
		} else {
			// Update the existing source
			Userdetails st = new Userdetails();
			st.setId(Long.parseLong(id.getValue()));
			st.setEmail(email.getValue());
			st.setUsername(username.getValue());
			BCryptPasswordEncoder bycrypt = new BCryptPasswordEncoder();
			String hashPass= bycrypt.encode(pass.getValue());
			st.setPassword(hashPass);
			st.setRole("OPERATOR");
			st.setId(main_id);

			// Save the updated source
			userRepository.save(st);
			Notification.show("Users have been updated successfully!");
			System.out.println("-------- Updating User -------");
			update();
		}
	}

	public void update() {
		List<Userdetails> list = userRepository.findAll();
		dataProvider = new ListDataProvider<>(list);
		users.setItems(list);
		users.setDataProvider(dataProvider);
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		init(parameter);
	}

}
