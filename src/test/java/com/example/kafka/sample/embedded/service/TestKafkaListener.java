package com.example.kafka.sample.embedded.service;

import java.util.concurrent.atomic.LongAdder;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TestKafkaListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestKafkaListener.class);

  private final LongAdder counter = new LongAdder();

  @KafkaListener(topics = "test_topic")
  public void listen(ConsumerRecord<String, String> record) {
    LOGGER.info("Received {}", record);
    counter.increment();
  }

  public int getCounter() {
    return counter.intValue();
  }

  public void resetCounter() {
    counter.reset();
  }
}
