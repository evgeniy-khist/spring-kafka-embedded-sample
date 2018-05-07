package com.example.kafka.sample.embedded.config;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("kafka")
public class KafkaTestBase {

  public static final KafkaEmbedded KAFKA_EMBEDDED = createKafkaEmbedded();

  private static KafkaEmbedded createKafkaEmbedded() {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(KafkaEmbeddedConfig.class);
    KafkaEmbedded kafkaEmbedded = context.getBean(KafkaEmbedded.class);
    Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    return kafkaEmbedded;
  }
}
