package com.project.ams.vaadin;

import com.fazecast.jSerialComm.SerialPort;
import com.project.ams.spring.Details;
import com.project.ams.spring.UserRepositoryy;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Route("/form2")
public class Form2 extends FormLayout {
    private final UserRepositoryy userRepositoryy;
    private final TextField source_id = new TextField("Source id");
    private final TextField com_port = new TextField("COM_Port");
    private final TextField baud_rate = new TextField("BAUD Rate");
    private final TextField data_bits = new TextField("Data Bits");
    private final TextField stop_bits = new TextField("Stop Bits");
    private final TextField parity = new TextField("Parity");
    private final Button backbtn = new Button("Back");
    private final Button savebtn = new Button("Save");
    private final Select<Integer> pollInterval = new Select<>();
    private final Select<Integer> repInterval = new Select<>();
    private final Select<String> timeFormat = new Select<>();
    private final Select<String> timeFormat2 = new Select<>();

    public Form2(UserRepositoryy userRepositoryy) {
        this.userRepositoryy = userRepositoryy;

        source_id.setReadOnly(true);
        source_id.setWidthFull();

        com_port.setRequiredIndicatorVisible(true);
        com_port.setErrorMessage("This field is required.");
        com_port.setWidthFull();

        baud_rate.setRequiredIndicatorVisible(true);
        baud_rate.setErrorMessage("This field is required.");
        baud_rate.setWidthFull();

        data_bits.setRequiredIndicatorVisible(true);
        data_bits.setErrorMessage("This field is required.");
        data_bits.setWidthFull();

        stop_bits.setRequiredIndicatorVisible(true);
        stop_bits.setErrorMessage("This field is required.");
        stop_bits.setWidthFull();

        parity.setRequiredIndicatorVisible(true);
        parity.setErrorMessage("This field is required.");
        parity.setHelperText("Enter True(1) or False(0)");
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

        //Setting values for Time Format
        timeFormat.setLabel("Set Time Format");
        timeFormat.setValue("Select");
        timeFormat.setItems("Seconds", "Minutes", "Hours");
        timeFormat.setRequiredIndicatorVisible(true);
        timeFormat.setErrorMessage("This field is required.");

        //Setting values for Time Format
        timeFormat2.setLabel("Set Time Format");
        timeFormat2.setValue("Select");
        timeFormat2.setItems("Seconds", "Minutes", "Hours");
        timeFormat2.setRequiredIndicatorVisible(true);
        timeFormat2.setErrorMessage("This field is required.");

        // Automatically scan and set the available COM ports
        setComPortValue();
        //com_port.setReadOnly(true);

        // Fetch the latest ID from the database and increment it by 1
        Long nextId = userRepositoryy.findMaxId();

        // Set the calculated ID as the value of the sourceIdField
        nextId = (nextId == null) ? 1L : nextId + 1;
        source_id.setValue(String.valueOf(nextId));
        backbtn.addClickListener(e -> {
            UI.getCurrent().navigate(Form1.class);
        });

        savebtn.addClickListener(e -> {
            saveDetails();
            UI.getCurrent().getPage().reload();
        });
        savebtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(backbtn,savebtn);
        buttonLayout.setSpacing(true);
        HorizontalLayout h2 = new HorizontalLayout(pollInterval, timeFormat);
        h2.setSpacing(true);
        HorizontalLayout h3 = new HorizontalLayout(repInterval,timeFormat2);
        h3.setSpacing(true);
        VerticalLayout v1 = new VerticalLayout(source_id,com_port,baud_rate,data_bits,stop_bits,parity,h2,h3,buttonLayout);
        v1.setPadding(true);
       // v1.setMargin(true);
        add(v1);
    }

    private void setComPortValue() {
        // Get the list of available serial ports using jSerialComm
        SerialPort[] serialPorts = SerialPort.getCommPorts();

        // Check if any serial ports are available
        if (serialPorts.length > 0) {
            // Use the first available serial port as the default value
            String defaultComPort = serialPorts[0].getSystemPortName();

            // Set the default COM port value in the com_port TextField
            com_port.setValue(defaultComPort);
        } else {
            // No serial ports found, you may want to handle this case accordingly
            Notification.show("No COM ports found.");
        }
    }



    private void saveDetails() {
        Details details = new Details();
        //details.setSource_id(Long.valueOf(source_id.getValue()));
        details.setCom_port(Integer.parseInt(com_port.getValue()));
        details.setBaud_rate(Integer.parseInt(baud_rate.getValue()));
        details.setData_bits(Integer.parseInt(data_bits.getValue()));
        details.setStop_bits(Integer.parseInt(stop_bits.getValue()));
        details.setParity(Boolean.parseBoolean(parity.getValue()));
        details.setPolling_interval(pollInterval.getValue());
        details.setTime_format(timeFormat.getValue());
        details.setReport_interval(repInterval.getValue());
        details.setSet_time_format(timeFormat2.getValue());
        userRepositoryy.save(details);
        Notification.show("Details Added!");

    }

//    private void clearForm() {
//        source_id.clear();
//        com_port.clear();
//        data_bits.clear();
//        baud_rate.clear();
//        stop_bits.clear();
//        parity.clear();
//        timeFormat.clear();
//        pollInterval.clear();
//        repInterval.clear();
//        timeFormat2.clear();
//
//    }
}
