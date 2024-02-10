package com.project.ams.vaadin;

import com.project.ams.spring.MappingData;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;

@Route(value = "/tagMapping", layout = MainLayout.class)
public class TagMapping extends VerticalLayout {
	private final TextField source_id = new TextField("Source Id");
	private final TextField reg_name = new TextField("Register Name");
	private final TextField reg_address = new TextField("Register Address");
	private final TextField reg_length = new TextField("Register Length");
	private final TextField reg_type = new TextField("Register Data Type");
	private final TextField multiplier = new TextField("Multiplier");
	private final TextField element_name = new TextField("Element Name");
	private final TextField point_type = new TextField("Modbus Point Type");
	private final Button backbtn = new Button("Back");
	private final Button submitbtn = new Button("Submit");
	
	private final Grid<MappingData> grid = new Grid<>(MappingData.class);

	public TagMapping() {
		setSizeFull();

		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Source Tag Mapping Form  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");
		add(navbar, hr);

		source_id.setReadOnly(true);
		source_id.setRequiredIndicatorVisible(true);
		source_id.setErrorMessage("This field is required");
		source_id.setWidthFull();

		reg_name.setRequiredIndicatorVisible(true);
		reg_name.setErrorMessage("This field is required");
		reg_name.setWidthFull();

		reg_address.setRequiredIndicatorVisible(true);
		reg_address.setErrorMessage("This field is required");
		reg_address.setWidthFull();

		reg_length.setRequiredIndicatorVisible(true);
		reg_length.setErrorMessage("This field is required");
		reg_length.setWidthFull();

		reg_type.setRequiredIndicatorVisible(true);
		reg_type.setErrorMessage("This field is required");
		reg_type.setWidthFull();

		multiplier.setRequiredIndicatorVisible(true);
		multiplier.setErrorMessage("This field is required");
		multiplier.setWidthFull();

		element_name.setRequiredIndicatorVisible(true);
		element_name.setErrorMessage("This field is required");
		element_name.setWidthFull();

		point_type.setRequiredIndicatorVisible(true);
		point_type.setErrorMessage("This field is required");
		point_type.setWidthFull();

		backbtn.addClickListener(e -> {
			UI.getCurrent().navigate(RTUConfig.class);
		});

		submitbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//		submitbtn.addClickListener(e -> {
//			UI.getCurrent().getPage().reload();
//		});

		HorizontalLayout buttonLayout = new HorizontalLayout(backbtn, submitbtn);
		VerticalLayout v1 = new VerticalLayout(source_id, reg_name, reg_address, reg_length);
		VerticalLayout v2 = new VerticalLayout(reg_type, multiplier, element_name, point_type);
		HorizontalLayout form = new HorizontalLayout(v1, v2);
		form.setSpacing(true);
		form.setSizeFull();
		buttonLayout.setSizeFull();
		add(form, buttonLayout);
		
		// Configure the grid
        grid.setColumns("sourceId", "regName", "regAddress", "regLength", "regType",
                "multiplier", "elementName", "pointType");
        // Remove the line related to "savedDate"
        grid.setItems(getSavedMappings()); // Replace with your data fetching logic

        add(form, buttonLayout);
        add(new Hr(), grid);
	}

	private DataProvider<MappingData, Void> getSavedMappings() {
		// TODO Auto-generated method stub
		return null;
	}

}
