package com.srvivek.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerPoc {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerPoc.class);

    public static void main(String[] args) {

        // Create properties wit kafka configuration
        final Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "[::1]:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // Create kafka record to encapsulate the data
        ProducerRecord<String, String> record1 = new ProducerRecord<>("demo_java", "Hello");
        ProducerRecord<String, String> record2 = new ProducerRecord<>("demo_java", "World");

        // Create Kafka producer
        final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        producer.send(record1);
        producer.send(record2);

        //flush and close
        producer.flush();
        producer.close();
    }
}