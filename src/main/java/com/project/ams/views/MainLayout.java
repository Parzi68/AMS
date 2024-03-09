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
		title.getStyle().set("font-size", "var(--lumo-font-size-m)").set("margin", "1");

		SideNav nav = getSideNav();
		nav.getStyle().set("padding", "0.25em");

		Scroller scroller = new Scroller(nav);
		scroller.setClassName(LumoUtility.Padding.MEDIUM);
		scroller.setClassName(LumoUtility.AlignItems.CENTER);

		addToDrawer(scroller);
		addToNavbar(toggle, title);
	}

	public SideNav getSideNav() {
		SideNav sideNav = new SideNav();
		
		SideNavItem src = new SideNavItem("Source Management", "", VaadinIcon.INFO_CIRCLE.create()); 
		SideNavItem rtu = new SideNavItem("Modbus RTU Config", "/rtuconfig", VaadinIcon.AUTOMATION.create());
		SideNavItem tag = new SideNavItem("Tag Mapping", "/tagMapping", VaadinIcon.SPLINE_AREA_CHART.create());
		sideNav.addItem(src,rtu,tag);
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
		
		src.getStyle().setMarginBottom("4px");
		rtu.getStyle().setMarginBottom("4px");
		tag.getStyle().setMarginBottom("4px");

		return sideNav;
	}

}
