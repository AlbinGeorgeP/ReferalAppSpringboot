package com.social.referral.controllers;
import com.social.referral.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@EnableScheduling
public class EmailController {
    @Autowired
    EmailService emailService;
    @Scheduled(cron = "${Notification.Approval.Schedule}")
    public void sendApprovalNotificationMail() throws IOException {
        emailService.sendApprovalNotification();
    }
}
