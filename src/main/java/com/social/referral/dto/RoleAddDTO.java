package com.social.referral.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class RoleAddDTO {
    @NonNull
    private String UserId;
    @NonNull
    private String Role;
}
