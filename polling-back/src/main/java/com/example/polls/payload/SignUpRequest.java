package com.example.polls.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * Created by rajeevkumarsingh on 02/08/17.
 */
@Setter
@Getter
public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 40)
    @JsonProperty("name")
    private String name;

    @NotBlank
    @Size(min = 3, max = 15)
    @JsonProperty("username")
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    @JsonProperty("email")
    private String email;

    @NotBlank
    @JsonProperty("password")
    @Size(min = 6, max = 20)
    private String password;

}
