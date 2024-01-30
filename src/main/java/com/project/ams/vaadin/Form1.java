package com.project.ams.vaadin;


import com.project.ams.spring.Asset;
import com.project.ams.spring.UserRepository;
import com.project.ams.vaadin.service.JsonConverter;
import com.project.ams.vaadin.service.QRCodeGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;


@Route("")
@PageTitle("Asset info")
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
    private final Anchor downloadLink = new Anchor();

    private final Button previewbtn = new Button("Decode QR");


    public Form1(UserRepository userRepository) {
        this.userRepository = userRepository;
        source_id.setReadOnly(true);
        source_id.setWidthFull();

        //making fields as required
        source_name.setRequiredIndicatorVisible(true);
        source_name.setErrorMessage("This field is required");
        source_name.setWidthFull();

        application_Name.setRequiredIndicatorVisible(true);
        application_Name.setErrorMessage("This field is required");
        application_Name.setWidthFull();

        longitude.setRequiredIndicatorVisible(true);
        longitude.setErrorMessage("This field is required");
        longitude.setWidthFull();

        latitude.setRequiredIndicatorVisible(true);
        latitude.setErrorMessage("This field is required");
        latitude.setWidthFull();

        protocol_type.setRequiredIndicatorVisible(true);
        protocol_type.setErrorMessage("This field is required");
        protocol_type.setWidthFull();

        location_name.setRequiredIndicatorVisible(true);
        location_name.setErrorMessage("This field is required");
        location_name.setWidthFull();

        install_date.setRequiredIndicatorVisible(true);
        install_date.setErrorMessage("This field is required");
        install_date.setWidthFull();

        modified_date.setRequiredIndicatorVisible(true);
        modified_date.setErrorMessage("This field is required");
        modified_date.setWidthFull();
        // Fetch the latest ID from the database and increment it by 1
        Long nextId = userRepository.findMaxId();

        // If no records exist, set nextId to 1, otherwise increment the found max ID by 1
        nextId = (nextId == null) ? 1L : nextId + 1;

        // Set the calculated ID as the value of the sourceIdField
        source_id.setValue(String.valueOf(nextId));

        saveButton.addClickListener(e -> saveAsset());
        saveButton.setAutofocus(true);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        nextButton.setAutofocus(true);
        nextButton.addClickListener(e -> {
            UI.getCurrent().navigate(Form2.class);
        });
        // Initially, hide the "Generate QR" button
        qrGen.setVisible(false);
        qrGen.addThemeVariants(ButtonVariant.LUMO_ERROR);
        qrGen.setAutofocus(true);
        qrGen.addClickListener(e -> generateQR());

        previewbtn.setVisible(false);
        previewbtn.addClickListener(e -> {
           UI.getCurrent().navigate(Dashboard.class);
        });

        HorizontalLayout h1 = new HorizontalLayout(saveButton,nextButton,qrGen,downloadLink,previewbtn);
        // Create an Anchor for downloading the QR Code
        downloadLink.getElement().getThemeList().add("button");
        downloadLink.getElement().setAttribute("download", true);
        downloadLink.add(new Button("Download QR Code"));
        downloadLink.setVisible(false); // Initially hide the anchor

        h1.add(downloadLink); // Add the anchor to the layout
        h1.setSpacing(true);
        VerticalLayout v1 = new VerticalLayout(source_id,source_name,application_Name,longitude,latitude,location_name,protocol_type,install_date,modified_date,h1);
        v1.setPadding(true);
        //v1.setMargin(true);
        add(v1);

    }

    private void saveAsset() {
        Asset asset = new Asset();
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
        previewbtn.setVisible(true);

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

        // Set the resource to the Anchor for downloading
        downloadLink.setHref(resource);

        // Show the anchor for downloading
        downloadLink.setVisible(true);
        previewbtn.setVisible(true);


        clearForm();
    }


    private void clearForm() {
        UI.getCurrent().getPage().executeJs("setTimeout(function() { location.reload(); }, 20000);");

        // Hide the "Generate QR" button after clearing the form
//        qrGen.setVisible(false);
//        previewbtn.setVisible(false);

//        qrGen.getElement().executeJs("location.reload()");
    }
}

