package com.example.kafka.sample.embedded;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@EmbeddedKafka(topics = {"test_topic", "test_topic2"})
public class KafkaTestBase {

  @Autowired
  protected KafkaEmbedded kafkaEmbedded;

}
