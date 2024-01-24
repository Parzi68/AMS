package com.project.ams.vaadin;


import com.project.ams.spring.Asset;
import com.project.ams.spring.UserRepository;
import com.project.ams.vaadin.service.JsonConverter;
import com.project.ams.vaadin.service.QRCodeGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;


@Route("/form1")
public class Form1 extends FormLayout {

    private final UserRepository userRepository;
    private final TextField source_id = new TextField("Source Id");
    private final TextField source_name = new TextField("Source Name");
    private final TextField application_Name = new TextField("Application Name");
    private final TextField longitude = new TextField("Longitude");
    private final TextField latitude = new TextField("Latitude");
    private final TextField location_name = new TextField("Location Name");
    private final TextField protocol_type = new TextField("Protocol Type");
    private final DatePicker install_date = new DatePicker("Install Date");
    private final DatePicker modified_date = new DatePicker("Modified Date");
    private final Button saveButton = new Button("Save");
    private final Button nextButton = new Button("Next");
    private final Button qrGen = new Button("Generate QR");


    public Form1(UserRepository userRepository) {
        this.userRepository = userRepository;
        source_id.setReadOnly(true);

        //making fields as required
        source_name.setRequiredIndicatorVisible(true);
        source_name.setErrorMessage("This field is required");

        application_Name.setRequiredIndicatorVisible(true);
        application_Name.setErrorMessage("This field is required");

        longitude.setRequiredIndicatorVisible(true);
        longitude.setErrorMessage("This field is required");

        latitude.setRequiredIndicatorVisible(true);
        latitude.setErrorMessage("This field is required");

        protocol_type.setRequiredIndicatorVisible(true);
        protocol_type.setErrorMessage("This field is required");

        location_name.setRequiredIndicatorVisible(true);
        location_name.setErrorMessage("This field is required");

        install_date.setRequiredIndicatorVisible(true);
        install_date.setErrorMessage("This field is required");

        modified_date.setRequiredIndicatorVisible(true);
        modified_date.setErrorMessage("This field is required");
        // Fetch the latest ID from the database and increment it by 1
        Long nextId = userRepository.findMaxId() + 1;

        // Set the calculated ID as the value of the sourceIdField
        source_id.setValue(String.valueOf(nextId));
        saveButton.addClickListener(e -> saveAsset());
        saveButton.setAutofocus(true);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextButton.setAutofocus(true);

        // Initially, hide the "Generate QR" button
        qrGen.setVisible(false);

        qrGen.addClickListener(e -> generateQR());

        HorizontalLayout h1 = new HorizontalLayout(saveButton,nextButton,qrGen);
        h1.setSpacing(true);
        VerticalLayout v1 = new VerticalLayout(source_id,source_name,application_Name,longitude,latitude,location_name,protocol_type,install_date,modified_date,h1);
        add(v1);

    }

    private void saveAsset() {
        Asset  asset = new Asset();
        asset.setSource_name(source_name.getValue());
        asset.setApplication_name(application_Name.getValue());
        asset.setLongitude(Integer.parseInt(longitude.getValue()));
        asset.setLatitude(Integer.parseInt(latitude.getValue()));
        asset.setLocation_name(location_name.getValue());
        asset.setProtocol_type(protocol_type.getValue());
        asset.setInstall_date(install_date.getValue());
        asset.setModified_date(modified_date.getValue());
        userRepository.save(asset);
        // Show the "Generate QR" button after saving
        qrGen.setVisible(true);

        Notification.show("Asset saved successfully");

    }
    private void generateQR() {
        Asset asset = new Asset();
        asset.setSource_id(Long.valueOf(source_id.getValue()));
        asset.setSource_name(source_name.getValue());
        asset.setApplication_name(application_Name.getValue());
        asset.setLongitude(Integer.parseInt(longitude.getValue()));
        asset.setLatitude(Integer.parseInt(latitude.getValue()));
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

        // Create a button for downloading the QR code
        // Create a button for downloading the QR code

        //downloadButton.getElement().getThemeList().add("primary");

        // Open the browser's download prompt for the user to choose the download location
        Anchor anchor = new Anchor(resource, "");
        anchor.getElement().getThemeList().add("button");
        anchor.getElement().setAttribute("download", true);

        // You can customize the button text accordingly
        anchor.setText("Download QR Code");

        // Add the anchor to the layout
        add(anchor);

        // Add the resource to UI for download
//        qrGen.getElement().getThemeList().add("primary");
//        qrGen.getElement().getThemeList().add("download");
//        qrGen.getElement().setAttribute("download", true);
//        qrGen.getElement().setAttribute("href", resource);

//        Notification.show("QR Code generated. Click the button to download.");
        clearForm();
    }

    private void clearForm() {
        source_id.clear();
        source_name.clear();
        application_Name.clear();
        longitude.clear();
        latitude.clear();
        location_name.clear();
        protocol_type.clear();
        install_date.clear();
        modified_date.clear();
        // Hide the "Generate QR" button after clearing the form
        qrGen.setVisible(false);

//        qrGen.getElement().executeJs("location.reload()");
    }
}

