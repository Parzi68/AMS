package com.project.ams;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller;

import com.project.ams.spring.Repository.ConfigRepository;
import com.project.ams.spring.Repository.MapRepository;
import com.project.ams.spring.Repository.MeterRepository;

import jakarta.annotation.PostConstruct;

@Controller
public class ControllerFile {
	
	@Autowired
	private MeterRepository meterRepository;
	
	@Autowired
	private ConfigRepository configRepository;
	
	@Autowired
	private MapRepository mapRepository;
	
	@PostConstruct
	public void init() {
		ReadData rtu = new ReadData(meterRepository,configRepository,mapRepository);
		Thread t1 = new Thread(rtu,"rtu service");
		t1.start();
	}
}