package com.project.ams.vaadin;

import java.util.List; 
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.fazecast.jSerialComm.SerialPort;
import com.project.ams.spring.Repository.ConfigRepository;
import com.project.ams.spring.model.Rtuconfig;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
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
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("RTU Config  |  AMS")
@SuppressWarnings("removal")
@Route(value = "/rtuconfig", layout = MainLayout.class)
//@RolesAllowed("ADMIN")
public class RTUConfig extends VerticalLayout implements HasUrlParameter<String> {
	public static final String ROUTE_NAME = "rtuconfig";
	@Autowired
	private ConfigRepository configRepository;
//	Rtuconfig config = new Rtuconfig();
	TextField id = new TextField("id");
	TextField source_id = new TextField("Source id");
	TextField slave_id = new TextField("Slave ID");
	Select<String> com_port = new Select<>();
	Select<String> baud_rate = new Select<>();
	Select<String> data_bits = new Select<>();
	Select<String> stop_bits = new Select<>();
	Select<String> parity = new Select<>();
	Select<Integer> pollInterval = new Select<>();
	Select<Integer> repInterval = new Select<>();
	Select<String> timeFormat = new Select<>();
	Select<String> timeFormat2 = new Select<>();
	Button backbtn = new Button("Back");
	Button savebtn = new Button("Save");
	Button next = new Button("Next");
	Grid<Rtuconfig> grid = new Grid<>(Rtuconfig.class, false);
	ListDataProvider<Rtuconfig> dataProvider;
	long main_id = 0;


//	@PostConstruct
	@SuppressWarnings("removal")
	public void init(String param) {
		main_id = Long.parseLong(param);

		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Modbus RTU Configuration  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");
//		add(navbar, hr);

		source_id.setReadOnly(true);
		source_id.setValue(param);
		source_id.setWidthFull();

		slave_id.setRequiredIndicatorVisible(true);
		slave_id.setRequired(true);
		slave_id.setErrorMessage("This field is required");
		slave_id.setRequired(true);
		slave_id.setWidthFull();

		com_port.setLabel("COM_Port");
		com_port.setRequiredIndicatorVisible(true);
		com_port.setErrorMessage("This field is required.");
		com_port.setWidthFull();

		baud_rate.setRequiredIndicatorVisible(true);
		baud_rate.setErrorMessage("This field is required.");
		baud_rate.setItems("1200", "2400", "3600", "4800", "9600", "19200", "38400", "57600", "115200");
		baud_rate.setLabel("Baud Rate");
		baud_rate.setWidthFull();

		data_bits.setRequiredIndicatorVisible(true);
		data_bits.setErrorMessage("This field is required.");
		data_bits.setItems("7", "8");
		data_bits.setLabel("Data Bits");
		data_bits.setWidthFull();

		stop_bits.setRequiredIndicatorVisible(true);
		stop_bits.setErrorMessage("This field is required.");
		stop_bits.setItems("1", "2");
		stop_bits.setLabel("Stop Bits");
		stop_bits.setWidthFull();

		parity.setRequiredIndicatorVisible(true);
		parity.setErrorMessage("This field is required.");
		parity.setItems("none", "odd", "even");
		parity.setLabel("Parity");
		parity.setWidthFull();

		// Setting values for Polling Interval
		pollInterval.setItems(IntStream.rangeClosed(1, 60).boxed().collect(Collectors.toList()));
		pollInterval.setValue(1);
		pollInterval.setLabel("Polling Interval");
		pollInterval.setRequiredIndicatorVisible(true);
		pollInterval.setErrorMessage("This field is required.");

		// Setting values for Report Interval
		repInterval.setItems(IntStream.rangeClosed(1, 60).boxed().collect(Collectors.toList()));
		repInterval.setValue(1);
		repInterval.setLabel("Report Interval");
		repInterval.setRequiredIndicatorVisible(true);
		repInterval.setErrorMessage("This field is required.");

		// Setting values for Time Format
		timeFormat.setLabel("Set Time Format");
		timeFormat.setValue("Select");
		timeFormat.setItems("Seconds", "Minutes", "Hours");
		timeFormat.setRequiredIndicatorVisible(true);
		timeFormat.setErrorMessage("This field is required.");

		// Setting values for Time Format
		timeFormat2.setLabel("Set Time Format");
		timeFormat2.setValue("Select");
		timeFormat2.setItems("Seconds", "Minutes", "Hours");
		timeFormat2.setRequiredIndicatorVisible(true);
		timeFormat2.setErrorMessage("This field is required.");

		// Automatically scan and set the available COM ports
		setComPortValue();

		// Fetch the latest ID from the database and increment it by 1
//		Long nextId = configRepository.findMaxId();

		// Set the calculated ID as the value of the sourceIdField
//		nextId = (nextId == null) ? 1L : nextId + 1;
//		source_id.setValue(String.valueOf(nextId));
		
		if (!param.equals("0")) {
			for (Rtuconfig d1 : configRepository.details_list(main_id)) {
				// source_id.setValue(Integer.parseInt(a1.getSource_id()));
				slave_id.setValue(String.valueOf(d1.getSlave_id()));
				com_port.setValue(d1.getCom_port());
				baud_rate.setValue(d1.getBaud_rate());
				data_bits.setValue(d1.getData_bits());
				stop_bits.setValue(d1.getStop_bits());
				parity.setValue(d1.getParity());
				pollInterval.setValue(d1.getPolling_interval());
				timeFormat.setValue(d1.getTime_format());
				repInterval.setValue(d1.getReport_interval());
				timeFormat2.setValue(d1.getSet_time_format());
			}
		}

		backbtn.addClickListener(e -> {
			UI.getCurrent().navigate(AssetInfo.ROUTE_NAME+"/" + source_id.getValue());
		});

		savebtn.addClickListener(e -> {
			saveDetails(param);
		});
		savebtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//		connect.addClickListener(e -> {
//			Connection();
//		UI.getCurrent().navigate(TagMapping.class);
//		});
//		connect.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
//		connect.setVisible(false);
		
		next.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);
		next.addClickListener(e -> {
			UI.getCurrent().navigate(TagMapping.ROUTE_NAME+"/"+source_id.getValue());
		});

		HorizontalLayout buttonLayout = new HorizontalLayout(backbtn, savebtn,next);
		buttonLayout.setSpacing(true);
		FormLayout h2 = new FormLayout();
		h2.add(pollInterval, timeFormat);
//		h2.setSpacing(true);
		FormLayout h3 = new FormLayout();
		h3.add(repInterval, timeFormat2);
//		h3.setSpacing(true);
		VerticalLayout v1 = new VerticalLayout(source_id, slave_id, com_port, baud_rate, data_bits, stop_bits, parity,
				h2, h3, buttonLayout);
		v1.setPadding(true);
		// v1.setMargin(true);
//		add(navbar, hr,v1);

		update();
		grid.setAllRowsVisible(true);
//		grid.addColumn(Rtuconfig::getId).setHeader("Id").setFrozen(true).setFlexGrow(0).setAutoWidth(true);
		grid.addColumn(Rtuconfig::getSource_id).setHeader("Source Id").setAutoWidth(true).setFrozen(true).setAutoWidth(true);
		grid.addColumn(Rtuconfig::getSlave_id).setHeader("Slave Id").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getCom_port).setHeader("COM_Port").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getBaud_rate).setHeader("Baud Rate").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getData_bits).setHeader("Data Bits").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getStop_bits).setHeader("Stop Bits").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getParity).setHeader("Parity").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getPolling_interval).setHeader("Polling Interval").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getSet_time_format).setHeader("Time Format").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getReport_interval).setHeader("Report Interval").setAutoWidth(true);
		grid.addColumn(Rtuconfig::getTime_format).setHeader("Time Format").setAutoWidth(true);

		// Add edit button column
		Grid.Column<Rtuconfig> editsource = grid.addComponentColumn(editdata -> {
			// create edit button for each row
			Button addinst = new Button("EDIT");
			// set icon
			addinst.setIcon(new Icon(VaadinIcon.EDIT));
			// set theme
			addinst.addThemeVariants(ButtonVariant.LUMO_SMALL);
			// on click operation
			addinst.addClickListener(ed -> {
				//Long locationId = editdata.getId()
				main_id=editdata.getId();
				id.setValue(String.valueOf(editdata.getId()));
				source_id.setValue(String.valueOf(editdata.getSource_id()));
				slave_id.setValue(String.valueOf(editdata.getSlave_id()));
				com_port.setValue(editdata.getCom_port());
				baud_rate.setValue(editdata.getBaud_rate());
				data_bits.setValue((editdata.getData_bits()));
				stop_bits.setValue(editdata.getStop_bits());
				parity.setValue(editdata.getParity());
				pollInterval.setValue(editdata.getPolling_interval());
				timeFormat.setValue(editdata.getTime_format());
				repInterval.setValue(editdata.getReport_interval());
				timeFormat2.setValue(editdata.getSet_time_format());
				// Audit Trial
			});
			return addinst;
		}).setAutoWidth(true).setTextAlign(ColumnTextAlign.CENTER);

		// Add delete button column
		grid.addColumn(new ComponentRenderer<>(item -> {
			Button deletebtn = new Button("Delete");
			deletebtn.setIcon(new Icon(VaadinIcon.TRASH));
			deletebtn.addThemeVariants(ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_ERROR);
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
					configRepository.delete(item);
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
		
		add(navbar, hr,v1,new Hr(), grid);
	}

	private void setComPortValue() {
		SerialPort[] ports = SerialPort.getCommPorts();
		for (SerialPort port : ports) {
			com_port.setItems(port.getSystemPortName());
		}
	}

	private void saveDetails(String param) {
		if (param.equals("0")) {
	        if (!configRepository.check_source(Integer.parseInt(slave_id.getValue()))){
	            // Create a new SourceTable object
	        	Rtuconfig st = new Rtuconfig();
	        	st.setId(Long.parseLong(id.getValue()));
	            st.setSource_id(Integer.parseInt(source_id.getValue()));
	            st.setSlave_id(Integer.parseInt(slave_id.getValue()));
	            st.setCom_port(com_port.getValue());
	            st.setBaud_rate(baud_rate.getValue());
	            st.setData_bits(data_bits.getValue());
	            st.setStop_bits(stop_bits.getValue());
	            st.setParity(parity.getValue());
	            st.setPolling_interval(pollInterval.getValue());
	            st.setTime_format(timeFormat.getValue());
	            st.setReport_interval(repInterval.getValue());
	            st.setSet_time_format(timeFormat2.getValue());
	            // Save the source
	            configRepository.save(st);
	            Notification.show("Configurations has been saved successfully");
	            savebtn.setEnabled(false);
	            update();
	        } else {
	            Notification.show("Slave id Already Exists");
	        }
	    } else {
	        // Update the existing source
	    	Rtuconfig st = new Rtuconfig();
	    	st.setId(Long.parseLong(id.getValue()));
            st.setSource_id(Integer.parseInt(source_id.getValue()));
            st.setSlave_id(Integer.parseInt(slave_id.getValue()));
            st.setCom_port(com_port.getValue());
            st.setBaud_rate(baud_rate.getValue());
            st.setData_bits(data_bits.getValue());
            st.setStop_bits(stop_bits.getValue());
            st.setParity(parity.getValue());
            st.setPolling_interval(pollInterval.getValue());
            st.setTime_format(timeFormat.getValue());
            st.setReport_interval(repInterval.getValue());
            st.setSet_time_format(timeFormat2.getValue());
	        st.setId(main_id);
	        // Save the updated source
	        configRepository.save(st);
	        Notification.show("Configurations has been updated successfully");
	        update();
	    }
	}
	
	public void update() {
		List<Rtuconfig> list = configRepository.findAll();
		dataProvider = new ListDataProvider<>(list);
		grid.setItems(list);
		grid.setDataProvider(dataProvider);
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		init(parameter);
	}
}
