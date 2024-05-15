package com.social.referral.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String name;
    private String email;
    private String mobileNo;
    private String tag;
    private Date createdTime;
    private Date deactivatedTime;
    private Integer isActive;
    private String company;
    private String password;
}
