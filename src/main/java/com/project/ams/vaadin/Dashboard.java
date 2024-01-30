package com.project.ams.vaadin;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.project.ams.spring.UserRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Route("Dashboard")
public class Dashboard extends VerticalLayout {
    private final UserRepository userRepository;
    private final Upload upload = new Upload();
    private final Button decode = new Button("Decode QR");
    private final TextArea output = new TextArea("Decoded data");
    Div res = new Div();

    public Dashboard(UserRepository userRepository) {
        this.userRepository = userRepository;
        output.setReadOnly(true);

        MemoryBuffer buffer = new MemoryBuffer();
        upload.setReceiver(buffer);
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("image/png");
        upload.setMaxFileSize(1000);
        upload.addSucceededListener(event -> {
            // Handle successful upload if needed
            Notification.show("File uploaded successfully");
        });
        upload.addFileRejectedListener(e -> {
            Paragraph component = new Paragraph();
            showOutput(e.getErrorMessage(), component, res);
        });
        decode.addClickListener(e -> {
            try {
                decodeQR();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        add(upload,decode,output,res);

    }

    private void showOutput(String text, Component content,
                            HasComponents outputContainer) {
        outputContainer.removeAll();
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        p.getElement().getStyle().set("color","red");
        upload.getElement().getStyle().set("background","pink");

        outputContainer.add(p);
        outputContainer.add(content);
    }


    private void decodeQR() throws IOException {
        InputStream inputStream = ((MemoryBuffer) upload.getReceiver()).getInputStream();
        byte[] fileBytes = inputStream.readAllBytes();

        // Use a QR code decoding library to extract data from the QR code
        String decodedData = decodeQRCode(fileBytes);

        // Display the decoded data in the text field
        output.setValue(decodedData);

        Notification.show("QR code decoded successfully");
    }
    private String decodeQRCode(byte[] qrCodeBytes) {
        try {
            // Create a BinaryBitmap from the QR code bytes
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new ByteArrayInputStream(qrCodeBytes)))));

            // Decode the QR code using Zxing
            Result result = new MultiFormatReader().decode(binaryBitmap);

            // Return the decoded text
            return result.getText();
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
            return "Error decoding QR code";
        }
    }
}


