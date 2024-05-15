package com.social.referral.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name="user_company_view")
public class UserCompanyView {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "deactivated_time")
    private Date deactivatedTime;
    @Column(name="company_name")
    private String company;
    @Column(name = "is_active")
    private Integer isActive;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "mobile_no")
    private String mobileNo;
    @Column(name = "tag")
    private String tag;
    @Column(name="role")
    private String role;






}
