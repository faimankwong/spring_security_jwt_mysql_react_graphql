package com.example.polls.controller;


import com.example.polls.Wiring;
import com.example.polls.payload.ApiResponse;
import com.example.polls.util.QueryParameters;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class GraphQLController {

    @Autowired
    Wiring wiring;
    private GraphQL graphQL;
    @RequestMapping(value = "/graphql", method = RequestMethod.POST)
    public ResponseEntity myGraphql(@RequestBody String request) throws Exception {
        JSONObject jsonRequest = new JSONObject (request);
        //system.out.println(jsonRequest);
        SchemaParser schemaParser = new SchemaParser();
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
  //      File schemaFile = new File(sdl);

        TypeDefinitionRegistry typeDefinitionRegistry =schemaParser.parse(sdl);


        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, wiring.buildRuntimeWiring());
        graphQL = GraphQL.newGraphQL(graphQLSchema).instrumentation(new TracingInstrumentation()).build();


        ExecutionInput executionInput=ExecutionInput.newExecutionInput().query(jsonRequest.getString("query"))
                .context(jsonRequest.getString("variables"))
                //.variables("username","ad");
                .variables(QueryParameters.getVariables(jsonRequest.getString("variables")))
                .build();
            ExecutionResult executionResult=graphQL.execute(executionInput);

            return ResponseEntity.ok(executionResult.toSpecification());

    }

}
