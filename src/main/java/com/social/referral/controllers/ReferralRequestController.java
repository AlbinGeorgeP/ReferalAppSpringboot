package com.social.referral.controllers;


import com.social.referral.dto.ClaimRequestDTO;
import com.social.referral.dto.ReferralRequestDTO;
import com.social.referral.services.ReferralRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ReferralService")
public class ReferralRequestController {


    @Autowired
    ReferralRequestService referralRequestService;

    //GET Referral request by company
    @GetMapping(value="/ReferralRequest/Company/{id}",produces = "application/json")
    public List<ReferralRequestDTO> getReferralRequestsByCompany(@PathVariable(value = "id") Integer companyId){
        return  referralRequestService.getReferralRequestsByCompany(companyId);
    }
    //Get Referral Request by ID
    @GetMapping(value="/ReferralRequest/{id}",produces = "application/json")
    public ReferralRequestDTO getReferralRequestsById(@PathVariable(value = "id") Integer ReferralId) throws Exception {
        return  referralRequestService.getReferralRequestById(ReferralId);
    }
    //Claim Referral Request
    @PostMapping(value="/ReferralRequest/Claim",produces = "application/json")
    public ResponseEntity<?> claimReferralRequest(@RequestBody ClaimRequestDTO request)
    {
        try
        {
            return referralRequestService.claimReferralRequest(request);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //POST Referral request
    @PostMapping(value="/ReferralRequest",produces = "application/json",consumes = "application/json")
    public ResponseEntity<?> addReferralRequest(@RequestBody ReferralRequestDTO inputRequest){
        try {
            return referralRequestService.addReferralRequest(inputRequest);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    //PUT Referral request
    @PutMapping(value="/ReferralRequest",produces = "application/json",consumes = "application/json")
    public ResponseEntity<?> updateReferralRequest(@RequestBody ReferralRequestDTO inputRequest){
        try {
            return referralRequestService.updateReferralRequest(inputRequest);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
