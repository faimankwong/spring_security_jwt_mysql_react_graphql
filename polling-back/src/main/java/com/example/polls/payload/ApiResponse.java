package com.example.polls.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Created by rajeevkumarsingh on 19/08/17.
 */
public class ApiResponse {
    private Boolean success;
    private String message;

}
