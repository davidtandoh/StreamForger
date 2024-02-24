package com.streamforger.load;

import java.util.Properties;

public class DataDestinationFactory {
    public static DataDestination<String> createDestination(Properties config) {
        String type = config.getProperty("destination.type");
        switch (type) {
            case "kafka":
                return new KafkaDataDestination(config, config.getProperty("destination.topic"));
            // Add cases for other data destination types
            default:
                throw new IllegalArgumentException("Unsupported data destination type: " + type);
        }
    }
}

