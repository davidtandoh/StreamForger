package com.streamforger.load;

import com.streamforger.load.DataDestination;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class KafkaDataDestination implements DataDestination<String> {
    private KafkaProducer<String, String> producer;
    private String topic;

    public KafkaDataDestination(Properties producerProps, String topic) {
        this.producer = new KafkaProducer<>(producerProps);
        this.topic = topic;
    }

    @Override
    public void initialize() {
        // Initialization if needed
    }

    @Override
    public void writeData(String data) {
        producer.send(new ProducerRecord<>(topic, data));
    }
}
