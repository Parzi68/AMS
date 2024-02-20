package com.project.ams.kafka.topic.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(KafkaTopicConstant.DATA2)
                .partitions(1)
                .replicas(2)
                .config("min.insync.replicas", "1")
                .build();
    }
}
