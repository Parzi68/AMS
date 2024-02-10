package com.project.ams.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route
public class MainLayout extends AppLayout {

	public MainLayout() {
		DrawerToggle toggle = new DrawerToggle();

		H1 title = new H1("AMS");
		title.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "0");

		SideNav nav = getSideNav();

		Scroller scroller = new Scroller(nav);
		scroller.setClassName(LumoUtility.Padding.SMALL);

		addToDrawer(scroller);
		addToNavbar(toggle, title);
	}

	public SideNav getSideNav() {
		SideNav sideNav = new SideNav();
		sideNav.addItem(new SideNavItem("Asset Management Form", "", VaadinIcon.DASHBOARD.create()));
//                new SideNavItem("Orders", "/orders", VaadinIcon.CART.create()),
//                new SideNavItem("Customers", "/customers",
//                        VaadinIcon.USER_HEART.create()),
//                new SideNavItem("Products", "/products",
//                        VaadinIcon.PACKAGE.create()),
//                new SideNavItem("Documents", "/documents",
//                        VaadinIcon.RECORDS.create()),
//                new SideNavItem("Tasks", "/tasks", VaadinIcon.LIST.create()),
//                new SideNavItem("Analytics", "/analytics",
//                        VaadinIcon.CHART.create()));
		return sideNav;
	}

}
