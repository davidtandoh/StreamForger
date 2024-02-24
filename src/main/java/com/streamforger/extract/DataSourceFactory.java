package com.streamforger.extract;

import java.util.Properties;

public class DataSourceFactory {
    public static DataSource<String> createSource(Properties config) {
        String type = config.getProperty("source.type");
        switch (type) {
            case "kafka":
                return new KafkaDataSource(config, config.getProperty("source.topic"));
            // Add cases for other data source types
            default:
                throw new IllegalArgumentException("Unsupported data source type: " + type);
        }
    }
}

