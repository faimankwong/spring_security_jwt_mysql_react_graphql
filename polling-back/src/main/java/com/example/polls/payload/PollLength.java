package com.example.polls.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
@Setter
@Getter
public class PollLength {
    @NotNull
    @Max(7)
    private Integer days;

    @NotNull
    @Max(23)
    private Integer hours;
}
