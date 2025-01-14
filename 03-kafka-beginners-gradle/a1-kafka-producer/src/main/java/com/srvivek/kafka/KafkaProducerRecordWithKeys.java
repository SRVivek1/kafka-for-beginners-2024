package com.srvivek.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerRecordWithKeys {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerRecordWithKeys.class);

    public static void main(String[] args) {

        logger.info("Execution started for main(...)");

        final Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "[::1]:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        final String TOPIC = "demo_java";
        // Send data
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 10; i++) {

                String key = "id_" + i;
                String message = "hello world - " + i;

                producer.send(new ProducerRecord<>(TOPIC, key, message), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                        if (e == null) {
                            logger.info("Record: Key: {}, Partition: {}", key, recordMetadata.partition());
                        } else {
                            logger.error("Stacktrace:\n{}", e.getStackTrace());
                        }
                    }
                });
                producer.flush();
            }
            // sleep thead to create bataches
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        producer.close();
        logger.info("Execution completed for main(...)");
    }
}
