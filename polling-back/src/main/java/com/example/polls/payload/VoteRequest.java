package com.example.polls.payload;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
@Setter
@Getter
@AllArgsConstructor
public class VoteRequest {
    @NotNull
    private Long choiceId;
}

