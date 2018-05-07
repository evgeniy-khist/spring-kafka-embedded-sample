package com.example.kafka.sample.embedded.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@PropertySource({"classpath:application.properties", "classpath:test.properties"})
public class KafkaEmbeddedConfig {

  public static final String TEST_TOPIC = "test_topic";

  @Value("${kafka.embedded.port:9092}")
  private int kafkaEmbeddedPort;

  @Bean
  public KafkaEmbedded kafkaEmbedded() {
    KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, false, 1, TEST_TOPIC);
    kafkaEmbedded.setKafkaPorts(kafkaEmbeddedPort);
    return kafkaEmbedded;
  }
}
