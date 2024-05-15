package com.social.referral.dto;

import com.social.referral.entities.Company;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class ReferralRequestDTO {

    private Integer id;
    private String company;
    private String jobId;
    private String skill;
    private String position;
    private String requestedBy;
    private Integer requestedByUserId;
    private String takenBy;
    private Integer takenByUserId;
    private Date createdTime;
    private Date acceptedTime;
    private Date approvedTime;
    private Integer isActive;

}
