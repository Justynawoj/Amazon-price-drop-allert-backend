package com.amazon_price_drop_alert.services;

import com.amazon_price_drop_alert.domains.Mail;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailCreatorService emailCreatorService;

    private final Logger LOGGER = LoggerFactory.getLogger(SimpleMailMessage.class);

    public void send(final Mail mail){

        LOGGER.info("Starting email preparation");

        try {
            javaMailSender.send(createMimeMessage(mail));
            LOGGER.info("Email has been sent.");

        }catch (MailException e){
            LOGGER.error("Failed to process email sending: ",e.getMessage(),e);
        }
    }

    private MimeMessagePreparator createMimeMessage(final Mail mail) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setTo(mail.getMailTo());
            messageHelper.setSubject(mail.getSubject());
            messageHelper.setText(emailCreatorService.buildEmail(mail.getMessage(),mail.getRequest(),mail.getProductDetailsDto()), true);
        };
    }


//
//    private SimpleMailMessage createMailMessage(final Mail mail) {
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(mail.getMailTo());
//        mailMessage.setSubject(mail.getSubject());
//        mailMessage.setText(emailCreatorService.buildEmail(mail.getMessage()));
//        return mailMessage;
//    }

}
