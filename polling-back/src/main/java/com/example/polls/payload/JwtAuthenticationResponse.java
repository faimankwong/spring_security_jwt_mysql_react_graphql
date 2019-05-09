package com.example.polls.payload;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by rajeevkumarsingh on 19/08/17.
 */
@Setter
@Getter
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
