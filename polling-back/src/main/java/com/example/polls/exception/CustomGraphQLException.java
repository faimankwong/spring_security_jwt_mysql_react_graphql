package com.example.polls.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomGraphQLException extends RuntimeException implements GraphQLError {
    private final int errorCode;
    private final String errorMessage;

    public CustomGraphQLException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return null;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }

    @Override
    public Map<String, Object> getExtensions() {
        Map<String, Object> customAttributes = new HashMap<>();
        customAttributes.put("errorCode", this.errorCode);
        customAttributes.put("errorMessage", this.getMessage());
        return customAttributes;
    }
}