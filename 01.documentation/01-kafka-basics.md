# Introdution to Kafka
## 01. Introduction to kafka [***in progress***]
- **<ins>About / Introduction</ins>**
  - Apache Kafka is an open-source distributed event streaming platform used by thousands of companies for high-performance data pipelines, streaming analytics, data integration, and mission-critical applications.
  - Created by `LinkedIn`,now mainly maintained by IBM, Cloudera, Confluent.
  - Distributed, fault tolerant, resilient architecture.
  - ***Horizontal Scalability:***
    - Can scale upto *100s of brokers*.
    - Can scale to millions of messages per second.
  - **Core Capabilities:**
    - ***High Throughput:***
      - Deliver messages at network limited throughput using a cluster of machines with latencies as low as 2ms.
    - ***Scalable:***
      - Scale production clusters up to a thousand brokers, trillions of messages per day, petabytes of data, hundreds of thousands of partitions. Elastically expand and contract storage and processing.
    - ***Permanent storage:***
      - Store streams of data safely in a distributed, durable, fault-tolerant cluster.
    - ***High availability:***
      - Stretch clusters efficiently over availability zones or connect separate clusters across geographic regions.
  - **Ecosystem:**
    - ***Built-in Stream Processing:***
      - Process streams of events with joins, aggregations, filters, transformations, and more, using event-time and exactly-once processing.
    - ***Connect To Almost Anything:***
      - Kafka’s out-of-the-box Connect interface integrates with hundreds of event sources and event sinks including Postgres, JMS, Elasticsearch, AWS S3, and more.
    - ***Client Libraries:***
      - Read, write, and process streams of events in a vast array of programming languages.
    - ***Large Ecosystem Open Source Tools:***
      - Large ecosystem of open source tools: Leverage a vast array of community-driven tooling.
  - **Trust & Ease Of Use:**
    - ***Mission Critical:***
      - Support mission-critical use cases with guaranteed ordering, zero message loss, and efficient exactly-once processing.
    - ***Trusted By Thousands of Orgs:***
      - Thousands of organizations use Kafka, from internet giants to car manufacturers to stock exchanges. More than 5 million unique lifetime downloads.
    - ***Vast User Community:***
      - Kafka is one of the five most active projects of the Apache Software Foundation, with hundreds of meetups around the world.
    - ***Rich Online Resources:***
      - Rich documentation, online training, guided tutorials, videos, sample projects, Stack Overflow, etc.
  - **Use cases:**
    - *Messaging System*
    - *Activity tracking*
    - *Gather matrics from many different locations*
    - *Application logs gathering* (first use case of kafak)
    - *Stream processing* (Using kafka stream API)
    - *Decouple system dependency*
    - *Integration with Spark, Hadoop, Flink, Storm and many other BigData technologies.*
    - *Microservices pub-sub*
  - **Applications:**
    - ***Netflix:*** Apply recommendations in real-time while user watches the shows.
    - ***Uber:*** Collect User, Cab & trip data in realtime to forecast demand and compute surge pricing.
    - ***LinkedIn:*** Prevent spam & collect user interactions to make better connection recommendataions in realtime. 

- **<ins>References:</ins>**
  - [https://kafka.apache.org/](https://kafka.apache.org/)

---

## 1. Kafka Topics, partitions and offsets
- **<ins>About / Introduction</ins>**
  - ***Kafka Topics*** are a particular stream of data inside kafka cluster. In a cluster we can have many topics e.g. - logs, purchases, twitter_tweets, trucks_gps and so on.
    - We can think of a Topic as a table in database but without all the constraints. But we can't query topics.
    - Instead, we use `Kafka Producers` to send data and `Kafka Consumers` to receive data.
  - We can as much as Topic in kafka cluster, it can be identified by it's name.
  - Kafak Topics support any kind of message formats e.g. JSON, Avro, Text file, binary etc..
  - The squence of messages is called Data Stream.
  - **Partitions and offset:**
    - Topics are split in partitions (eg. 100 partitions).
    - Messages sent to Kafka Topic will endup in these partitions and message within each partition is going to be ordered.
      - Data is assigned randomly to a partition unless a key is provided (more on this later).
      - All the messages in partitions are assigned an message ID (aka Offset) (autoincrement 0,1,2,3,4,.....).
      - The message ID is assigned at the `Partition Level`. Each partition maintains it's own independent sequence of `offsets`, starting `0`. Thesse offsets are unique only within the given partition. Other partitions can also have same offset.
      - Offsets are not reused even if the message is removed from the partition.
    - When consuming a message combination of partition_number and the offset is used to uniquely identify a message in a topic.
    - Consumers track offsets independenctly for each partition they consume from.
  - ***Kafka Topics are immutable:*** Once the data is written to a partition, it can't be updated or deleted.
  - ***Data Storage:*** Data is kept only for a limited time (default is one week - configurable)  
  - **Message ordering:** Order of messsages is guaranteed only within a partition but not accross the partitions.
    - Here message ordering means reading messages in ordered sequence as per offset number.

- **<ins>References:</ins>**
  - [https://developer.confluent.io/courses/apache-kafka/producers/?utm_medium=sem&utm_source=google&utm_campaign=ch.sem_br.nonbrand_tp.prs_tgt.dsa_mt.dsa_rgn.india_lng.eng_dv.all_con.confluent-developer&utm_term=&creative=&device=c&placement=&gad_source=1&gclid=Cj0KCQiAj9m7BhD1ARIsANsIIvDL5iHFIKlmflFV-JZ0nsPa-33AVBuBxRBlnagQtFar5bgoO0Eygz8aAvm7EALw_wcB](https://developer.confluent.io/courses/apache-kafka/producers/?utm_medium=sem&utm_source=google&utm_campaign=ch.sem_br.nonbrand_tp.prs_tgt.dsa_mt.dsa_rgn.india_lng.eng_dv.all_con.confluent-developer&utm_term=&creative=&device=c&placement=&gad_source=1&gclid=Cj0KCQiAj9m7BhD1ARIsANsIIvDL5iHFIKlmflFV-JZ0nsPa-33AVBuBxRBlnagQtFar5bgoO0Eygz8aAvm7EALw_wcB)

---

## 2. Kafka - Producers, message keys and Serialization
- **<ins>About / Introduction</ins>**
  - **Kafka Producer:**
    - A producer is the one which publishes or writes data to the topics within different partitions.
    - Producers knows / decides that, what data should be written to which partition and broker (server). The user does not require to specify the broker and the partition.
    - In case of Kafka broker failures, Producers will automatically recover.
    - **Strategies:**
      - A producer uses following strategie//s to write data to the cluster:
        - *Message Keys*
        - *Acknowledgement*
    - **Message Keys:**
      - Apache Kafka enables the concept of the key to send the messages in a specific order in specific partition. 
      - The Key enables the producer with two choices, i.e., either to send data to each partition (automatically) or send data to a specific partition only. Sending data to some specific partitions is possible with the message keys.
      - If the producers apply key over the data, that data will always be sent to the same partition always. But, if the producer does not apply the key while writing the data, it will be sent in a round-robin manner. This process is called load balancing.
      - In Kafka, load balancing is done when the producer writes data to the Kafka topic without specifying any key, Kafka distributes little-little bit data to each partition.
      - There are two ways to know that the data is sent with or without a key:
        - ***If the value of key=NULL:***
          - It means that the data is sent without a key. Thus, it will be distributed in a round-robin manner (i.e., distributed to each partition).
        - ***If the value of the key!=NULL:***
          - It means the key is attached with the data, and thus all messages will always be delivered to the same partition (Hashing).
      - Typical message in kafka:
        <table style="border: 1px solid black">
          <tr>
            <td>
              Key-Binary <br/>
              (can be null)
              </td>
            <td>
              Value-Binary <br/>
              (can be null)
            </td>
          </tr>
          <tr>
            <td colspan="2">
              Compression type <br/> 
              (none, gzip, snappy, lz4, zstd)
            </td>
          </tr>
          <tr>
            <td colspan="2">
              Headers (Optional) <br/> 
              <p> 
              |-----------------| <br/>
              | Key &nbsp;&nbsp;| value &nbsp;&nbsp; |<br/>
              |-----------------| <br/>
              | Key &nbsp;&nbsp;| value &nbsp;&nbsp; |<br/>
              |-----------------| <br/>
              </p>
            </td>
          </tr>
          <tr>
            <td colspan="2">
              Partition + Offset
            </td>
          </tr>
          <tr>
            <td colspan="2">
              Timestamp (System or user set)
            </td>
          </tr>
        </table>
    - **Acknowledgment:**
      - In this strategy, the producer can get a confirmation of its data writes by receiving the following acknowledgments:
        - ***acks=0:*** 
          - This means that the producer sends the data to the broker but does not wait for the acknowledgement. 
          - This leads to possible data loss because without confirming that the data is successfully sent to the broker or may be the broker is down, it sends another one.
        - ***acks=1:*** 
          - This means that the producer will wait for the leader's acknowledgement. 
          - The leader asks the broker whether it successfully received the data, and then returns feedback to the producer. In such case, there is limited data loss only.
        - ***acks=all:*** 
          - Here, the acknowledgment is done by both the leader and its followers. 
          - When they successfully acknowledge the data, it means the data is successfully received. In this case, there is no data loss.
  - **Message Serializer:**
    - Kafka only accepts `bytes` as input from `Producers` and sends only `bytes` as output to `Consumers`.
    - Message Serialization means transforming objects/data into bytes.
      - The serialization is used only on the value and the key.
      - It uses the Serializers provided **KeySerializer** & **ValueSerializer** serialize the corresponding data.
        - **Serializers:** `IntegerSerializer`, `StringSerializer` etc.
        - e.g. 
          - **Key Object is 123** => *KeySerializer=IntegerSerializer*
          - **Value Object is 'Hello world'**  => *ValueSerializer=StringSerializer*
    - **Common Serializers:**
      - *String (including JSON)*
      - *Int, Float*
      - *Avro*
      - *Protobuf*
    - **Kafka Message Key Hashing:**
      - A **Kafka Partitioner** is a code logic that takes a record / message and determines to which partition to send the record to.
      - The it uses key hashing to determine the partition.
      - By default *kafka partitioner* uses **murmur2 algorithm** as below.
        - `targetPartition = Math.abs(Utils.murmur2(keyBytes)) % (numPartitions - 1)` 
- **<ins>References:</ins>**
  - [https://www.javatpoint.com/apache-kafka-producer](https://www.javatpoint.com/apache-kafka-producer)

---

## 3. Kafka Consumer and Deserialization
- **<ins>About / Introduction</ins>**
  - **Kafka Consumers** is used to reading data from a topic (pull model) and remember a topic again is identified by its name. So the consumers are smart enough and they will know which broker to read from and which partitions to read from. 
    - In case of broker failures, the consumers know how to recover and this is resiiency property of Apache Kafka. 
    - A consumer can read data from more than one partions / brokers. They know in advance that from which broker / parition they need to read data from and also in case of broker failures, they know how to recover data.
    - Data is read from low to high offset within each partition.
  - **Consumer Deserialization:**
    - Deserialization is the process of transformating bytes into objects / data. This is used on the key and the value of the message.
    - It uses the Serializers provided by **KeyDeserializer** & **ValueDeserializer** to deserialize the key and the value.
      - **Serializers:** `IntegerSerializer`, `StringSerializer` etc.
        - e.g. 
          - **Key Object is 123** => *KeySerializer=IntegerSerializer*
          - **Value Object is 'Hello world'**  => *ValueSerializer=StringSerializer*
    - **Common Serializers:**
      - *String (including JSON)*
      - *Int, Float*
      - *Avro*
      - *Protobuf*

- **<ins>Notes:</ins>**
  - The Serialization / Deserialization type must not change during a `Topic` lifecycle.
  - If we need to change the Data type of a topic, we must create a new Topic and shared with details so consumers can make necessary changes to consume data from new Topic. 

- **<ins>References:</ins>**
  - [https://www.geeksforgeeks.org/apache-kafka-serializer-and-deserializer/](https://www.geeksforgeeks.org/apache-kafka-serializer-and-deserializer/)
  - [https://www.javatpoint.com/apache-kafka-consumer-and-consumer-groups](https://www.javatpoint.com/apache-kafka-consumer-and-consumer-groups)

---

## XX. Kafka - Consumer groups
- **<ins>About / Introduction</ins>**
  - A consumer group is a group of multiple consumers which visions to an application basically. Each consumer present in a group reads data directly from the exclusive partitions.
    - We can have multiple consumer groups on the same topic. and all consumers from these groups can read parallely from the Topic partitions.
    - **Note:** Only one consumer will be assigned to 1 partition but one consumer can read from multiple partitions.
  - In case, the number of consumers are more than the number of partitions, some of the consumers will be in an inactive state. 
    - Somehow, if we lose any active consumer within the group then the inactive one can takeover and will come in an active state to read the data.
  - But, how to decide which consumer should read data first and from which partition ?
    - For such decisions, consumers within a group automatically use a **GroupCoordinator** and one **ConsumerCoordinator**, which assigns a consumer to a partition. This feature is already implemented in the Kafka.
  - **To Create disntinct consumer groups** we'll use the consumer property *group.id*.
  - **Consumer Offsets:**
    - Apache Kafka provides a convenient feature to store an offset value for a consumer group. It *stores an offset value* to know at which partition, the consumer group is reading the data. 
      - As soon as a consumer in a group reads data, Kafka automatically commits the offsets, or it can be programmed. 
      - These offsets are committed live in a topic known as ***__consumer_offsets*** (*internal kafka topic*). 
      - This feature was implemented in the case of a machine failure where a consumer fails to read the data. So, the consumer will be able to continue reading from where it left off due to the commitment of the offset. 
    - **Delivery Semantics for consumers**
      - By default, Java consumers will automatically commits offset (at least once).
      - There are 3 delivery semantics, if we choose to commit ***manually***:
        - **At least once (usually preferred):**
          - Offsets are commited after the message is processed. As if something goes wrong the mesage will be read again.
          - This can result in duplicate processing of message. Hence ensure that our processing is idempotent (reprocessing the message won't impact the system). 
        - **At most once:**
          - Offsets are commited as soon as the message is received.
          - If processing goes wrong, some message will be lost as they won't be read again.
        - **Exactly Once:**
          - For Kafka to Kafka workflows, uses the transactional API (easy with Kafka Streams API)
          - For Kafka to external system workflows, uses idempotent consumers.
- **<ins>References:</ins>**
  - [https://www.javatpoint.com/apache-kafka-consumer-and-consumer-groups](https://www.javatpoint.com/apache-kafka-consumer-and-consumer-groups)

---

