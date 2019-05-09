package com.example.polls.payload;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Setter
@Getter
public class ChoiceRequest {
    @NotBlank
    @Size(max = 40)
    private String text;

}
