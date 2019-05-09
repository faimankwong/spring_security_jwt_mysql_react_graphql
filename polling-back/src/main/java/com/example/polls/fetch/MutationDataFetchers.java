package com.example.polls.fetch;

import com.example.polls.exception.AppException;
import com.example.polls.exception.CustomGraphQLException;
import com.example.polls.model.Poll;
import com.example.polls.model.Role;
import com.example.polls.model.RoleName;
import com.example.polls.model.User;
import com.example.polls.payload.*;
import com.example.polls.repository.RoleRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.JwtTokenProvider;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.PollService;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;

@Component
public class MutationDataFetchers {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PollService pollService;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    HttpSession httpSession;


    public DataFetcher registerUser = env -> {
        //system.out.println("registerUser");
        ObjectMapper mapper = new ObjectMapper();
        try {
            //change the json to object
            //setting restriction
            SignUpRequest signUpRequest = mapper.readValue(env.getContext().toString(), SignUpRequest.class);
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                        HttpStatus.BAD_REQUEST);
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                        HttpStatus.BAD_REQUEST);
            }

            // Creating user's account
            User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                    signUpRequest.getEmail(), signUpRequest.getPassword());

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new AppException("User Role not set."));

            user.setRoles(Collections.singleton(userRole));

            User result = userRepository.save(user);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/users/{username}")
                    .buildAndExpand(result.getUsername()).toUri();

            return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    };


    public DataFetcher authenticateUser = env -> {
        //system.out.println("authenticateUser");
        ObjectMapper mapper = new ObjectMapper();
        try {
            LoginRequest loginRequest = mapper.readValue(env.getContext().toString(), LoginRequest.class);
            //system.out.println(loginRequest.getUsernameOrEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);
            return new JwtAuthenticationResponse(jwt);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadCredentialsException e) {
            throw new CustomGraphQLException(401, "Bad credentials");
        }
        return null;
    };


    public DataFetcher createPoll = env -> {
        //system.out.println("createPollcreatePoll");
        ObjectMapper mapper = new ObjectMapper();
        try {
            PollRequest pollRequest = mapper.readValue(env.getContext().toString(), PollRequest.class);
            Poll poll = pollService.createPoll(pollRequest);
            //system.out.println("poll.getCreateBY)" + poll.getCreatedBy());
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{pollId}")
                    .buildAndExpand(poll.getId()).toUri();

            ApiResponse apiResponse = new ApiResponse(true, "Poll Created Successfully");
            ResponseEntity.created(location)
                    .body(apiResponse);
            return apiResponse;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadCredentialsException e) {
            throw new CustomGraphQLException(401, "Bad credentials");
        }
        return null;
    };


    public DataFetcher castVote = env -> {
        //system.out.println("castVote");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();
        Integer pollId_int = env.getArgument("pollId");
        Long pollId = new Long(pollId_int);
        Integer choiceId_int = env.getArgument("choiceId");
        Long choiceId = new Long(choiceId_int);
        VoteRequest voteRequest = new VoteRequest(choiceId);
        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
    };

}


