package com.project.ams.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("OPERATOR")
public class UserLayout extends AppLayout {

	public UserLayout() {
		DrawerToggle toggle = new DrawerToggle();
		toggle.setEnabled(false);

		H1 title = new H1("AMS");
		title.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "1");

		HorizontalLayout header;

//		if (securityService.getAuthenticatedUser() != null) {
		Button logout = new Button("Logout", click -> {
			UI.getCurrent().navigate(LoginView.class);
		});
		logout.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);

		HorizontalLayout logoutLayout = new HorizontalLayout(logout);
		logoutLayout.setWidthFull();
		logoutLayout.getStyle().setMarginRight("30px");
		logoutLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

		header = new HorizontalLayout(toggle, title, logoutLayout);
		header.addClassName("header");
		header.setWidth("100%");
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

//        } else {
//            header = new HorizontalLayout(toggle,title);
//        }
		addToNavbar(header);
	}
}
