package com.example.polls.util;

import com.example.polls.model.Poll;
import com.example.polls.model.User;
import com.example.polls.payload.ChoiceResponse;
import com.example.polls.payload.PollResponse;
import com.example.polls.payload.UserSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryParameters {
    public static Map<String, Object> getVariables(String variables) throws IOException {
        HashMap<String,Object> result =new ObjectMapper().readValue(variables, HashMap.class);
        return result;
    }
}
