package com.example.kafka.sample.embedded.config;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@PropertySource({"classpath:application.properties", "classpath:test.properties"})
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaEmbeddedConfig {

  public static final String TEST_TOPIC = "test_topic";

  @Bean
  public KafkaEmbedded kafkaEmbedded() {
    KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1, false, 1, TEST_TOPIC);
    kafkaEmbedded.setKafkaPorts(9092);
    return kafkaEmbedded;
  }
}
