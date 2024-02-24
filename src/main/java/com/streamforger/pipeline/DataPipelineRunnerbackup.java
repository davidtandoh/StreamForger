package com.streamforger.pipeline;

import com.streamforger.extract.KafkaDataSource;
import com.streamforger.load.KafkaDataDestination;
import com.streamforger.transform.DataTransformation;
import com.streamforger.transform.streamprocessors.JsonFilterTransformation;
import com.streamforger.transform.util.Condition;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class DataPipelineRunnerbackup {
    public static void main(String[] args) {
        // Kafka Consumer Properties for the Data Source
        System.out.println("Program started");
        Properties consumerProps = new Properties();
        consumerProps.put("bootstrap.servers", "localhost:9092");
        consumerProps.put("group.id", "example-group");
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        String sourceTopic = "mytopic";

        // Kafka Producer Properties for the Data Destination
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        String destinationTopic = "destination-topic";

        // Initialize the Data Source and Data Destination
        KafkaDataSource dataSource = new KafkaDataSource(consumerProps, sourceTopic);
        KafkaDataDestination dataDestination = new KafkaDataDestination(producerProps, destinationTopic);

        // Define the Transformations
        JsonFilterTransformation jsonFilter = new JsonFilterTransformation(new Condition("details.age", ">=", "30"));
        //DataTransformation<String, String> otherTransformation = new OtherTransformation(); // Define this transformation as per your requirement

        List<DataTransformation<String, Optional<String>>> transformations = Arrays.asList(jsonFilter);

        // Instantiate and Run the DataPipeline
        DataPipeline<String, String> pipeline = new DataPipeline<>(dataSource, dataDestination, transformations);
        pipeline.run();

        System.out.println("program ended");
    }
}
