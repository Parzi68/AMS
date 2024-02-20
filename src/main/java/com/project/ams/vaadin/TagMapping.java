package com.project.ams.vaadin;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.kafka.producer.service.KafkaProducerService;
import com.project.ams.spring.ConfigRepository;
import com.project.ams.spring.Details;
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

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

@Route(value = "/tagMapping", layout = MainLayout.class)
public class TagMapping extends VerticalLayout {

	@Autowired
	private KafkaProducerService kafkaProducerService;

	private MapRepository mapRepository;
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
	private final Button connbtn = new Button("Connect");
	private final Button producebtn = new Button("Read Data");
	private final Button dashboard = new Button("View Dashboard");
	private final Button stopbtn = new Button("Stop Producing");
	private volatile boolean stopRequested = false;
	private final Button resetProd = new Button("Reset production");
	private ConfigRepository configRepository;

	private Grid<MappingData> grid = new Grid<>(MappingData.class);
	private ListDataProvider<MappingData> dataProvider;

	public TagMapping(MapRepository mapRepository, ConfigRepository configRepository) {
		this.mapRepository = mapRepository;
		this.configRepository = configRepository;
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
		submitbtn.addClickListener(e -> {
			SaveTags();
			// Communication(0, null);
		});

		connbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		connbtn.addClickListener(e -> {
			Comm();
		});

		producebtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
		producebtn.addClickListener(e -> {
			Notification.show("Kafka Production started!!");
			Notification.show("You can see the output in the dashboard");
			producebtn.setEnabled(false);
			dashboard.focus();
			try {
				kafkaProduce();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		dashboard.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
		dashboard.addClickListener(e -> {
			UI.getCurrent().getPage().open("http://localhost:3000/goto/Bf_1I7TSR?orgId=1", "Grafana Dashboard");
		});

		stopbtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
		stopbtn.addClickListener(e -> {
			stopProduction();
		});

		resetProd.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);
		resetProd.setVisible(false);
		resetProd.addClickListener(e -> {
			resetProduction();
		});

		HorizontalLayout buttonLayout = new HorizontalLayout(backbtn, submitbtn, connbtn, producebtn, dashboard,
				stopbtn, resetProd);
		VerticalLayout v1 = new VerticalLayout(source_id, reg_name, reg_address, reg_length);
		VerticalLayout v2 = new VerticalLayout(reg_type, multiplier, element_name, point_type);
		HorizontalLayout form = new HorizontalLayout(v1, v2);
		form.setSpacing(true);
		form.setWidthFull();
		// buttonLayout.setSizeFull();
		add(form, buttonLayout);
		grid.removeAllColumns();
		grid.addColumn(MappingData::getSource_id).setHeader("Source Id");
		grid.addColumn(MappingData::getReg_name).setHeader("Register Name");
		grid.addColumn(MappingData::getReg_address).setHeader("Register Address");
		grid.addColumn(MappingData::getReg_length).setHeader("Register Length");
		grid.addColumn(MappingData::getReg_type).setHeader("Register Data Type");
		grid.addColumn(MappingData::getMultiplier).setHeader("Multiplier");
		grid.addColumn(MappingData::getElement_name).setHeader("Element");
		grid.addColumn(MappingData::getPoint_type).setHeader("Modbus Point Type");

		List<MappingData> list = mapRepository.findAll();
//        System.out.println("Retrieved data from repository: " + list); // Debugging line
		dataProvider = new ListDataProvider<>(list);
		grid.setDataProvider(dataProvider);
		grid.setAllRowsVisible(true);
		add(new Hr(), grid);

	}

	private void resetProduction() {
		stopRequested = false;
		producebtn.setEnabled(true);
		stopbtn.setVisible(true);
		resetProd.setVisible(false);
		Notification.show("You can now restart the production");
	}

	private void kafkaProduce() throws Exception {

		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> {
			int range = 50;
//	        while (range > 0 && !stopRequested) {
//	            try {
//	                Thread.sleep(1000);
//	                kafkaProducerService.updateData(Math.random() + "," + Math.random() + " range: " + range);
//	                range--;
//	                
//	            }

			while (!stopRequested) {
				try {
					Comm();
				}

				catch (Exception e) {
					// Handle exceptions
				}
			}
			executor.shutdown();
			if (range == 1) {
				producebtn.setEnabled(true);
				Notification.show("Production Completed!!");
			}
		});

	}

	public synchronized void stopProduction() {
		stopRequested = true;
		producebtn.setEnabled(stopRequested);
		Notification.show("Production Stopped!!").setDuration(2000);
		// UI.getCurrent().getPage().executeJs("setTimeout(function() {
		// location.reload(); }, 2000);");
		producebtn.setEnabled(false);
		resetProd.setVisible(true);
		stopbtn.setVisible(false);
	}

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

	public void Comm() {
		// Use getter methods from Details to access the required data
		String comPort = configRepository.comPort();
		String baudRate = configRepository.baudRate();
		String dataBits = configRepository.dataBits();
		String parity = configRepository.parity();
		String stopBits = configRepository.stopBits();
		int regAddress = mapRepository.RegAddress();
		int regLength = mapRepository.RegLength();

		System.out.println(comPort + baudRate + dataBits + parity + stopBits + regAddress + regLength); // Debugging
																										// line....
		SerialConnection con = null;
		try {
			SerialParameters params = new SerialParameters();
			params.setPortName(comPort);
			params.setBaudRate(baudRate);
			params.setDatabits(dataBits);
			params.setParity(parity);
			params.setStopbits(stopBits);
			params.setEncoding("RTU");
			params.setEcho(false);
			con = new SerialConnection(params);

			if (!con.isOpen()) {
				con.open();
				Notification.show("Modbus RTU Device Connected Successfully").setDuration(3000);
				// UI.getCurrent().navigate(TagMapping.class);
				Notification.show("Reading....").setDuration(3000);
				
			}

			String getRes = getDataLNT(4, regAddress, regLength, "", con);
			System.out.println(getRes);
			
			kafkaProducerService.updateData(getRes);

		} catch (Exception e) {
			System.out.println(e);
			Notification.show("An error occurred while trying to connect to Modbus RTU Device").setDuration(3000);
		}
	}

	public static String getDataLNT(int SlaveId, int reference, int register, String headVal, SerialConnection con) {

		ModbusSerialTransaction trans = null;
		ReadMultipleRegistersRequest req = null;
		ReadMultipleRegistersResponse res = null;

		String getResponse = "";
		String response = "";

		int slaveID = SlaveId, ref = reference, registers = register;

		try {

			ModbusCoupler.getReference().setUnitID(slaveID);
			// ModbusCoupler.getReference().setMaster(false);

			req = new ReadMultipleRegistersRequest(ref, registers);
			req.setUnitID(slaveID);
			req.setHeadless();
			trans = new ModbusSerialTransaction(con);
			System.out.println("Reading............");

			System.out.println("Sending request  -----slaveid--" + slaveID + "--Reference--" + ref + "--" + registers
					+ "-----Value--" + req.getHexMessage());

			trans.setRequest(req);
			trans.execute();
			res = (ReadMultipleRegistersResponse) trans.getResponse();
			if (res == null) {

				boolean anyLeft = true;
				int k = 0;
				while (anyLeft == true) {

					trans.setRequest(req);
					trans.execute();
					res = (ReadMultipleRegistersResponse) trans.getResponse();
					if (res != null) {
						anyLeft = false;
					}
					k++;

					if (slaveID == 1) {
						if (k == 60) {
							anyLeft = false;

						}
					} else {
						if (k == 30) {
							anyLeft = false;

						}

					}
				}

			}
			if (res != null) {
				response = res.getHexMessage();
				getResponse = res.getHexMessage();
				getResponse = getResponse.replaceAll(" ", "");

				// if( !peUtil.isNullString(headVal)) {
				getResponse = getResponse.substring(6, getResponse.length());
				// }

				con.close();
			}

			// Perform hex to float conversion
			float floatValue = hexToFloat(getResponse);
			// System.out.println("Hex Value: " + getResponse);
			System.out.println("Response from meter: " + response);
			System.out.println("Response in Float - " + floatValue);

		}

		catch (Exception ex) {
			System.out.println("Reading Error - " + ex);

		}

		System.out.println("Response in hexadecimal - " + getResponse);

		return getResponse;

	}

	public static float hexToFloat(String hex) {
		if (hex.length() == 8) {
			hex = hex.substring(4) + hex.substring(0, 4);
		}
		long longBits = Long.parseLong(hex, 16);
		return Float.intBitsToFloat((int) longBits);
	}
//	public void Communication(int slave_id, SerialConnection con) {
//	
//		int regAddress = Integer.parseInt(reg_address.getValue());
//		int regLength = Integer.parseInt(reg_length.getValue());
//		
//		new ModbusRTUExample();
//		ModbusRTUExample.getDataLNT(slave_id, regAddress, regLength, " ", con);
//	
//	}

}
