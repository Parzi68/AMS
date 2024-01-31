package com.project.ams.vaadin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Route("Dashboard")
public class Dashboard extends VerticalLayout {

    private final Upload upload = new Upload();
    private final Button decode = new Button("Decode QR");
    private final TextField sourceId = new TextField("Source ID");
    private final TextField sourceName = new TextField("Source Name");
    private final TextField applicationName = new TextField("Application Name");
    private final TextField longitude = new TextField("Longitude");
    private final TextField latitude = new TextField("Latitude");
    private final TextField locationName = new TextField("Location Name");
    private final TextField protocolType = new TextField("Protocol Type");
    private final TextField installDate = new TextField("Install Date");
    private final TextField modifiedDate = new TextField("Modified Date");


    public Dashboard() {

        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();
        H1 heading = new H1("  Dashboard  ");
        navbar.add(heading);
        Hr hr = new Hr();
        hr.setHeight("5px");
        add(navbar,hr);

        MemoryBuffer buffer = new MemoryBuffer();
        upload.setReceiver(buffer);
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("image/png");
        upload.setMaxFileSize(1000);
        upload.addSucceededListener(event -> {
            // Handle successful upload if needed
            Notification.show("File uploaded successfully");
        });

        decode.addClickListener(e -> {
            try {
                decodeQR();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        add(upload,decode);

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
        image.setHeight("80px");
        image.setWidth("80px");
        image.setTitle("MultiFunction Energy Meter (MFM)");
        VerticalLayout v2 = new VerticalLayout();
        H3 details = new H3("Asset details:");
        v2.add(details,image);
        HorizontalLayout h1 = new HorizontalLayout(sourceId,longitude , protocolType);
        h1.setSpacing(true);
        HorizontalLayout h2 = new HorizontalLayout(sourceName, latitude,installDate);
        h2.setSpacing(true);
        HorizontalLayout h3 =  new HorizontalLayout(applicationName,locationName, modifiedDate);
        h3.setSpacing(true);
        VerticalLayout v1 = new VerticalLayout(h1,h2,h3);
        v1.setSpacing(false);
        v1.getThemeList().add("spacing-xl");
        v1.setWidthFull();
        v1.setHeightFull();
        v1.setAlignItems(Alignment.CENTER);
        v1.setMargin(true);
        add(v2,v1);

        Notification.show("QR code decoded successfully");
    }
    private String decodeQRCode(byte[] qrCodeBytes) {
        try {
            // Create a BinaryBitmap from the QR code bytes
            BinaryBitmap binaryBitmap = new BinaryBitmap(
                    new HybridBinarizer(new BufferedImageLuminanceSource(
                            ImageIO.read(new ByteArrayInputStream(qrCodeBytes))
                    ))
            );

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


