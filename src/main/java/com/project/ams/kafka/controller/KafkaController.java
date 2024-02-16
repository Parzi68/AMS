package com.project.ams.kafka.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ams.kafka.producer.service.KafkaProducerService;

@RestController
@RequestMapping("/produce")
public class KafkaController {

	@Autowired
	private KafkaProducerService kafkaProducerService;
	
	@PutMapping
	public ResponseEntity updateData() throws InterruptedException {
		
		int range = 15;
		while(range > 0) {
			Thread.sleep(1000);
			kafkaProducerService.updateData(Math.random() + "," + Math.random());
			range--;
		}
		System.out.println(updateData());
		return new ResponseEntity<>(Map.of("message","Data updated"),HttpStatus.OK);
	}
	
	
}

