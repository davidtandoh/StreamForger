package com.streamforger.transform.streamprocessors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streamforger.transform.DataTransformation;
import com.streamforger.transform.util.Condition;


import java.util.Optional;

public class JsonFilterTransformation implements DataTransformation<String, Optional<String>> {
    private Condition condition; // Assuming a Condition class is already defined
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public JsonFilterTransformation(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Optional<String> transform(String data) {
        // Implement the logic to apply the condition to the data
        // If the condition is met, return the data; otherwise, return null or some indicator to skip this data
        try {
            JsonNode jsonNode = objectMapper.readTree(data); // Parse string data to JSON
            if (condition.matches(jsonNode)) {
                return Optional.of(data); // Return data if it matches the condition
            }
        } catch (Exception e) {
            // Handle parsing exceptions or consider logging
        }
        return Optional.empty(); // Return empty Optional if data does not match the condition
    }

}
