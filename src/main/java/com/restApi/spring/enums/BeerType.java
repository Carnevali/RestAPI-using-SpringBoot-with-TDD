package com.restApi.spring.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;

/**
 * Created by felipecarnevalli on 14/7/18.
 */

@JsonDeserialize(using = BeerType.Deserializer.class )
@JsonFormat(shape = JsonFormat.Shape.OBJECT )
public enum BeerType {
    IPA("IPA", "label-success"),

    PILSNER("PILSNER", "label-info"),

    PALEALE("PALEALE", "label-purple"),

    STOUT("STOUT", "label-inverse"),

    WHEATBEER("WHEATBEER", "label-danger"),

    LARGER("LARGER", "label-warning"),

    NONE("NONE", "label-warning");

    private String value;
    private String label;

    private BeerType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static class Deserializer extends JsonDeserializer<BeerType> {
        @Override
        public BeerType deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
            final JsonNode nodes = parser.getCodec().readTree(parser);
            final String enumName = nodes.get("value").asText();
            return BeerType.valueOf(enumName);
        }
    }
}