package com.example.polls.util;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

/**
 * Class containing custom scalars
 */
public class Scalars {

    public static GraphQLScalarType GraphQLInstant  = new GraphQLScalarType("Instant", "an instant in time", new Coercing() {
        @Override
        public String serialize(Object input) {
            return ((Instant)input).toString();
        }

        @Override
        public Object parseValue(Object input) {
            return serialize(input);
        }

        @Override
        public ZonedDateTime parseLiteral(Object input) {
            if (input instanceof StringValue) {
                return ZonedDateTime.parse(((StringValue) input).getValue());
            } else if (input instanceof IntValue) {
                return Instant.ofEpochMilli(((IntValue) input).getValue().longValue()).atZone(ZoneOffset.UTC);
            } else {
                return null;
            }
        }
    });
}