package com.project.ams.vaadin;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Style.JustifyContent;
import com.vaadin.flow.router.Route;

import jakarta.annotation.PostConstruct;

@Route(value = "/dashboard", layout = MainLayout.class)
public class Dashboard extends VerticalLayout {

	Upload upload = new Upload();
	Button decode = new Button("Decode QR");
	TextField sourceId = new TextField("Source ID");
	TextField sourceName = new TextField("Source Name");
	TextField applicationName = new TextField("Application Name");
	TextField longitude = new TextField("Longitude");
	TextField latitude = new TextField("Latitude");
	TextField locationName = new TextField("Location Name");
	TextField protocolType = new TextField("Protocol Type");
	TextField installDate = new TextField("Install Date");
	TextField modifiedDate = new TextField("Modified Date");

	@PostConstruct
	public void init() {

		HorizontalLayout navbar = new HorizontalLayout();
		navbar.setWidthFull();
		H3 heading = new H3("  Dashboard  ");
		navbar.add(heading);
		Hr hr = new Hr();
		hr.setHeight("5px");
//		add(navbar, hr);

		MemoryBuffer buffer = new MemoryBuffer();
		upload.setReceiver(buffer);
		upload.setMaxFiles(1);
		upload.setAcceptedFileTypes("image/png");
		upload.setMaxFileSize(1000000);
		upload.addSucceededListener(event -> {
			// Handle successful upload if needed
			Notification.show("File uploaded successfully");
		});
		upload.addFailedListener(e -> {
			Notification.show("Failed due to " + e.getReason().getMessage());
		});

		decode.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		decode.addClickListener(e -> {
			try {
				decodeQR();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});

		add(navbar, hr, upload, decode);

	}

	private void decodeQR() throws IOException {
		InputStream inputStream = ((MemoryBuffer) upload.getReceiver()).getInputStream();
		byte[] fileBytes = inputStream.readAllBytes();

		// Use a QR code decoding library to extract data from the QR code
		String decodedData = decodeQRCode(fileBytes);
		upload.setVisible(false);
		decode.setVisible(false);

		sourceId.setReadOnly(true);
		sourceName.setReadOnly(true);
		applicationName.setReadOnly(true);
		longitude.setReadOnly(true);
		latitude.setReadOnly(true);
		locationName.setReadOnly(true);
		protocolType.setReadOnly(true);
		installDate.setReadOnly(true);
		modifiedDate.setReadOnly(true);

		Image image = new Image("images/mfm.png", "Alt text");
		image.setHeight("100px");
		image.setWidth("100px");
		image.setTitle("MultiFunction Energy Meter (MFM)");
		VerticalLayout v2 = new VerticalLayout();
		H3 details = new H3("Asset details:");
		v2.add(details, image);
		FormLayout h1 = new FormLayout();
		h1.add(sourceId, sourceName, applicationName, longitude, latitude, locationName, protocolType, installDate, modifiedDate);
		h1.setResponsiveSteps(// Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if the layout's width exceeds 320px
                new ResponsiveStep("320px", 2),
                // Use three columns, if the layout's width exceeds 500px
                new ResponsiveStep("500px", 3));
//		h1.setSpacing(true);
//		FormLayout h2 = new FormLayout();
//		h2.add(sourceName, latitude, installDate);
////		h2.setSpacing(true);
//		FormLayout h3 = new FormLayout();
//		h3.add(applicationName, locationName, modifiedDate);
////		h3.setSpacing(true);
		VerticalLayout v1 = new VerticalLayout(h1);
//		v1.setSpacing(false);
//		v1.getThemeList().add("spacing-xl");
		v1.setSizeFull();
//		v1.setHeightFull();
//		v1.setMargin(true);
		add(v2,v1);

		Notification.show("QR code decoded successfully");
	}

	private String decodeQRCode(byte[] qrCodeBytes) {
		try {
			// Create a BinaryBitmap from the QR code bytes
			BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
					new BufferedImageLuminanceSource(ImageIO.read(new ByteArrayInputStream(qrCodeBytes)))));

			// Decode the QR code using ZXing
			Result result = new MultiFormatReader().decode(binaryBitmap);

			// Parse the JSON result using Jackson
			String jsonResult = result.getText();
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(jsonResult);

			// Set values to the corresponding text fields
			sourceId.setValue(jsonNode.path("source_id").asText(""));
			sourceName.setValue(jsonNode.path("source_name").asText(""));
			applicationName.setValue(jsonNode.path("application_name").asText(""));
			longitude.setValue(jsonNode.path("longitude").asText(""));
			latitude.setValue(jsonNode.path("latitude").asText(""));
			locationName.setValue(jsonNode.path("location_name").asText(""));
			protocolType.setValue(jsonNode.path("protocol_type").asText(""));
			installDate.setValue(jsonNode.path("install_date").asText(""));
			modifiedDate.setValue(jsonNode.path("modified_date").asText(""));

			// Return the decoded JSON data
			return jsonResult;
		} catch (IOException | NotFoundException e) {
			e.printStackTrace();
			Notification.show("Error decoding QR code");
			return "Error decoding QR code";
		}
	}
}
