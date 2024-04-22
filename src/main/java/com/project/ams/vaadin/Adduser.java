package com.project.ams.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

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

import jakarta.annotation.PostConstruct;

@PageTitle("Add User")
@Route(value = "/Adduser", layout = MainLayout.class)
public class Adduser extends VerticalLayout implements HasUrlParameter<String> {
	
	public static final String ROUTE_NAME = "Adduser";
	
	@Autowired
	private UserRepository userRepository;
	
	Userdetails user = new Userdetails();
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
		add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		add.getElement().getStyle().set("margin-top", "15px");
		add.addClickListener(e -> {
			AddUser();
		});

		cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancel.getElement().getStyle().set("margin-top", "15px");

		HorizontalLayout buttonLayout = new HorizontalLayout(add, cancel);
		FormLayout form = new FormLayout();
		form.getStyle().setPadding("10px");
//		form.getStyle().setAlignItems(AlignItems.CENTER);
		form.add(email, username, pass, buttonLayout);
		form.setResponsiveSteps(
				// Use one column by default
				new ResponsiveStep("0", 1));

		update();
		users.setAllRowsVisible(true);
		users.addColumn(Userdetails::getId).setHeader("ID").setAutoWidth(true).setFrozen(true);
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
				email.setValue(editdata.getEmail());
				username.setValue(editdata.getUsername());
				pass.setValue(editdata.getPassword());
			});
			return addinst;
		}).setTextAlign(ColumnTextAlign.CENTER);

		// Delete
		users.addColumn(new ComponentRenderer<>(item -> {
			Button deletebtn = new Button("Delete");
			deletebtn.setIcon(new Icon(VaadinIcon.TRASH));
			deletebtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
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

	public void AddUser() {
		user.setEmail(email.getValue());
		user.setUsername(username.getValue());
		user.setPassword(pass.getValue());
		userRepository.save(user);
		System.out.println("-------- Adding User -------");
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
