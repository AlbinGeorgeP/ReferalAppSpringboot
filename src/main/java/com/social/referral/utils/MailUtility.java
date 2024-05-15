package com.social.referral.utils;

import com.social.referral.entities.Email;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import jakarta.mail.internet.MimeMessage;
@Component
public class MailUtility {

    @Autowired
    private JavaMailSender javaMailSender;
    public void sendMail(Email mail)
    {
        try {
            MimeMessage mailMessage = javaMailSender.createMimeMessage();
            mailMessage.setFrom(Environment.getProperties().getProperty("spring.mail.username"));
            mailMessage.setRecipients(MimeMessage.RecipientType.TO, mail.getRecipients());
            mailMessage.setSubject(mail.getSubject());
            mailMessage.setContent(mail.getBody(), "text/html; charset=utf-8");
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("Mail triggering failed for Recipient :"+mail.getRecipients());
        }
    }
}
