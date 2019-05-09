package com.example.polls;
import com.example.polls.fetch.GraphQLDataFetchers;
import com.example.polls.fetch.MutationDataFetchers;
import com.example.polls.payload.PagedResponse;
import com.example.polls.payload.PollResponse;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.util.AppConstants;
import com.example.polls.util.Scalars;
import graphql.scalars.ExtendedScalars;
import graphql.schema.idl.RuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public class Wiring {

    @Autowired
    GraphQLDataFetchers graphQLDataFetcher;
    @Autowired
    MutationDataFetchers mutationDataFetchers;
    public RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query",typeWiring -> typeWiring
                        .dataFetcher("checkUsernameAvailability",graphQLDataFetcher.checkUsernameAvailability )
                        .dataFetcher("checkEmailAvailability",graphQLDataFetcher.checkEmailAvailability )
                        .dataFetcher("getCurrentUser",graphQLDataFetcher.getCurrentUser )
                        .dataFetcher("getPolls",graphQLDataFetcher.getPolls )
                        .dataFetcher("getUserProfile",graphQLDataFetcher.getUserProfile )
                        .dataFetcher("getPollsCreatedBy",graphQLDataFetcher.getPollsCreatedBy )
                        .dataFetcher("getPollsVotedBy",graphQLDataFetcher.getPollsVotedBy )

                )
                .type("Mutation",typeWiring -> typeWiring
                        .dataFetcher("signup",mutationDataFetchers.registerUser )
                        .dataFetcher("login",mutationDataFetchers.authenticateUser )
                        .dataFetcher("createPoll",mutationDataFetchers.createPoll )
                        .dataFetcher("castVote",mutationDataFetchers.castVote )
                )
                .scalar(Scalars.GraphQLInstant)
                .build();
    }



}
