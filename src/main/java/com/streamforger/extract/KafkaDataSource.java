package com.streamforger.extract;
import com.streamforger.extract.DataSource;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaDataSource implements DataSource<String> {
    private KafkaConsumer<String, String> consumer;
    private String topic;

    private static final Logger logger = LoggerFactory.getLogger(KafkaDataSource.class);


    public KafkaDataSource(Properties consumerProps, String topic) {
        this.consumer = new KafkaConsumer<>(consumerProps);
        this.topic = topic;
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    @Override
    public void initialize() {
        // Initialization if needed
    }

    //TODO should I be using any othert stream libraries here ?
    @Override
    public Iterable<String> readData() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100000));
        if (!records.isEmpty()) {
            logger.info("Successfully polled {} records from topic {}:{}", records.count(), topic);
        } else {
            logger.info("No records polled - this might be normal if there are no new messages.");
        }
        return StreamSupport.stream(records.spliterator(), false)
                .map(record -> record.value())
                .collect(Collectors.toList());
    }

    @Override
    public void acknowledge(String message) {
        // Commit offsets if necessary
        consumer.commitAsync();
    }
}
