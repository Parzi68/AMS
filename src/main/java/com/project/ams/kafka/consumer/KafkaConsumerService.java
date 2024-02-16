package com.project.ams.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.project.ams.kafka.topic.config.KafkaTopicConstant;

@Service
public class KafkaConsumerService {
	@KafkaListener(topics = KafkaTopicConstant.DATA2, groupId = "user-group")
	public void updateData(String data) {
		System.out.println(data);
	}
}
