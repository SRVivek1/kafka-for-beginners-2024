package com.srvivek.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerApp {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerApp.class);

    private static final String GROUP_ID = "my-java-app-consumers";

    private static final String TOPIC = "demo_java";

    /**
     * Kafka consumer configuration
     */
    private static Properties getKafkaConfig() {

        final Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "[::1]:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // set application group id
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        // Read config
        //Read only new messages
        // properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        // --> fail if consumer group doesn't exist
        // properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "none");
        // --> Read from beginning
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Don't create topics if not found.
        properties.setProperty(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, Boolean.toString(false));

        return properties;
    }

    public static void main(String[] args) {

        logger.info("Execution started of main(...)");

        // Create consumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(getKafkaConfig());

        //Add shutdown hook to gracefully close the consumer
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Detected shutdown. calling to initiate shutdown.");
                kafkaConsumer.wakeup();

                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    logger.error("Shutdown error while waiting for consumer to close resources. Message: {}", e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });

        try {
            //subscribe
            kafkaConsumer.subscribe(List.of(TOPIC));

            // Poll for events
            while (true) {

                logger.info("Polling.................");
                // The maximum time to block.
                // Must not be greater than Long.MAX_VALUE milliseconds.
                final ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    logger.info("Key: {}, Value: {}", record.key(), record.value());
                    logger.info("Partition: {}, Offset: {}", record.partition(), record.offset());
                }
            }
        } catch (WakeupException we) {
            logger.info("Started shutdown for consumer.");
        } catch (Exception e) {
            logger.error("Unexpected error in consumer. Message: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            kafkaConsumer.close();
            logger.info("Consumer is now gracefully shutdown.");
        }
        logger.info("Execution completed of main(...)");
    }
}