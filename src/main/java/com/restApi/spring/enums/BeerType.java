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
    IPA("Ipa", "label-success"),

    PILSNER("Pilsner", "label-info"),

    PALEALE("PaleAle", "label-purple"),

    STOUT("Stout", "label-inverse"),

    WHEATBEER("WheatBeer", "label-danger"),

    LARGER("Larger", "label-warning"),

    NONE("None", "label-warning");

    private String value;
    private String label;

    private BeerType(String value, String label) {
        this.value = value;
        this.label = label;
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
            return BeerType.valueOf(enumName.toUpperCase());
        }
    }
}