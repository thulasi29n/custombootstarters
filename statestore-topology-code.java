Certainly. Based on the classes and details you've provided, here's how your topology might be constructed:

```java
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.common.serialization.Serdes;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

...

// Initialize the StreamsBuilder
StreamsBuilder streamsBuilder = new StreamsBuilder();

// Serde Setup
Map<String, String> serdeConfig = Collections.singletonMap("schema.registry.url", "http://localhost:8081"); // Adjust the URL accordingly

Serde<String> stringSerde = Serdes.String();

// Serde for the validated-txn-avro topic
Serde<ValidatedTransaction> validatedTxnSerde = new SpecificAvroSerde<>();
validatedTxnSerde.configure(serdeConfig, false);

// Serde for the data-to-add topic
Serde<NewData> dataToAddSerde = new SpecificAvroSerde<>();
dataToAddSerde.configure(serdeConfig, false);

// Serde for the joined data
Serde<JoinedData> joinedDataSerde = new SpecificAvroSerde<>();
joinedDataSerde.configure(serdeConfig, false);

// Create KStream for validated-txn-avro topic
KStream<String, ValidatedTransaction> validatedTxnStream = streamsBuilder.stream("validated-txn-avro", Consumed.with(stringSerde, validatedTxnSerde));

// Create KStream for data-to-add topic
KStream<String, NewData> dataToAddStream = streamsBuilder.stream("data-to-add", Consumed.with(stringSerde, dataToAddSerde));

// Perform KStream-KStream join
KStream<String, JoinedData> joinedStream = validatedTxnStream.join(
    dataToAddStream, 
    (validatedTxn, newData) -> {
        // Here's how you would construct a JoinedData object using values from both streams
        return new JoinedData(validatedTxn.getTxn_ref(), validatedTxn.getLei(), validatedTxn.getTr_date(),
                              validatedTxn.getOtcflag(), validatedTxn.getReg_name(), validatedTxn.getIsin(), 
                              newData.getCurrency_code());
    },
    JoinWindows.of(Duration.ofMinutes(5)),  // This assumes you're okay with a windowed join. Adjust as necessary.
    Joined.with(stringSerde, validatedTxnSerde, dataToAddSerde)
);

// Optional: If you want to send the joined data to another topic
joinedStream.to("joined-topic", Produced.with(stringSerde, joinedDataSerde));

// Build the topology (not starting it yet)
// KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), config);
```

This code sets up the topology of your Kafka Streams application, where two streams are joined based on the common key (`txn_ref`), and the resulting data is modeled using the `JoinedData` class.

Please remember to replace placeholder class names with actual class names and adjust configurations accordingly. This is a generalized structure, and you might need to make further modifications based on your environment and requirements.