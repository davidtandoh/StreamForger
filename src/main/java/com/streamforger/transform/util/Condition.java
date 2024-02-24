package com.streamforger.transform.util;

import com.fasterxml.jackson.databind.JsonNode;

// Represents a condition for filtering JSON messages
public class Condition {
    public String field;    // JSON field to check
    public String operator; // Operator, e.g., "==", ">="
    public String value;    // Value to compare against

    public Condition(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    // Check if the condition matches for a given JSON node
    public boolean matches(JsonNode jsonNode) {
        String[] pathSegments = field.split("\\.");
        JsonNode currentNode = jsonNode;
        for (String segment : pathSegments) {
            currentNode = currentNode.path(segment);
            if (currentNode.isMissingNode()) {
                return false;
            }
        }

        // Apply the condition based on the operator
        switch (operator) {
            case "==":
                return currentNode.asText().equals(value);
            case ">=":
                return currentNode.asInt() >= Integer.parseInt(value);
            // Add more operators as needed
            default:
                return false;
        }
    }
}