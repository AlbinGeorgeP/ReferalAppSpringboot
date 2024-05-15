package com.social.referral.services;

import com.social.referral.dto.ClaimRequestDTO;
import com.social.referral.dto.ReferralRequestDTO;
import com.social.referral.entities.Company;
import com.social.referral.entities.ReferralRequest;
import com.social.referral.entities.User;
import com.social.referral.repository.CompanyRepository;
import com.social.referral.repository.ReferralRequestRepository;
import com.social.referral.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReferralRequestService {

    @Autowired
    ReferralRequestRepository referralRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyService companyService;

    public List<ReferralRequestDTO> getReferralRequestsByCompany(Integer company_id) {
        List<ReferralRequest> referralRequests = referralRequestRepository.getReferralRequestsByCompany(company_id);
        List<ReferralRequestDTO> referralRequestsDto = new ArrayList<>();
        for (ReferralRequest request : referralRequests) {
            referralRequestsDto.add(getReferralRequestDto(request));
        }

        return referralRequestsDto;
    }

    @Transactional
    public ResponseEntity<?> addReferralRequest(ReferralRequestDTO inputRequest) throws Exception {
        ReferralRequest referralRequest = getReferralRequestEntity(inputRequest);
        referralRequest.setCreatedTime(new Date());
        referralRequestRepository.save(referralRequest);
        return ResponseEntity.ok().body("Referral request created");
    }


    public ReferralRequestDTO getReferralRequestById(Integer id) throws Exception {
        ReferralRequest referralRequest = referralRequestRepository.findById(id).orElse(new ReferralRequest());
        if (referralRequest.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Referral Not Found");
        return getReferralRequestDto(referralRequest);

    }


    @Transactional
    public ResponseEntity<?> updateReferralRequest(ReferralRequestDTO request) throws Exception {
        if (!referralRequestRepository.existsById(request.getId()))
            throw new Exception("Referral request not found");
        ReferralRequest referralRequest = getReferralRequestEntity(request);
        referralRequestRepository.save(referralRequest);
        return ResponseEntity.ok().body("Referral request updated");

    }

    @Transactional
    public ResponseEntity<?> claimReferralRequest(ClaimRequestDTO request) throws Exception {
        if (request.getRequestId() == request.getUserId())
            throw new Exception("Requested user and taken user cannot be same");
        ReferralRequest referralRequest = referralRequestRepository.findById(request.getRequestId()).orElse(new ReferralRequest());
        User takenUser = userRepository.findById(request.getUserId()).orElse(new User());
        if (referralRequest.getId() == null || takenUser.getId() == null)
            throw new Exception("Record not found");
        referralRequest.setTakenBy(takenUser);
        referralRequest.setAcceptedTime(new Date());
        referralRequestRepository.save(referralRequest);
        return ResponseEntity.ok().body("Claim Successfully done");
    }


    private ReferralRequestDTO getReferralRequestDto(ReferralRequest requestRepo) {
        ReferralRequestDTO resultDto = new ReferralRequestDTO();

        resultDto = resultDto.toBuilder()
                .id(requestRepo.getId())
                .company(requestRepo.getCompany().getName())
                .jobId(requestRepo.getJobId())
                .skill(requestRepo.getSkill())
                .position(requestRepo.getPosition())
                .requestedBy(requestRepo.getRequestedBy().getName())
                .requestedByUserId(requestRepo.getRequestedBy().getId())
                .takenByUserId(requestRepo.getTakenBy().getId())
                .takenBy(requestRepo.getTakenBy().getName())
                .createdTime(requestRepo.getCreatedTime())
                .acceptedTime(requestRepo.getAcceptedTime())
                .approvedTime(requestRepo.getApprovedTime())
                .isActive(requestRepo.getIsActive()).build();
        return resultDto;
    }

    private ReferralRequest getReferralRequestEntity(ReferralRequestDTO request) {
        ReferralRequest repoResult = new ReferralRequest();
        User requestedUser = userRepository.findById(request.getRequestedByUserId()).orElse(new User());
        User takenUser = userRepository.findById(request.getTakenByUserId()).orElse(new User());
        Company company = companyRepository.findByName(request.getCompany());
        if (request.getId() == null)
            repoResult = repoResult
                    .toBuilder()
                    .requestedBy(requestedUser)
                    .acceptedTime(request.getAcceptedTime())
                    .approvedTime(request.getApprovedTime())
                    .company(company)
                    .isActive(request.getIsActive())
                    .jobId(request.getJobId())
                    .position(request.getPosition())
                    .skill(request.getSkill()).build();
        else
            repoResult = repoResult.toBuilder().id(request.getId()).requestedBy(requestedUser).takenBy(takenUser).acceptedTime(request.getAcceptedTime()).approvedTime(request.getApprovedTime()).company(company).isActive(request.getIsActive()).jobId(request.getJobId()).position(request.getPosition()).skill(request.getSkill()).build();
        return repoResult;
    }

}
