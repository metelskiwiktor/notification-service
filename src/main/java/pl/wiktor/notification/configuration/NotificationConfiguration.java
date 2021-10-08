package pl.wiktor.notification.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

@Configuration
public class NotificationConfiguration {
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Paris")));
    }

    @Bean
    public JavaMailSender getJavaMailSender(@Value("${gmail.username}") String gmailUsername, @Value("${gmail.password}") String gmailPassword) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(gmailUsername);
        mailSender.setPassword(gmailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        Locale locale = new Locale("pl", "PL");
        sessionLocaleResolver.setDefaultLocale(locale);
        return sessionLocaleResolver;
    }
}
