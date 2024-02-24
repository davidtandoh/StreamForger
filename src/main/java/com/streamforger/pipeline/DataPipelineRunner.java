package com.streamforger.pipeline;

import com.streamforger.config.ConfigReader;
import com.streamforger.extract.DataSource;
import com.streamforger.extract.DataSourceFactory;
import com.streamforger.load.DataDestination;
import com.streamforger.load.DataDestinationFactory;
import com.streamforger.transform.DataTransformation;
import com.streamforger.transform.streamprocessors.JsonFilterTransformation;
import com.streamforger.transform.util.Condition;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class DataPipelineRunner {
    public static void main(String[] args) {
        System.out.println("Program started");

        // Load configuration
        Properties config = ConfigReader.readProperties("config.properties");

        // Create data source and destination based on configuration
        DataSource<String> dataSource = DataSourceFactory.createSource(config);
        DataDestination<String> dataDestination = DataDestinationFactory.createDestination(config);

        // Define transformations (this part might also be made configurable)
        List<DataTransformation<String, Optional<String>>> transformations = Arrays.asList(
                new JsonFilterTransformation(new Condition("details.age", ">=", "30"))
                // Add more transformations as needed
        );

        // Instantiate and run the data pipeline
        DataPipeline<String, String> pipeline = new DataPipeline<>(dataSource, dataDestination, transformations);
        pipeline.run();

        System.out.println("Program ended");
    }
}

