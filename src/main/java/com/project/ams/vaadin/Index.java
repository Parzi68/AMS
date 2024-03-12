package com.project.ams.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.spring.Asset;
import com.project.ams.spring.AssetRepository;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style.JustifyContent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;

@PageTitle("Asset Info")
@Route(value = " ", layout = MainLayout.class)
public class Index extends VerticalLayout {
	Button btn = new Button("Add Source");
	Grid<Asset> grid = new Grid<>(Asset.class, false);
	List<Asset> assetList;
	@Autowired
	private AssetRepository assetRepository;

	@PostConstruct
	public void init() {
		this.assetList = assetRepository.findAll();

		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Asset Source Info  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");
		add(navbar, hr);

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
		add(v1);

		// Set up the grid
		grid.removeAllColumns();
		grid.setItems(assetList);
		grid.addColumn(Asset::getId).setHeader("ID").setFrozen(true).setAutoWidth(true).setFlexGrow(0);
		grid.addColumn(Asset::getSource_id).setHeader("Source Id").setAutoWidth(true);
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
		}).setAutoWidth(true);

		// Add delete button column
		grid.addComponentColumn(asset -> {
			Button deleteButton = new Button("Delete");
			deleteButton.addClickListener(event -> deleteAsset(asset.getId()));
			deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
			deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ERROR);
			return deleteButton;
		}).setAutoWidth(true) ;

		add(grid);
	}

	private void deleteAsset(Long assetId) {
		Dialog confirmDialog = new Dialog();
		confirmDialog.add(new H3("Confirm Delete?"), new Button("Confirm", event -> {
			assetRepository.deleteById(assetId);
			refreshGrid();
			confirmDialog.close();
		}), new Button("Cancel", event -> confirmDialog.close()));
		confirmDialog.open();
	}

	private void refreshGrid() {
		assetList = assetRepository.findAll();
		grid.setItems(assetList);
	}
}
