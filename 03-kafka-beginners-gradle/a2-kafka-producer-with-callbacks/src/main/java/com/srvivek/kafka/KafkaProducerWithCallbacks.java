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

public class KafkaProducerWithCallbacks {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerWithCallbacks.class);

    public static void main(String[] args) {
        logger.info("KafkaProducerWithCallbacks execution started.");

        final Properties properties = new Properties();
        /*properties.setProperty("bootstrap.servers", "[::1]:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());*/

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "[::1]:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //Kafka producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // push 10 messages to topic
        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>("demo_java", "callback demo app - " + i), new Callback() {
                // Executes every time a message is successfully sent or exception occurs.
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                    // check if no exception occurred
                    if (e == null) {
                        logger.info("Received new metadata. \nTopic: {}, \nPartition: {}, \nOffset: {}, \nTimestamp: {}",
                                recordMetadata.topic(), recordMetadata.partition(),
                                recordMetadata.offset(), recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing: {}", e);
                    }
                }
            });
            // flush and close
            producer.flush();
        }
        producer.close();

        logger.info("KafkaProducerWithCallbacks execution completed.");
    }
}