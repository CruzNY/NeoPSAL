package com.cruzny.neopsal.email;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.net.ConnectException;

@Log4j2
@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("Hello@gamil.com");
            mailSender.send(mimeMessage);
            log.info("Email has been sent.");
        }catch(Exception e){
            log.error(e.getMessage(),e);
            log.error("Email has not been sent");
            // TODO logic to hanlde resending the email when the mail server is eventually brought back up.
        }

    }
}