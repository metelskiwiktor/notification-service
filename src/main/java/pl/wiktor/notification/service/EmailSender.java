package pl.wiktor.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private final JavaMailSender emailSender;
    private final String gmailUsername;
    private final String gmailRecipient;

    public EmailSender(JavaMailSender emailSender, @Value("${gmail.username}") String gmailUsername,
                       @Value("${gmail.recipient}") String gmailRecipient) {
        this.emailSender = emailSender;
        this.gmailUsername = gmailUsername;
        this.gmailRecipient = gmailRecipient;
    }

    public void sendItemAvailableMessage(String itemUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(gmailUsername);
        message.setTo(gmailRecipient);
        message.setSubject("Przedmiot dostępny");
        message.setText("Przedmiot już dostępny, \nlink: " + itemUrl);
        emailSender.send(message);
    }

    public void sendTestMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(gmailUsername);
        message.setTo(gmailRecipient);
        message.setSubject("Próba temat");
        message.setText("Próba wiadomość");
        emailSender.send(message);
    }
}
