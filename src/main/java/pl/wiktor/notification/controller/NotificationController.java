package pl.wiktor.notification.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wiktor.notification.service.EmailSender;
import pl.wiktor.notification.service.NotificationScheduler;
import pl.wiktor.notification.service.NotificationService;

import java.net.MalformedURLException;

@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationScheduler notificationScheduler;
    private final EmailSender emailSender;

    public NotificationController(NotificationService notificationService, NotificationScheduler notificationScheduler,
                                  EmailSender emailSender) {
        this.notificationService = notificationService;
        this.notificationScheduler = notificationScheduler;
        this.emailSender = emailSender;
    }

    @GetMapping(value = "/info", produces = "text/plain")
    public String getNotificationInfo() {
        return notificationService.notificationsInfo();
    }

    @GetMapping("/trigger")
    public void trigger() throws MalformedURLException {
        notificationScheduler.checkItemStatus();
    }

    @GetMapping("/mail-connection-test")
    public String mailConnectionTest() {
        emailSender.sendTestMessage();
        return "MAIL HAS BEEN SENT";
    }
}
