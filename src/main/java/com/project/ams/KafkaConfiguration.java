package com.project.ams;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
@EnableKafka
@Configuration
public class KafkaConfiguration {

	
	  @Bean
	    public ProducerFactory<String, String> producerFactory() {
	        Map<String, Object> config = new HashMap<>();

	        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "0.0.0.0:9092,0.0.0.0:9093,0.0.0.0:9094");
//	        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "64.227.170.3:9092,64.227.170.3:9093,64.227.170.3:9094");
	        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	       /// config.put(ProducerConfig., StringSerializer.class);

	        return new DefaultKafkaProducerFactory<>(config);
	    }


	    @Bean
	    public KafkaTemplate<String, String> kafkaTemplate() {
	        return new KafkaTemplate<>(producerFactory());
	    }
}
