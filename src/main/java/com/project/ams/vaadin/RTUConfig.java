package com.project.ams.vaadin;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fazecast.jSerialComm.SerialPort;
import com.project.ams.spring.ConfigRepository;
import com.project.ams.spring.Details;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

@Route(value = "/rtuconfig", layout = MainLayout.class)
public class RTUConfig extends VerticalLayout {
	private final ConfigRepository configRepository;
	private final TextField source_id = new TextField("Source id");
	private final TextField slave_id = new TextField("Slave ID");
	private final Select<String> com_port = new Select<>();
	private final Select<Integer> baud_rate = new Select<>();
	private final Select<Integer> data_bits = new Select<>();
	private final Select<Integer> stop_bits = new Select<>();
	Select<String> parity = new Select<>();
	private final Button backbtn = new Button("Back");
	private final Button savebtn = new Button("Save");
	private final Select<Integer> pollInterval = new Select<>();
	private final Select<Integer> repInterval = new Select<>();
	private final Select<String> timeFormat = new Select<>();
	private final Select<String> timeFormat2 = new Select<>();
	private final Button connect = new Button("Next");

	
	public RTUConfig(ConfigRepository configRepository) {
		this.configRepository = configRepository;

		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Modbus RTU Configuration  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");
		add(navbar, hr);

		source_id.setReadOnly(true);
		source_id.setWidthFull();

		slave_id.setRequiredIndicatorVisible(true);
		slave_id.setErrorMessage("This field is required");
		slave_id.setWidthFull();

		com_port.setLabel("COM_Port");
		com_port.setRequiredIndicatorVisible(true);
		com_port.setErrorMessage("This field is required.");
		com_port.setWidthFull();

		baud_rate.setRequiredIndicatorVisible(true);
		baud_rate.setErrorMessage("This field is required.");
		baud_rate.setItems(1200, 2400, 3600, 4800, 9600, 19200, 38400, 57600, 115200);
		baud_rate.setLabel("Baud Rate");
		baud_rate.setValue(0);
		baud_rate.setWidthFull();

		data_bits.setRequiredIndicatorVisible(true);
		data_bits.setErrorMessage("This field is required.");
		data_bits.setItems(7, 8);
		data_bits.setLabel("Data Bits");
		data_bits.setWidthFull();

		stop_bits.setRequiredIndicatorVisible(true);
		stop_bits.setErrorMessage("This field is required.");
		stop_bits.setItems(1, 2);
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

//		starting_address.setWidthFull();
//		starting_address.setErrorMessage("Enter valid starting address");
//		starting_address.setPattern("\\b\\d{5}\\b");
//		starting_address.setRequiredIndicatorVisible(true);
//
//		req_quantity.setWidthFull();
//		req_quantity.setErrorMessage("This field is required");
//		req_quantity.setRequiredIndicatorVisible(true);
		// Automatically scan and set the available COM ports
		setComPortValue();
		// com_port.setReadOnly(true);

		// Fetch the latest ID from the database and increment it by 1
		Long nextId = configRepository.findMaxId();

		// Set the calculated ID as the value of the sourceIdField
		nextId = (nextId == null) ? 1L : nextId + 1;
		source_id.setValue(String.valueOf(nextId));

		backbtn.addClickListener(e -> {
			UI.getCurrent().navigate(AssetInfo.class);
		});

		savebtn.addClickListener(e -> {
			saveDetails();
		});
		savebtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

//		skip.addClickListener(e -> {
//			UI.getCurrent().navigate(Output.class);
//		});
//		skip.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
//		skip.setVisible(false);

		connect.addClickListener(e -> {
			Connection();
		UI.getCurrent().navigate(TagMapping.class);
		});
		connect.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		connect.setVisible(false);

		HorizontalLayout buttonLayout = new HorizontalLayout(backbtn, savebtn,connect);
		buttonLayout.setSpacing(true);
		HorizontalLayout h2 = new HorizontalLayout(pollInterval, timeFormat);
		h2.setSpacing(true);
		HorizontalLayout h3 = new HorizontalLayout(repInterval, timeFormat2);
		h3.setSpacing(true);
		VerticalLayout v1 = new VerticalLayout(source_id, slave_id, com_port, baud_rate, data_bits, stop_bits, parity, h2, h3, buttonLayout);
		v1.setPadding(true);
		// v1.setMargin(true);
		add(v1);
	}

	private void Connection() {
		SerialConnection con = null;
		try{
              SerialParameters params = new SerialParameters();
			  params.setPortName(com_port.getValue());
			  params.setBaudRate(baud_rate.getValue());
			  params.setDatabits(data_bits.getValue());
			  params.setParity(parity.getValue());
			  params.setStopbits(stop_bits.getValue()); // only for hubli
			  params.setEncoding("RTU");
			  params.setEcho(false);
			  con = new SerialConnection(params);
			  
			  if (!con.isOpen()) {
		            con.open();
		            Notification.show("Modbus RTU Device Connected Successfully").setDuration(3000);
		            Notification.show("Reading.........").setDuration(3000);
		            UI.getCurrent().navigate(TagMapping.class);
		            TagMapping tagMapping = new TagMapping(null);
		            tagMapping.Communication();
		            con.close();
		        } else {
		            Notification.show("Failed to connect to Modbus RTU Device").setDuration(3000);
		            con.close();
		        }
		    } catch (Exception e) {
		        System.out.println(e);
		        Notification.show("An error occurred while trying to connect to Modbus RTU Device").setDuration(3000);
		        con.close();
		    }
	}

	private void setComPortValue() {
		SerialPort[] ports = SerialPort.getCommPorts();
		for (SerialPort port : ports) {
			com_port.setItems(port.getSystemPortName());
		}
	}

	private void saveDetails() {
		Details details = new Details();
		// details.setSource_id(Long.valueOf(source_id.getValue()));
		details.setCom_port(com_port.getValue());
		details.setBaud_rate(baud_rate.getValue());
		details.setData_bits(data_bits.getValue());
		details.setStop_bits(stop_bits.getValue());
		details.setParity(parity.getValue());
		details.setPolling_interval(pollInterval.getValue());
		details.setTime_format(timeFormat.getValue());
		details.setReport_interval(repInterval.getValue());
		details.setSet_time_format(timeFormat2.getValue());
		details.setSlave_id(Integer.parseInt(slave_id.getValue()));
//		details.setStarting_address(Integer.parseInt(starting_address.getValue()));
//		details.setReq_quantity(Integer.parseInt(req_quantity.getValue()));
		configRepository.save(details);
//		skip.setVisible(true);
		connect.setVisible(true);
		Notification.show("Details Added! You can connect now").setDuration(5000);
		
		clearForm();

	}

	private void clearForm() {
		UI.getCurrent().getPage().executeJs("setTimeout(function() { location.reload(); }, 20000);");
	}
}
