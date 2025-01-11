package com.srvivek.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class KafkaProducerPartitionerPoc {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerPartitionerPoc.class);

    public static void main(String[] args) {

        logger.info("execution started for main(...)");

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "[::1]:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // RoundRobin partitioner - send messages to each queue
        // Note: keep partitioner default as per kafka config
        properties.setProperty(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class.getName());

        //set batch size
        // Note: keep it default as per kafka config
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, "500");

        final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // send data in multiple batches
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 30; j++) {
                producer.send(new ProducerRecord<>("demo_java", String.format("Message: Hello World testing partitioner in kafka. Test [i: %s, j: %s]", i, j)), new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e == null) {
                            logger.info("Record Metadata. \nTopic: {} \nPartition: {} \nOffset: {} \nTimestamp: {} \n",
                                    recordMetadata.topic(), recordMetadata.partition(),
                                    recordMetadata.offset(), recordMetadata.timestamp());
                        } else {
                            logger.error("Error: {}", e.getStackTrace());
                        }
                    }
                });
                producer.flush();
            }
            // sleep thread for 500ms
            try {
                logger.info("Thread will sleep for 500ms.");
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        producer.close();
        logger.info("execution completed for main(...)");
    }
}