package com.project.ams.vaadin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.spring.MapRepository;
import com.project.ams.spring.MappingData;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;

@Route(value = "/tagMapping", layout = MainLayout.class)
public class TagMapping extends VerticalLayout {
	
	@Autowired
	MapRepository mapRepository;
	
	private final TextField source_id = new TextField("Source Id");
	private final TextField reg_name = new TextField("Register Name");
	private final TextField reg_address = new TextField("Register Address");
	private final TextField reg_length = new TextField("Register Length");
	private final Select<String> reg_type = new Select<>();
	private final Select<Integer> multiplier = new Select<>();
	private final TextField element_name = new TextField("Element Name");
	private final Select<String> point_type = new Select<>();
	private final Button backbtn = new Button("Back");
	private final Button submitbtn = new Button("Submit");
	private final Button producebtn = new Button("Kafka Produce");
	
	Grid<MappingData> grid = new Grid<>(MappingData.class,false);
	ListDataProvider<MappingData> dataProvider;

	
	public TagMapping(MapRepository mapRepository) {
		this.mapRepository = mapRepository;
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
		reg_type.setLabel("Register Data Type");
		reg_type.setItems("float", "int", "double", "boolean");
		reg_type.setErrorMessage("This field is required");
		reg_type.setWidthFull();

		multiplier.setRequiredIndicatorVisible(true);
		multiplier.setLabel("Multiplier");
		multiplier.setItems(1, 100, 1000, 10000);
		multiplier.setErrorMessage("This field is required");
		multiplier.setWidthFull();

		element_name.setRequiredIndicatorVisible(true);
		element_name.setErrorMessage("This field is required");
		element_name.setWidthFull();

		point_type.setRequiredIndicatorVisible(true);
		point_type.setLabel("Modbus Point Type");
		point_type.setItems("01: COIL STATUS", "02: INPUT STATUS", "03: HOLDING REGISTER", "04: INPUT REGISTER");
		point_type.setErrorMessage("This field is required");
		point_type.setWidthFull();
		
		// Fetch the latest ID from the database and increment it by 1
				Long nextId = mapRepository.findMaxId();

				// Set the calculated ID as the value of the sourceIdField
				nextId = (nextId == null) ? 1L : nextId + 1;
				source_id.setValue(String.valueOf(nextId));
		

		backbtn.addClickListener(e -> {
			UI.getCurrent().navigate(RTUConfig.class);
		});

		submitbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//		submitbtn.addClickListener(e -> {
//			UI.getCurrent().getPage().reload();
//		});
		submitbtn.addClickListener(e -> {
			SaveTags();
//			Comm();
			
		});

		producebtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

		HorizontalLayout buttonLayout = new HorizontalLayout(backbtn, submitbtn, producebtn);
		VerticalLayout v1 = new VerticalLayout(source_id, reg_name, reg_address, reg_length);
		VerticalLayout v2 = new VerticalLayout(reg_type, multiplier, element_name, point_type);
		HorizontalLayout form = new HorizontalLayout(v1, v2);
		form.setSpacing(true);
		form.setSizeFull();
		buttonLayout.setSizeFull();
		add(form, buttonLayout);
		
//		    grid.addColumn(MappingData::getSource_id).setHeader("Source Id");
//		    grid.addColumn(MappingData::getReg_name).setHeader("Register Name");
//		    grid.addColumn(MappingData::getReg_address).setHeader("Register Address");
//		    grid.addColumn(MappingData::getReg_length).setHeader("Register Length");
//		    grid.addColumn(MappingData::getReg_type).setHeader("Register Data Type");
//		    grid.addColumn(MappingData::getMultiplier).setHeader("Multiplier");
//		    grid.addColumn(MappingData::getElement_name).setHeader("Element Name");
//		    grid.addColumn(MappingData::getPoint_type).setHeader("Modbus Point Type");
//		    
//		    List<MappingData> list = mapRepository.findAll();
//			dataProvider = new ListDataProvider<>(list);
//			grid.setItems(list);
//			grid.setDataProvider(dataProvider);
		    //List<MappingData> mappingData = mapRepository.findAll();
		    //grid.setItems(mapRepository.findAll());
		update();
		Grid.Column<MappingData> Source_id = grid.addColumn(MappingData::getSource_id).setHeader("Source id");
		
		add(new Hr(), grid);
	}

public void update() {
		List<MappingData> list = mapRepository.findAll();
		dataProvider = new ListDataProvider<>(list);
		grid.setItems(list);
		
	}

//	private void getSavedMappings() {
//	    grid.addColumn((ValueProvider<MappingData, Long>) MappingData::getSource_id).setHeader("Source Id");
//	    grid.addColumn((ValueProvider<MappingData, String>) MappingData::getReg_name).setHeader("Register Name");
//	    grid.addColumn((ValueProvider<MappingData, Integer>) MappingData::getReg_address).setHeader("Register Address");
//	    grid.addColumn((ValueProvider<MappingData, Integer>) MappingData::getReg_length).setHeader("Register Length");
//	    grid.addColumn((ValueProvider<MappingData, String>) MappingData::getReg_type).setHeader("Register Data Type");
//	    grid.addColumn((ValueProvider<MappingData, Integer>) MappingData::getMultiplier).setHeader("Multiplier");
//	    grid.addColumn((ValueProvider<MappingData, String>) MappingData::getElement_name).setHeader("Element Name");
//	    grid.addColumn((ValueProvider<MappingData, String>) MappingData::getPoint_type).setHeader("Modbus Point Type");
//	    
//	    //List<MappingData> mappingData = mapRepository.findAll();
//	    grid.setItems(mapRepository.findAll());
//	}

		private void SaveTags() {
			MappingData mappingData = new MappingData();
			mappingData.setReg_name(reg_name.getValue());
			mappingData.setReg_address(Integer.parseInt(reg_address.getValue()));
			mappingData.setReg_length(Integer.parseInt(reg_length.getValue()));
			mappingData.setReg_type(reg_type.getValue());
			mappingData.setMultiplier(multiplier.getValue());
			mappingData.setElement_name(element_name.getValue());
			mappingData.setPoint_type(point_type.getValue());
			mapRepository.save(mappingData);
			Notification.show("Tags Saved!");
//			clearForm();
//			getSavedMappings();
}
		/*
		 * 
		 * Take the Polling interval value and time format value. in the kafka produce
		 * method, add the logic for time format for eg. if the time is in ms then
		 * convert seconds, mins and hrs acc. and multiply the polling value with it.
		 * add the thread and add the polling value. produce after that interval
		 * 
		 * 
		 * also add the logic to stop the production.
		 * 
		 * 
		 * add the grid when the data is saved0
		 */
//		private void clearForm() {
//			//UI.getCurrent().getPage().executeJs("setTimeout(function() { location.reload(); }, 5000);");
//			 // Reload only the form by executing JavaScript
//		    UI.getCurrent().getPage().executeJs("location.reload();");
//		}

		// Configure the grid
//        grid.setColumns("sourceId", "regName", "regAddress", "regLength", "regType",
//                "multiplier", "elementName", "pointType");
//        // Remove the line related to "savedDate"
//        grid.setDataProvider(getSavedMappings());
//
//
//        add(form, buttonLayout);
//        add(new Hr(), grid);
//	}
//




//	private void Comm() {
//		SerialConnection con = null;
//		Details details = new Details();
//		try {
//			SerialParameters params = new SerialParameters();
//			params.setPortName(details.getCom_port());
//			params.setBaudRate(details.getBaud_rate());
//			params.setDatabits(details.getData_bits());
//			params.setParity(details.getParity());
//			params.setStopbits(details.getStop_bits()); // only for hubli
//			params.setEncoding("RTU");
//			params.setEcho(false);
//			con = new SerialConnection(params);
//
//			if (!con.isOpen()) {
//				con.open();
//				Notification.show("Modbus RTU Device Connected Successfully").setDuration(3000);
//				//UI.getCurrent().navigate(TagMapping.class);
//				Notification.show("Reading....").setDuration(3000);
//			} else {
//				Notification.show("Failed to connect to Modbus RTU Device").setDuration(3000);
//			}
//		} catch (Exception e) {
//			System.out.println(e);
//			Notification.show("An error occurred while trying to connect to Modbus RTU Device").setDuration(3000);
//		}
//		
//	}

}
