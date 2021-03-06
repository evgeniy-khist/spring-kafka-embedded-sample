package com.example.kafka.sample.embedded;

import static com.example.kafka.sample.embedded.config.KafkaEmbeddedConfig.TEST_TOPIC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.example.kafka.sample.embedded.config.KafkaTestBase;
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
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@TestPropertySource("classpath:test.properties")
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaListenerTest extends KafkaTestBase {

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

    kafkaTemplate.send(TEST_TOPIC, testKey, testData);

    try (Consumer<String, String> consumer = createConsumer()) {
      KAFKA_EMBEDDED.consumeFromAnEmbeddedTopic(consumer, TEST_TOPIC);
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
        KafkaTestUtils.consumerProps("test-consumer", "true", KAFKA_EMBEDDED);
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    DefaultKafkaConsumerFactory<String, String> cf = new DefaultKafkaConsumerFactory<>(
        consumerProps, new StringDeserializer(), new StringDeserializer());
    return cf.createConsumer();
  }
}