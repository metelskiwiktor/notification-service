package pl.wiktor.notification.service;

import org.springframework.stereotype.Service;
import pl.wiktor.notification.Notification;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final List<Notification> notifications = new ArrayList<>();
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM HH:mm");

    public void saveNotification(boolean available, boolean mailNotified, boolean verified) {
        notifications.add(new Notification(LocalDateTime.now(), available, mailNotified,
                verified));
    }

    public String notificationsInfo() {
        return notifications.stream()
                .sorted(Comparator.comparing(Notification::getCreatedAt).reversed())
                .map(notification -> {
                    String status = notification.isAvailable() ? "dostępny" : "brak możliwości kupna";
                    String notified = notification.isMailNotified() ? "tak" : "nie";
                    String verified = notification.isVerified() ? "tak" : "nie";
                    return String.format("%s -> status: %s, powiadomiono mailowo: %s, zweryfikowano przedmiot: %s",
                            dateFormat.format(notification.getCreatedAt()), status, notified, verified);
                }).collect(Collectors.joining("\n"));
    }

    public boolean alreadySentNotification() {
        return notifications.stream()
                .anyMatch(Notification::isMailNotified);
    }
}
