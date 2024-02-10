package com.project.ams.vaadin;

import com.project.ams.spring.ConfigRepository;
import com.project.ams.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import net.wimpi.modbus.net.SerialConnection;

@Route(value = "/output", layout = MainLayout.class)
public class Output extends VerticalLayout {
	private final ConfigRepository configRepository;
	private final TextArea txt = new TextArea("Output");

	public Output(ConfigRepository configRepository) {
		this.configRepository = configRepository;
		ModbusComm();
	}

	private void ModbusComm() {
		SerialConnection con = null;

	}
}
