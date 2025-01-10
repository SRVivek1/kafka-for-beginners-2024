#Kafka programming with Java
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
    ```json
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
    ```json
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
      - 
- **<ins>References:</ins>**
  - [https://learn.conduktor.io/kafka/kafka-sdk-list/](https://learn.conduktor.io/kafka/kafka-sdk-list/)
---

## 2. Kafka Client - Hello world
### Project ref: *xx-xxxx-xx-xxxx*
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
   - ***Step-4:*** Cretae Kafka ProducerRecord with message to be sent to topic.
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
- **<ins>References:</ins>**
  - None

---



