package com.example.polls.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChoiceVoteCount {
    private Long choiceId;
    private Long voteCount;
}

