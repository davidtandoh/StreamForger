package com.streamforger.pipeline;

package com.example.datapipeline.pipeline;

import java.util.List;
import java.util.Properties;

public class PipelineConfig {
    private String pipelineId; // Unique identifier for the pipeline
    private Properties sourceConfig; // Configuration for the data source
    private Properties destinationConfig; // Configuration for the data destination
    private List<TransformationConfig> transformationConfigs; // List of configurations for each transformation

    // Constructor
    public PipelineConfig(String pipelineId, Properties sourceConfig, Properties destinationConfig, List<TransformationConfig> transformationConfigs) {
        this.pipelineId = pipelineId;
        this.sourceConfig = sourceConfig;
        this.destinationConfig = destinationConfig;
        this.transformationConfigs = transformationConfigs;
    }

    // Getters
    public String getPipelineId() {
        return pipelineId;
    }

    public Properties getSourceConfig() {
        return sourceConfig;
    }

    public Properties getDestinationConfig() {
        return destinationConfig;
    }

    public List<TransformationConfig> getTransformationConfigs() {
        return transformationConfigs;
    }

    // Additional methods as needed, like setters or utility methods to parse specific configuration values
}

