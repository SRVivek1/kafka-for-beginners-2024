# Kafka programming with Java
## 1. Kafka Client libraries SDKs
- **<ins>About / Introduction</ins>**
  - We can use kafka client libraries to develop application client to communicate with Kafka server as a producer/consumer.
- **<ins>Java SDKs</ins>**
  - **The official client library:** *Low-level client*
    - **Gradle:**
        ```json
            // https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients
            implementation 'org.apache.kafka:kafka-clients:3.9.0'
        ```
    - **Maven:**
        ```xml
            <!-- https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients -->
            <dependency>
                <groupId>org.apache.kafka</groupId>
                <artifactId>kafka-clients</artifactId>
                <version>3.9.0</version> <!-- check for latest release / compatibe version -->
            </dependency>
        ```
  - **The official Kafka Streams client library:** *To create your Kafka Streams application*
    - **Gradle:**
        ```json
              // https://mvnrepository.com/artifact/org.apache.kafka/kafka-streams
              implementation 'org.apache.kafka:kafka-streams:3.9.0'
        ```
    - **Maven:**
        ```xml
            <!-- https://mvnrepository.com/artifact/org.apache.kafka/kafka-streams -->
            <dependency>
                <groupId>org.apache.kafka</groupId>
                <artifactId>kafka-streams</artifactId>
                <version>3.9.0</version>
            </dependency>
        ```
  - **Kafka for Spring Boot:** Applies Spring concepts to Kafka development
    - **Reference:** [https://spring.io/projects/spring-kafka#overview](https://spring.io/projects/spring-kafka#overview)
    - **Gradle:**
        ```gradle
            // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
            implementation 'org.springframework.kafka:spring-kafka:3.3.1'
        ```
    - **Maven:**
        ```xml
          <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
          </dependency>
        ```
  - **Spring Cloud Stream:** Bindings for Kafka Stream
    - **Reference:** [https://spring.io/projects/spring-cloud-stream](https://spring.io/projects/spring-cloud-stream)
    - **Gradle:**
        ```gradle
            // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-stream
            implementation 'org.springframework.cloud:spring-cloud-stream:4.2.0'
            
            // https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-stream-binder-kafka
            implementation 'org.springframework.cloud:spring-cloud-stream-binder-kafka:4.2.0'

            // https://mvnrepository.com/artifact/org.springframework.kafka/spring-kafka
            implementation 'org.springframework.kafka:spring-kafka:3.3.1'
        ```
    - **Maven:**
        ```xml
          <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-stream</artifactId>
          </dependency>
          <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-stream-binder-kafka</artifactId>
          </dependency>
          <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
          </dependency>
        ```
  - **Akka Streams & Alpakka Kafka**
    - **Documentation:**
      - [https://doc.akka.io/libraries/akka-core/current/stream/index.html](https://doc.akka.io/libraries/akka-core/current/stream/index.html)
      - [https://doc.akka.io/libraries/alpakka-kafka/current/home.html](https://doc.akka.io/libraries/alpakka-kafka/current/home.html)
- **<ins>References:</ins>**
  - [https://learn.conduktor.io/kafka/kafka-sdk-list/](https://learn.conduktor.io/kafka/kafka-sdk-list/)
---

## 2. Kafka Client - Hello world
### Project ref: [a1-kafka-producer](https://github.com/SRVivek1/kafka-for-beginners-2024/tree/main/03-kafka-beginners-gradle/a1-kafka-producer)
- **<ins>Purpose / Feature</ins>**
  - This is xyz feature.
- **<ins>Steps</ins>**
  - ***Project Setup:*** 
    - Create a gradle project using intellij IDEA IDE or by visiting *https://start.spring.io/*.
    - Delete `src` folder from project home directory.
  - ***Step-1:*** Create a *new module* inside project.
    - **Steps:** Right Click on the project --> New --> Module
      - Select appropriate option on the given form and click *Create*.
  - ***Step-2:*** Add following dependencies.
     - **Kafka Clients:** *implementation("org.apache.kafka:kafka-clients:3.9.0")*
     - **Slf4j API:** *implementation("org.slf4j:slf4j-api:2.0.16")*
     - **Slf4j Simple:** *implementation("org.slf4j:slf4j-simple:2.0.16")*
   - ***Step-3:*** Create properties for target Kafka server.
   - ***Step-4:*** Cretae Kafka ProducerRecord with *Topic name* & *message* to be sent to topic.
   - ***Step-5:*** Create Kafka Producer object using the *kafka properties*.
   - ***Step-6:*** Pass the kafka record to *Kafka Producer* ***send(record)*** to send to topic.
   - ***Step-7:*** Flush the data using *producer* instance.
   - ***Step-8:*** Close the producer object/connection.
- **<ins>Gradle / External dependency</ins>**
  - Required dependency.
      ```groovy
            dependencies {
              implementation("org.apache.kafka:kafka-clients:3.9.0")
              implementation("org.slf4j:slf4j-api:2.0.16")
              implementation("org.slf4j:slf4j-simple:2.0.16")
              testImplementation(platform("org.junit:junit-bom:5.9.1"))
              testImplementation("org.junit.jupiter:junit-jupiter")
            }
      ```
- **<ins>Code / Config changes</ins>**
  - **Producer:** *KafkaProducerPoc.java*
    - imports
      - *import org.apache.kafka.clients.producer.KafkaProducer;*
      - *import org.apache.kafka.clients.producer.ProducerRecord;*
      - *import org.apache.kafka.common.serialization.StringSerializer;*
      - *import java.util.Properties;*
    - Producer class to publish data to topics.
      ```java
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
      ```
    - **Output: *Terminal*** *kafka-console-consumer.sh*
      ```properties
          TBU
      ```
- **<ins>References:</ins>**
  - [https://www.javatpoint.com/creating-kafka-producer-in-java](https://www.javatpoint.com/creating-kafka-producer-in-java)
---

## 3. Kafka Producers: callbacks
### Project ref: [a2-kafka-producer-with-callbacks](https://github.com/SRVivek1/kafka-for-beginners-2024/tree/main/03-kafka-beginners-gradle/a2-kafka-producer-with-callbacks)
- **<ins>Purpose / Feature</ins>**
  - The callback is a function passed when sending data to topic. This function is implemented for asynchronously handling the request completion, hence *void* return type.  
  - The callback function used by the producer is the *onCompletion()* with two arguments:
    - **Metadata of the Record:** 
      - Metadata of the record means fetching the information regarding the partition and its offsets. 
      - If it is not null, an error will be thrown.
    - **Exception:**
      - Following are two exceptions which can be thrown while processing:
        - **Retriable exception:** 
          - This exception says that the message may be sent.
        - **Non-retriable exception:** 
          - This exception throws the error that the message will never be sent.
- **<ins>Steps</ins>**
  - ***Project Setup:*** Develop kafka producer app.
    - Check *Section-2* for reference.
  - ***Step-1:*** Create and pass Kafka *Callback.java* class instance in send(...) method.
    - Add completion business logic inside *onCompletion(...)* method.
- **<ins>Gradle / External dependency</ins>**
  - Required dependency.
      ```groovy
            dependencies {
              implementation("org.apache.kafka:kafka-clients:3.9.0")
              implementation("org.slf4j:slf4j-api:2.0.16")
              implementation("org.slf4j:slf4j-simple:2.0.16")
            }
      ```
- **<ins>Code / Config changes</ins>**
  - **Producer:** *KafkaProducerPoc.java*
    - imports
      - *import org.apache.kafka.clients.producer.Callback;*
      - *import org.apache.kafka.clients.producer.ProducerConfig;*
    - Producer class to publish data to topics with *Callback instance*.
        ```java
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

                  // send data and register callback for status
                  producer.send(new ProducerRecord<>("demo_java", "callback demo app"), new Callback() {
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
                  producer.close();

                  logger.info("KafkaProducerWithCallbacks execution completed.");
              }
          }
        ```
    - **Output: ** *IDE Console*
    ```properties
        [kafka-producer-network-thread | producer-1] INFO com.srvivek.kafka.KafkaProducerWithCallbacks - Received new metadata. 
        Topic: demo_java, 
        Partition: 2, 
        Offset: 51, 
        Timestamp: 1736577123225
    ```
- **<ins>References:</ins>**
  - [https://www.javatpoint.com/kafka-producer-callbacks](https://www.javatpoint.com/kafka-producer-callbacks)
---





