package com.project.ams.vaadin;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.kafka.producer.service.KafkaProducerService;
import com.project.ams.spring.Asset;
import com.project.ams.spring.ConfigRepository;
import com.project.ams.spring.Details;
import com.project.ams.spring.MapRepository;
import com.project.ams.spring.MappingData;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

@Route(value = "/tagMapping", layout = MainLayout.class)
public class TagMapping extends VerticalLayout implements HasUrlParameter<String> {

	private static final String ROUTE_NAME = "tagMapping";

	@Autowired
	private KafkaProducerService kafkaProducerService;

	@Autowired
	private MapRepository mapRepository;
	@Autowired
	private ConfigRepository configRepository;
	MappingData mappingData = new MappingData();
	TextField source_id = new TextField("Source Id");
	TextField reg_name = new TextField("Register Name");
	TextField reg_address = new TextField("Register Address");
	TextField reg_length = new TextField("Register Length");
	Select<String> reg_type = new Select<>();
	Select<Integer> multiplier = new Select<>();
	TextField element_name = new TextField("Element Name");
	Select<String> point_type = new Select<>();
	Button backbtn = new Button("Back");
	Button submitbtn = new Button("Submit");
	Button connbtn = new Button("Connect");
	Button producebtn = new Button("Read Data");
	Button dashboard = new Button("View Dashboard");
	Button stopbtn = new Button("Stop Producing");
	volatile boolean stopRequested = false;
	Button resetProd = new Button("Reset production");

	Grid<MappingData> grid = new Grid<>(MappingData.class, false);
	List<MappingData> tagList;

	Long main_id;

//	@PostConstruct
	public void init(String param) {
		main_id = Long.parseLong(param);
		this.tagList = mapRepository.findAll();

		setSizeFull();

		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Source Tag Mapping Form  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");
		add(navbar, hr);

		source_id.setReadOnly(true);
		source_id.setWidthFull();

		reg_name.setRequiredIndicatorVisible(true);
		reg_name.setErrorMessage("This field is required");
		reg_name.setRequired(true);
		reg_name.setWidthFull();

		reg_address.setRequiredIndicatorVisible(true);
		reg_address.setErrorMessage("This field is required");
		reg_address.setRequired(true);
		reg_address.setWidthFull();

		reg_length.setRequiredIndicatorVisible(true);
		reg_length.setErrorMessage("This field is required");
		reg_length.setRequired(true);
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
		element_name.setRequired(true);
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
		
		if (!param.equals("0")) {
			for (MappingData t1 : mapRepository.tag_list(main_id)) {
				// source_id.setValue(Integer.parseInt(a1.getSource_id()));
				source_id.setValue(String.valueOf(t1.getSource_id()));
				reg_name.setValue(t1.getReg_name());
				reg_address.setValue(String.valueOf(t1.getReg_address()));
				reg_length.setValue(String.valueOf(t1.getReg_length()));
				reg_type.setValue(t1.getReg_type());
				multiplier.setValue(t1.getMultiplier());
				element_name.setValue(t1.getElement_name());
				point_type.setValue(t1.getPoint_type());
			}
		}


		backbtn.addClickListener(e -> {
			UI.getCurrent().navigate(RTUConfig.class);
		});

		submitbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		submitbtn.addClickListener(e -> {
			SaveTags(param);
			// Communication(0, null);
		});

		connbtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		connbtn.addClickListener(e -> {
//			Comm();
		});

		producebtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
		producebtn.addClickListener(e -> {
			Notification.show("Kafka Production started!!");
			Notification.show("You can see the output in the dashboard");
			producebtn.setEnabled(false);
			stopbtn.setEnabled(true);
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
		stopbtn.setEnabled(false);
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
		// Grid
		grid.removeAllColumns();
		grid.setItems(tagList);
		grid.addColumn(MappingData::getId).setHeader("ID").setAutoWidth(true).setFrozen(true);
		grid.addColumn(MappingData::getSource_id).setHeader("Source ID").setAutoWidth(true);
		grid.addColumn(MappingData::getReg_name).setHeader("Register Name").setAutoWidth(true);
		grid.addColumn(MappingData::getReg_address).setHeader("Register Address").setAutoWidth(true);
		grid.addColumn(MappingData::getReg_length).setHeader("Register Length").setAutoWidth(true);
		grid.addColumn(MappingData::getReg_type).setHeader("Register Data Type").setAutoWidth(true);
		grid.addColumn(MappingData::getMultiplier).setHeader("Multiplier").setAutoWidth(true);
		grid.addColumn(MappingData::getElement_name).setHeader("Element").setAutoWidth(true);
		grid.addColumn(MappingData::getPoint_type).setHeader("Modbus Point Type").setAutoWidth(true);

		// Add edit button column
		grid.addComponentColumn(mappingData -> {
			Button editButton = new Button("Edit");
			editButton.setIcon(new Icon(VaadinIcon.EDIT));
			editButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
			editButton.addClickListener(
					event -> UI.getCurrent().navigate(TagMapping.ROUTE_NAME + "/" + mappingData.getId()));
			return editButton;
		}).setAutoWidth(true);

		// Add delete button column
		grid.addComponentColumn(mappingData -> {
			Button deleteButton = new Button("Delete");
			deleteButton.addClickListener(event -> deleteAsset(mappingData.getId()));
			deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
			deleteButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
			return deleteButton;
		}).setAutoWidth(true);
		grid.setAllRowsVisible(true);
		add(new Hr(), grid);

		
	}

	private void deleteAsset(Long tagId) {
		Dialog confirmDialog = new Dialog();
		confirmDialog.add(new H3("Confirm Delete?"), new Button("Confirm", event -> {
			mapRepository.deleteById(tagId);
			refreshGrid();
			confirmDialog.close();
		}), new Button("Cancel", event -> confirmDialog.close()));
		confirmDialog.open();
	}

	private void refreshGrid() {
		tagList = mapRepository.findAll();
		grid.setItems(tagList);
	}

	private void resetProduction() {
		stopRequested = false;
		producebtn.setEnabled(true);
		stopbtn.setVisible(true);
		stopbtn.setEnabled(false);
		resetProd.setVisible(false);
		Notification.show("You can now restart the production");
	}

	private void kafkaProduce() throws Exception {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> {
			int range = 15;
			while (range > 0 && !stopRequested) {
				try {
					Thread.sleep(1000);
					kafkaProducerService.updateData(Math.random() + "," + Math.random() + " range: " + range);
					range--;
				} catch (Exception e) {
					// Handle exceptions
				}
			}
			// Shutdown the executor after the loop completes
			executor.shutdown();
		});
	}

	public synchronized void stopProduction() {
		stopRequested = true;
		Notification.show("Production Stopped!!").setDuration(2000);
		// UI.getCurrent().getPage().executeJs("setTimeout(function() {
		// location.reload(); }, 2000);");
		producebtn.setEnabled(false);
		resetProd.setVisible(true);
		stopbtn.setVisible(false);
	}

	private void SaveTags(String param) {
		if (param.equals("0")) {
	        if (!mapRepository.check_source(reg_name.getValue())) {
	            // Create a new SourceTable object
	            MappingData st = new MappingData();
	            st.setSource_id(Integer.parseInt(source_id.getValue()));
	            st.setReg_name(reg_name.getValue());
	            st.setReg_address(Integer.parseInt(reg_address.getValue()));
	            st.setReg_length(Integer.parseInt(reg_length.getValue()));
	            st.setReg_type(reg_type.getValue());
	            st.setMultiplier(multiplier.getValue());
	            st.setElement_name(element_name.getValue());
	            st.setPoint_type(point_type.getValue());
	            // Save the source
	            mapRepository.save(st);
	            Notification.show("Tags have been saved successfully");
	            submitbtn.setEnabled(false);
	        } else {
	            Notification.show("Register Name Already Exists");
	        }
	    } else {
	        // Update the existing source
	        MappingData st = new MappingData();
	        st.setSource_id(Integer.parseInt(source_id.getValue()));
	        st.setReg_name(reg_name.getValue());
	        st.setReg_address(Integer.parseInt(reg_address.getValue()));
            st.setReg_length(Integer.parseInt(reg_length.getValue()));
            st.setReg_type(reg_type.getValue());
            st.setMultiplier(multiplier.getValue());
            st.setElement_name(element_name.getValue());
            st.setPoint_type(point_type.getValue());
	        st.setId(main_id);

	        // Save the updated source
	        mapRepository.save(st);
	        Notification.show("Source has been updated successfully");
	    }


	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		init(parameter);
	}

//	public void Comm() {
//		// Use getter methods from Details to access the required data
//		String comPort = configRepository.comPort();
//		String baudRate = configRepository.baudRate();
//		String dataBits = configRepository.dataBits();
//		String parity = configRepository.parity();
//		String stopBits = configRepository.stopBits();
//		int regAddress = mapRepository.RegAddress();
//		int regLength = mapRepository.RegLength();
//
//		System.out.println(comPort + baudRate + dataBits + parity + stopBits + regAddress + regLength); // Debugging
//																										// line....
//		SerialConnection con = null;
//		try {
//			SerialParameters params = new SerialParameters();
//			params.setPortName(comPort);
//			params.setBaudRate(baudRate);
//			params.setDatabits(dataBits);
//			params.setParity(parity);
//			params.setStopbits(stopBits);
//			params.setEncoding("RTU");
//			params.setEcho(false);
//			con = new SerialConnection(params);
//
//			if (!con.isOpen()) {
//				con.open();
//				Notification.show("Modbus RTU Device Connected Successfully").setDuration(3000);
//				// UI.getCurrent().navigate(TagMapping.class);
//				Notification.show("Reading....").setDuration(3000);				
//			}
//
////			String getRes = getDataLNT(4, regAddress, regLength, "", con);
////			System.out.println(getRes);
////			
////			kafkaProducerService.updateData(getRes);
//
//		} catch (Exception e) {
//			System.out.println(e);
//			Notification.show("An error occurred while trying to connect to Modbus RTU Device").setDuration(3000);
//		}
//}
//
//	public static String getDataLNT(int SlaveId, int reference, int register, String headVal, SerialConnection con) {
//
//		ModbusSerialTransaction trans = null;
//		ReadMultipleRegistersRequest req = null;
//		ReadMultipleRegistersResponse res = null;
//
//		String getResponse = "";
//		String response = "";
//
//		int slaveID = SlaveId, ref = reference, registers = register;
//
//		try {
//
//			ModbusCoupler.getReference().setUnitID(slaveID);
//			// ModbusCoupler.getReference().setMaster(false);
//
//			req = new ReadMultipleRegistersRequest(ref, registers);
//			req.setUnitID(slaveID);
//			req.setHeadless();
//			trans = new ModbusSerialTransaction(con);
//			System.out.println("Reading............");
//
//			System.out.println("Sending request  -----slaveid--" + slaveID + "--Reference--" + ref + "--" + registers
//					+ "-----Value--" + req.getHexMessage());
//
//			trans.setRequest(req);
//			trans.execute();
//			res = (ReadMultipleRegistersResponse) trans.getResponse();
//			if (res == null) {
//
//				boolean anyLeft = true;
//				int k = 0;
//				while (anyLeft == true) {
//
//					trans.setRequest(req);
//					trans.execute();
//					res = (ReadMultipleRegistersResponse) trans.getResponse();
//					if (res != null) {
//						anyLeft = false;
//					}
//					k++;
//
//					if (slaveID == 1) {
//						if (k == 60) {
//							anyLeft = false;
//
//						}
//					} else {
//						if (k == 30) {
//							anyLeft = false;
//
//						}
//
//					}
//				}
//
//			}
//			if (res != null) {
//				response = res.getHexMessage();
//				getResponse = res.getHexMessage();
//				getResponse = getResponse.replaceAll(" ", "");
//
//				// if( !peUtil.isNullString(headVal)) {
//				getResponse = getResponse.substring(6, getResponse.length());
//				// }
//
//				con.close();
//			}
//
//			// Perform hex to float conversion
//			float floatValue = hexToFloat(getResponse);
//			// System.out.println("Hex Value: " + getResponse);
//			System.out.println("Response from meter: " + response);
//			System.out.println("Response in Float - " + floatValue);
//
//		}
//
//		catch (Exception ex) {
//			System.out.println("Reading Error - " + ex);
//
//		}
//
//		System.out.println("Response in hexadecimal - " + getResponse);
//
//		return getResponse;
//
//	}
//
//	public static float hexToFloat(String hex) {
//		if (hex.length() == 8) {
//			hex = hex.substring(4) + hex.substring(0, 4);
//		}
//		long longBits = Long.parseLong(hex, 16);
//		return Float.intBitsToFloat((int) longBits);
//	}
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
