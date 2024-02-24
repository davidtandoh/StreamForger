package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class DynamicJsonFilterStream {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static List<Condition> conditions;

    static {
        try {
            // Load conditions from a config file (or could be from any other source)
            conditions = objectMapper.readValue(new File("path/to/your/config.json"), new TypeReference<List<Condition>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "dynamic-json-filter-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> sourceStream = builder.stream("source-topic");

        Predicate<String, String> dynamicPredicate = (key, value) -> {
            try {
                JsonNode jsonNode = objectMapper.readTree(value);
                for (Condition condition : conditions) {
                    if (!condition.matches(jsonNode)) {
                        return false;
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };

        KStream<String, String>[] branches = sourceStream.branch(dynamicPredicate);
        KStream<String, String> filteredStream = branches[0];

        filteredStream.to("target-topic");

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }

    // Represents a condition to be checked against a field in the JSON
    // Represents a condition to be checked against a field in the JSON
    static class Condition {
        public String field;    // The JSON field to check, e.g., "details.age"
        public String operator; // The operator, e.g., "==", ">=", "<="
        public String value;    // The value to compare against, e.g., "30"

        // Evaluates this condition against a given JSON node
        public boolean matches(JsonNode jsonNode) {
            String[] pathSegments = field.split("\\."); // Split the field into path segments
            JsonNode currentNode = jsonNode;
            for (String segment : pathSegments) {
                currentNode = currentNode.path(segment); // Navigate down the JSON structure
                if (currentNode.isMissingNode()) {
                    return false; // Field not found
                }
            }

            // Now, currentNode is the field to check the condition against
            switch (operator) {
                case "==":
                    if (currentNode.isTextual()) {
                        return currentNode.asText().equals(value);
                    } else if (currentNode.isNumber()) {
                        return currentNode.asDouble() == Double.parseDouble(value);
                    }
                    break;
                case ">=":
                    if (currentNode.isNumber()) {
                        return currentNode.asDouble() >= Double.parseDouble(value);
                    }
                    break;
                case "<=":
                    if (currentNode.isNumber()) {
                        return currentNode.asDouble() <= Double.parseDouble(value);
                    }
                    break;
                // Add more cases for other operators, e.g., ">", "<", "!="
            }

            return false; // Default to false if the condition doesn't match
        }
    }
}
