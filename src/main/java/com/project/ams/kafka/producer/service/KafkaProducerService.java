package com.project.ams.kafka.producer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.project.ams.kafka.topic.config.KafkaTopicConstant;
import com.vaadin.flow.component.page.Push;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public boolean updateData(String data) {
        kafkaTemplate.send(KafkaTopicConstant.DATA2, data);
        return true;
    }

}
