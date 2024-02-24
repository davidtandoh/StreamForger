package com.streamforger.pipeline;

import com.streamforger.transform.DataProcessor;
import com.streamforger.extract.DataSource;
import com.streamforger.load.DataDestination;
import com.streamforger.transform.DataTransformation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class DataPipeline<T, R> {
    private DataSource<T> source;
    private DataProcessor<T, R> processor;
    private DataDestination<R> destination;
    private List<DataTransformation<T, Optional<R>>> transformations;

    public DataPipeline(DataSource<T> source, DataDestination<R> destination, List<DataTransformation<T, Optional<R>>> transformations) {
        this.source = source;
        this.destination = destination;
        this.transformations = transformations;
    }

    public void run() {
        source.initialize();
        destination.initialize();
        AtomicReference<Optional<R>> transformedf = new AtomicReference<>(Optional.empty());

        /**
        source.readData().forEach(data -> {
            Optional<T> result = Optional.of(data);
            for (DataTransformation<T, Optional<R>> transformation : transformations) {
                result = result.flatMap(transformation::transform);
                if (!result.isPresent()) break; // Skip further processing if data is filtered out
            }
            result.ifPresent(destination::writeData);
        });
         **/

        source.readData().forEach(data -> {
            Optional<T> result = Optional.of(data);
            for (DataTransformation<T, Optional<R>> transformation : transformations) {
                if (result.isPresent()) {
                    Optional<R> transformed = transformation.transform(result.get()); // Explicit handling
                    transformedf.set(transformed);
                    //new_result = transformed; // Reassigning the transformed result //TODO Do I really need to reassign...
                } else {
                    break; // Skip further processing if data is filtered out
                }
            }
            transformedf.get().ifPresent(destination::writeData);
        });
    }
}

