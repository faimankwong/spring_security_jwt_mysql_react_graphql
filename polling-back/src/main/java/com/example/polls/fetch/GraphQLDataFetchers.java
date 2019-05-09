package com.example.polls.fetch;

import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.User;
import com.example.polls.payload.UserIdentityAvailability;
import com.example.polls.payload.UserProfile;
import com.example.polls.payload.UserSummary;
import com.example.polls.repository.PollRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.repository.VoteRepository;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.PollService;
import com.example.polls.util.AppConstants;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GraphQLDataFetchers {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private PollService pollService;
    public DataFetcher getAllUser = env ->  userRepository.findAll();


    public DataFetcher checkUsernameAvailability = env ->  {
        //system.out.println("checkUsernameAvailability");
        String username =env.getArgument("username");
        //system.out.println("username"+  env.getArguments());
        Boolean isAvailable = !userRepository.existsByUsername(username);
        //system.out.println("isAvailable "+  isAvailable);
        return new UserIdentityAvailability(isAvailable);
    };

    public DataFetcher checkEmailAvailability = env ->  {
        //system.out.println("checkEmailAvailability");
        String username =env.getArgument("email");
        Boolean isAvailable = !userRepository.existsByEmail(username);
        return new UserIdentityAvailability(isAvailable);
    };

    public DataFetcher getCurrentUser = (env) ->  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal currentUser=null;
        if (!(authentication instanceof AnonymousAuthenticationToken))
            currentUser=(UserPrincipal)authentication.getPrincipal();
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    };

    public DataFetcher getPolls = (env) ->  {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //system.out.println("getPollsgetPollsgetPolls");
        UserPrincipal currentUser=null;
        if (!(authentication instanceof AnonymousAuthenticationToken))
            currentUser=(UserPrincipal)authentication.getPrincipal();
        int page =Integer.parseInt((String)Optional.ofNullable(env.getArgument("page")).orElse(AppConstants.DEFAULT_PAGE_NUMBER));
        int size =Integer.parseInt((String)Optional.ofNullable(env.getArgument("size")).orElse(AppConstants.DEFAULT_PAGE_SIZE));
        return pollService.getAllPolls(currentUser, page, size);

    };
    public DataFetcher getUserProfile = (env) ->  {
        String username=env.getArgument("username");
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long pollCount = pollRepository.countByCreatedBy(user.getId());
        long voteCount = voteRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), pollCount, voteCount);

        return userProfile;
    };


    public DataFetcher getPollsCreatedBy = (env) ->  {
        String username=env.getArgument("username");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //system.out.println("getPollsCreatedBy");
        UserPrincipal currentUser=null;
        if (!(authentication instanceof AnonymousAuthenticationToken))
            currentUser=(UserPrincipal)authentication.getPrincipal();
        int page =Integer.parseInt((String)Optional.ofNullable(env.getArgument("page")).orElse(AppConstants.DEFAULT_PAGE_NUMBER));
        int size =Integer.parseInt((String)Optional.ofNullable(env.getArgument("size")).orElse(AppConstants.DEFAULT_PAGE_SIZE));
        return pollService.getPollsCreatedBy(username, currentUser, page, size);
    };

    public DataFetcher getPollsVotedBy = (env) ->  {
        String username=env.getArgument("username");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //system.out.println("getPollsVotedBy");
        UserPrincipal currentUser=null;
        if (!(authentication instanceof AnonymousAuthenticationToken))
            currentUser=(UserPrincipal)authentication.getPrincipal();
        int page =Integer.parseInt((String)Optional.ofNullable(env.getArgument("page")).orElse(AppConstants.DEFAULT_PAGE_NUMBER));
        int size =Integer.parseInt((String)Optional.ofNullable(env.getArgument("size")).orElse(AppConstants.DEFAULT_PAGE_SIZE));

        return pollService.getPollsVotedBy(username, currentUser, page, size);
    };



//    @PreAuthorize("hasRole('USER')")
////    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
////        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
////        return userSummary;
////    }

}


