package com.example.kafka.sample.embedded.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@Configuration
public class KafkaEmbeddedConfig {

  public static final String TEST_TOPIC = "test_topic";

  @Bean
  public KafkaEmbedded kafkaEmbedded() {
    KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, false, 1, TEST_TOPIC);
    kafkaEmbedded.setKafkaPorts(9092);
    return kafkaEmbedded;
  }
}
