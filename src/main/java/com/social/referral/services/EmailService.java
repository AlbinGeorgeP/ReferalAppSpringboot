package com.social.referral.services;

import com.social.referral.entities.Email;
import com.social.referral.entities.ReferralRequest;
import com.social.referral.entities.User;
import com.social.referral.repository.ReferralRequestRepository;
import com.social.referral.utils.MailUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
@Service
public class EmailService{

    @Autowired
    MailUtility referralUtility;
    @Autowired
    ReferralRequestRepository referralRepo;

    //Sends mails to all users where approval is null and current date is more than created date + 2 days
    @Transactional
    public void sendApprovalNotification() throws IOException {
        List<ReferralRequest> referralRequests = referralRepo.findAllNonApprovedReferralRequests();
        for (ReferralRequest request : referralRequests) {
            User user = request.getTakenBy();
            String body = new String(Files.readAllBytes(Paths.get("C:\\Users\\albgeorge\\Downloads\\Referral-System\\src\\main\\resources\\template\\ApprovalNotificationTemplate.html")));
            body=body.replace("${jobid}",request.getJobId()).replace("${company}",request.getCompany().getName()).replace("${recipient}", user.getName()).replace("${rid}", request.getId().toString()).replace("${ruser}", request.getRequestedBy().getName());
            Email mail = new Email(user.getEmail(), body, "Referral Approval Notification");
            referralUtility.sendMail(mail);
        }

    }
}
