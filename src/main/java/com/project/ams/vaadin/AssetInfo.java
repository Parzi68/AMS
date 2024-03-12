package com.project.ams.vaadin;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.ams.spring.Asset;
import com.project.ams.spring.AssetRepository;
import com.project.ams.vaadin.service.JsonConverter;
import com.project.ams.vaadin.service.QRCodeGenerator;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import jakarta.annotation.PostConstruct;

@PageTitle("Asset Info")
@Route(value = "asset", layout = MainLayout.class)
public class AssetInfo extends VerticalLayout implements HasUrlParameter<String>{

	public static final String ROUTE_NAME = "asset";
	@Autowired
	private AssetRepository assetRepository;
	Asset asset = new Asset();
	TextField source_id = new TextField("Source Id");
	TextField source_name = new TextField("Source Name");
	TextField application_Name = new TextField("Application Name");
	TextField longitude = new TextField("Longitude");
	TextField latitude = new TextField("Latitude");
	TextField location_name = new TextField("Location Name");
	TextField protocol_type = new TextField("Protocol Type");
	DatePicker install_date = new DatePicker("Install Date");
	DatePicker modified_date = new DatePicker("Modified Date");
	Button saveButton = new Button("Save");
	Button nextButton = new Button("Next");
	Button qrGen = new Button("Generate QR");
	Anchor downloadLink = new Anchor();
	Button decodebtn = new Button("Decode QR");
	
	Long main_id;

//	@PostConstruct
	public void init(String param) {
		
		main_id=Long.parseLong(param);

		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Asset Source Info  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");
		add(navbar, hr);
//        VerticalLayout nav = new VerticalLayout(navbar,hr);
//        add(nav);

		source_id.setReadOnly(true);
		source_id.setWidthFull();

		// making fields as required
		source_name.setRequiredIndicatorVisible(true);
		source_name.setErrorMessage("This field is required");
		source_name.setRequired(true);
		source_name.setWidthFull();

		application_Name.setRequiredIndicatorVisible(true);
		application_Name.setErrorMessage("This field is required");
		application_Name.setRequired(true);
		application_Name.setWidthFull();

		longitude.setRequiredIndicatorVisible(true);
		longitude.setErrorMessage("Please enter valid input");
//		^[0-9. ]*$    Validation regex for longitude and latitude
		longitude.setPattern("\\d[\\d. ]*\\d");
		longitude.setRequired(true);
		longitude.setWidthFull();

		latitude.setRequiredIndicatorVisible(true);
		latitude.setErrorMessage("Please enter valid input");
		latitude.setPattern("\\d[\\d. ]*\\d");
		latitude.setRequired(true);
		latitude.setWidthFull();

		protocol_type.setRequiredIndicatorVisible(true);
		protocol_type.setErrorMessage("This field is required");
		protocol_type.setRequired(true);
		protocol_type.setWidthFull();

		location_name.setRequiredIndicatorVisible(true);
		location_name.setErrorMessage("This field is required");
		location_name.setRequired(true);
		location_name.setWidthFull();

		install_date.setRequiredIndicatorVisible(true);
		install_date.setErrorMessage("This field is required");
		install_date.setRequired(true);
		install_date.setWidthFull();

		modified_date.setRequiredIndicatorVisible(true);
		modified_date.setErrorMessage("This field is required");
		modified_date.setRequired(true);
		modified_date.setWidthFull();
		// Fetch the latest ID from the database and increment it by 1
		Long nextId = assetRepository.findMaxId();

		// If no records exist, set nextId to 1, otherwise increment the found max ID by
		// 1
		nextId = (nextId == null) ? 1L : nextId + 1;

		// Set the calculated ID as the value of the sourceIdField
		source_id.setValue(String.valueOf(nextId));

		if (!param.equals("0")) {
				for (Asset a1 : assetRepository.asset_list(main_id)) {
					// source_id.setValue(Integer.parseInt(a1.getSource_id()));
					source_id.setValue(String.valueOf(a1.getSource_id()));
					source_name.setValue(a1.getSource_name());
					application_Name.setValue(a1.getApplication_name());
					longitude.setValue(a1.getLongitude());
					latitude.setValue(a1.getLatitude());
					location_name.setValue(a1.getLocation_name());
					protocol_type.setValue(a1.getProtocol_type());
					install_date.setValue(a1.getInstall_date());
					modified_date.setValue(a1.getModified_date());
				}
		}
		saveButton.addClickListener(e -> saveAsset(param));
		saveButton.setAutofocus(true);
		saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		nextButton.setAutofocus(true);
		nextButton.addClickListener(e -> {
			UI.getCurrent().navigate(RTUConfig.class);
		});
		// Initially, hide the "Generate QR" button
		qrGen.setVisible(false);
		qrGen.addThemeVariants(ButtonVariant.LUMO_ERROR);
		qrGen.setAutofocus(true);
		qrGen.addClickListener(e -> generateQR());

		decodebtn.setVisible(false);
		decodebtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
		decodebtn.addClickListener(e -> {
			UI.getCurrent().navigate(Dashboard.class);
		});

		HorizontalLayout h1 = new HorizontalLayout(saveButton, nextButton, qrGen, downloadLink, decodebtn);
		// Create an Anchor for downloading the QR Code
		downloadLink.getElement().getThemeList().add("button");
		downloadLink.getElement().setAttribute("download", true);
		downloadLink.add(new Button("Download QR Code"));
		downloadLink.setVisible(false); // Initially hide the anchor

//		h1.add(downloadLink); // Add the anchor to the layout
		h1.setSpacing(true);
		VerticalLayout v1 = new VerticalLayout(source_id, source_name, application_Name, longitude, latitude,
				location_name, protocol_type, install_date, modified_date, h1);
		v1.setSpacing(true);
		// v1.setMargin(true);
		add(v1);

	}

	private void saveAsset(String param) {
		if (param.equals("0")) {
	        if (!assetRepository.check_source(source_name.getValue())) {
	            // Create a new SourceTable object
	            Asset st = new Asset();
	            st.setSource_id(Integer.parseInt(source_id.getValue()));
	            st.setSource_name(source_name.getValue());
	            st.setApplication_name(application_Name.getValue());
	            st.setProtocol_type(protocol_type.getValue());
	            st.setLongitude(longitude.getValue());
	            st.setLatitude(latitude.getValue());
	            st.setLocation_name(location_name.getValue());
	            st.setInstall_date(install_date.getValue());
	            st.setModified_date(modified_date.getValue());
	            // Save the source
	            assetRepository.save(st);
	            Notification.show("Source has been saved successfully");
	            saveButton.setEnabled(false);
	        } else {
	            Notification.show("Source Name Already Exists");
	        }
	    } else {
	        // Update the existing source
	        Asset st = new Asset();
	        st.setSource_id(Integer.parseInt(source_id.getValue()));
	        st.setSource_name(source_name.getValue());
	        st.setApplication_name(application_Name.getValue());
	        st.setProtocol_type(protocol_type.getValue());
	        st.setLongitude(longitude.getValue());
	        st.setLatitude(latitude.getValue());
	        st.setLocation_name(location_name.getValue());
	        st.setInstall_date(install_date.getValue());
	        st.setModified_date(modified_date.getValue());
	        st.setId(main_id);

	        // Save the updated source
	        assetRepository.save(st);
	        Notification.show("Source has been updated successfully");
	    }

	}

	private void generateQR() {
		asset.setSource_id(Integer.valueOf(source_id.getValue()));
		asset.setSource_name(source_name.getValue());
		asset.setApplication_name(application_Name.getValue());
		asset.setLongitude((longitude.getValue()));
		asset.setLatitude((latitude.getValue()));
		asset.setLocation_name(location_name.getValue());
		asset.setProtocol_type(protocol_type.getValue());
		asset.setInstall_date(install_date.getValue());
		asset.setModified_date(modified_date.getValue());

		// Convert user data to JSON
		String userDataJson = JsonConverter.convertToJson(asset);

		// Generate QR code as a byte array
		byte[] qrCodeBytes;
		try {
			qrCodeBytes = QRCodeGenerator.generateQRCode(userDataJson);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// Create a StreamResource for the generated QR code
		StreamResource resource = new StreamResource("qr.png", () -> new ByteArrayInputStream(qrCodeBytes));

		// Set the resource to the Anchor for downloading
		downloadLink.setHref(resource);

		// Show the anchor for downloading
		downloadLink.setVisible(true);
		decodebtn.setVisible(true);

		clearForm();
	}

	private void clearForm() {
		UI.getCurrent().getPage().executeJs("setTimeout(function() { location.reload(); }, 20000);");

		// Hide the "Generate QR" button after clearing the form
//        qrGen.setVisible(false);
//        previewbtn.setVisible(false);

//        qrGen.getElement().executeJs("location.reload()");
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		init(parameter);
	}
}
