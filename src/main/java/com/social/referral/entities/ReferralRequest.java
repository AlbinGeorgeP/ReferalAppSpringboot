package com.social.referral.entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "referral_request")
public class ReferralRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Company company;
    @Column(name = "job_id")
    private String jobId;
    @Column(name = "skill")
    private String skill;
    @Column(name = "position")
    private String position;
    @JoinColumn(name = "requested_by", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private User requestedBy;
    @JoinColumn(name = "taken_by", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private User takenBy;
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "accepted_time")
    private Date acceptedTime;
    @Column(name = "approved_time")
    private Date approvedTime;
    @Column(name = "is_active")
    private int isActive;


}
