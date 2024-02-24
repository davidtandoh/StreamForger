import com.fasterxml.jackson.databind.JsonNode;

public class DynamicFilter {
    public boolean filter(JsonNode jsonNode) {
        return jsonNode.at("details/department").asText().equals("Engineering") && jsonNode.at("details/age").asText().equals("30");
    }
}