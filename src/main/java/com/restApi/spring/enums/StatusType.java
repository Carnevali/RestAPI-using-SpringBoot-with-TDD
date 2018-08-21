package com.restApi.spring.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

/**
 * Created by felipecarnevalli on 15/8/18.
 */

@JsonDeserialize(using = StatusType.Deserializer.class )
@JsonFormat(shape = JsonFormat.Shape.OBJECT )
public enum StatusType {
    OK("Ok", "label-success"),

    WARNING("Warning", "label-warning"),

    DANGER("Danger", "label-danger");

    private String value;
    private String label;

    private StatusType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }
    public String getLabel() { return label; }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static class Deserializer extends JsonDeserializer<StatusType> {
        @Override
        public StatusType deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final JsonNode nodes = parser.getCodec().readTree(parser);
            final String enumName = nodes.get("value").asText();
            return StatusType.valueOf(enumName.toUpperCase());
        }
    }
}