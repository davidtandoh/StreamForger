package com.streamforger.daemon;

package com.example.datapipeline.daemon;

import com.example.datapipeline.config.ConfigListener;
import com.example.datapipeline.pipeline.PipelineConfig;
import com.example.datapipeline.pipeline.PipelineManager;

public class PipelineDaemon implements Runnable {
    private final ConfigListener configListener;
    private final PipelineManager pipelineManager;

    public PipelineDaemon(ConfigListener configListener, PipelineManager pipelineManager) {
        this.configListener = configListener;
        this.pipelineManager = pipelineManager;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            PipelineConfig configUpdate = configListener.checkForUpdates();
            if (configUpdate != null) {
                pipelineManager.createAndStartPipeline(configUpdate);
            }

            // Include logic to sleep/wait or use event-driven notifications for efficiency
        }
    }
}
