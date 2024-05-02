package com.project.ams.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.spring.Repository.AssetRepository;
import com.project.ams.spring.model.Asset;
import com.project.ams.spring.model.Mappingdata;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.Style.JustifyContent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Asset Info  |  AMS")
@Route(value = "/admin", layout = MainLayout.class)
//@RouteAlias("index")
//@RolesAllowed("ADMIN")
public class Index extends VerticalLayout {
	Button btn = new Button("Add Source");
	Grid<Asset> grid = new Grid<>(Asset.class, false);
	ListDataProvider<Asset> dataProvider;
	@Autowired
	private AssetRepository assetRepository;

	@PostConstruct
	public void init() {
		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Asset Source Info  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");

		btn.addClickListener(e -> {
			UI.getCurrent().navigate(AssetInfo.ROUTE_NAME + "/" + 0);
		});
		btn.setSizeFull();
		btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
		btn.getStyle().setPadding("0.35em");
		btn.getStyle().setJustifyContent(JustifyContent.CENTER);
		btn.getStyle().set("font-weight", "bold");

		VerticalLayout v1 = new VerticalLayout(btn);
//		v1.setPadding(true);

		// Set up the grid
		update();
		grid.removeAllColumns();
//		grid.addColumn(Asset::getId).setHeader("ID").setFrozen(true).setAutoWidth(true).setFlexGrow(0);
		grid.addColumn(Asset::getSource_id).setHeader("Source Id").setAutoWidth(true).setAutoWidth(true).setFrozen(true);
		grid.addColumn(Asset::getSource_name).setHeader("Source Name").setAutoWidth(true);
		grid.addColumn(Asset::getApplication_name).setHeader("Application Name").setAutoWidth(true);
		grid.addColumn(Asset::getLongitude).setHeader("Longitude").setAutoWidth(true);
		grid.addColumn(Asset::getLatitude).setHeader("Latitude").setAutoWidth(true);
		grid.addColumn(Asset::getLocation_name).setHeader("Location Name").setAutoWidth(true);
		grid.addColumn(Asset::getProtocol_type).setHeader("Protocol Type").setAutoWidth(true);
		grid.addColumn(Asset::getInstall_date).setHeader("Install Date").setAutoWidth(true);
		grid.addColumn(Asset::getModified_date).setHeader("Modified Date").setAutoWidth(true);

		// Add edit button column
		grid.addComponentColumn(asset -> {
			Button editButton = new Button("Edit");
			editButton.setIcon(new Icon(VaadinIcon.EDIT));
			editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
			editButton.addClickListener(event -> UI.getCurrent().navigate(AssetInfo.ROUTE_NAME + "/" + asset.getId()));
			return editButton;
		}).setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);

		 grid.addColumn(new ComponentRenderer<>(item -> {
				Button deletebtn = new Button("Delete");
				deletebtn.setIcon(new Icon(VaadinIcon.TRASH));
				deletebtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
				deletebtn.setWidthFull();
				deletebtn.addClickListener(even -> {
					Dialog dialog = new Dialog();
					dialog.open();
					dialog.add(new VerticalLayout(new H3("Confirm Delete?"),
							new Label("Are you sure you want to delete?")));
					dialog.setCloseOnEsc(false);
					dialog.setCloseOnOutsideClick(false);
					VerticalLayout layout = new VerticalLayout();
					HorizontalLayout buttons = new HorizontalLayout();
					Button confirmButton = new Button("Confirm", ev -> {
						assetRepository.delete(item);
						dialog.close();
						//UI.getCurrent().getPage().reload();
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

		add(navbar, hr,v1,grid);
	}

	public void update() {
		List<Asset> list = assetRepository.findAll();
		dataProvider = new ListDataProvider<>(list);
		grid.setItems(list);
		grid.setDataProvider(dataProvider);
	}
}
