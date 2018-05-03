package com.example.kafka.sample.embedded;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.example.kafka.sample.embedded.service.TestKafkaListener;
import java.util.Iterator;
import java.util.Map;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource("classpath:test.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
@EmbeddedKafka(topics = {"test_topic", "test_topic2"})
public class KafkaListenerTest {

  @Autowired
  private KafkaEmbedded kafkaEmbedded;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private TestKafkaListener testKafkaListener;

  @Before
  public void setUp() {
    testKafkaListener.resetCounter();
  }

  @Test
  public void sendReceiveRecord() throws Exception {
    String testKey = "test_key";
    String testData = "test_data";

    kafkaTemplate.send("test_topic", testKey, testData);

    try (Consumer<String, String> consumer = createConsumer()) {
      kafkaEmbedded.consumeFromAnEmbeddedTopic(consumer, "test_topic");
      ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, 2_000);
      Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
      ConsumerRecord<String, String> record = iterator.next();

      assertEquals(testKey, record.key());
      assertEquals(testData, record.value());
      assertFalse(iterator.hasNext());
    }

    assertEquals(1, testKafkaListener.getCounter());
  }

  private Consumer<String, String> createConsumer() {
    Map<String, Object> consumerProps =
        KafkaTestUtils.consumerProps("test-consumer", "true", kafkaEmbedded);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(
        consumerProps, new StringDeserializer(), new StringDeserializer());
    return cf.createConsumer();
  }
}