package com.project.ams.views;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.Security.SecurityService;
import com.project.ams.vaadin.Adduser;
import com.project.ams.vaadin.Index;
import com.project.ams.vaadin.RTUConfig;
import com.project.ams.vaadin.TagMapping;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route
public class MainLayout extends AppLayout {
	
	 private SecurityService securityService;

	public MainLayout(@Autowired SecurityService securityService) {
		 this.securityService = securityService;
		 
		DrawerToggle toggle = new DrawerToggle();

		H1 title = new H1("AMS");
		title.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "1");

		SideNav nav = getSideNav();
		nav.getStyle().set("padding", "0.25em");

		Scroller scroller = new Scroller(nav);
		scroller.setClassName(LumoUtility.Padding.MEDIUM);
		scroller.setClassName(LumoUtility.AlignItems.CENTER);
		
		HorizontalLayout header;
        
		
		if (securityService.getAuthenticatedUser() != null) {
			 Button logout = new Button("Logout", click -> securityService.logout());
	            logout.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_TERTIARY_INLINE);

	            HorizontalLayout logoutLayout = new HorizontalLayout(logout);
	            logoutLayout.setWidthFull();
	            logoutLayout.getStyle().setMarginRight("30px");
	            logoutLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

	            header = new HorizontalLayout(toggle, title, logoutLayout);
	            header.addClassName("header");
	            header.setWidth("100%");
	            header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        } else {
            header = new HorizontalLayout(toggle,title);
        }

		addToDrawer(scroller);
		addToNavbar(header);
	}

	public SideNav getSideNav() {
		SideNav sideNav = new SideNav();
		
		SideNavItem src = new SideNavItem("Source Management", Index.class, VaadinIcon.INFO_CIRCLE.create()); 
		SideNavItem rtu = new SideNavItem("Modbus RTU Config", RTUConfig.ROUTE_NAME + "/" + 0, VaadinIcon.AUTOMATION.create());
		SideNavItem tag = new SideNavItem("Tag Mapping", TagMapping.ROUTE_NAME + "/" + 0, VaadinIcon.SPLINE_AREA_CHART.create());
		SideNavItem settings = new SideNavItem("Setting");
				settings.setPrefixComponent(VaadinIcon.COG.create());
				settings.addItem(new SideNavItem("Add User",Adduser.ROUTE_NAME + "/" + 0, VaadinIcon.USER.create()));
		sideNav.addItem(src,rtu,tag,settings);
//                new SideNavItem("Products", "/products",
//                        VaadinIcon.PACKAGE.create()),
//                new SideNavItem("Documents", "/documents",
//                        VaadinIcon.RECORDS.create()),
//                new SideNavItem("Tasks", "/tasks", VaadinIcon.LIST.create()),
//                new SideNavItem("Analytics", "/analytics",
//                        VaadinIcon.CHART.create()));
		src.getStyle().setMarginTop("2px");
		rtu.getStyle().setMarginTop("2px");
		tag.getStyle().setMarginTop("2px");
		settings.getStyle().setMarginTop("2px");
		
		src.getStyle().setMarginBottom("4px");
		rtu.getStyle().setMarginBottom("4px");
		tag.getStyle().setMarginBottom("4px");
		settings.getStyle().setMarginBottom("4px");

		return sideNav;
	}

}