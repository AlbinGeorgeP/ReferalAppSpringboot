package com.social.referral.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("user")
    private String user;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonIgnore
    private String refreshtoken;

}
