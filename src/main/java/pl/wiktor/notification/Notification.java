package pl.wiktor.notification;

import java.time.LocalDateTime;

public class Notification {
    private final LocalDateTime createdAt;
    private final boolean available;
    private final boolean mailNotified;
    private final boolean verified;

    public Notification(LocalDateTime createdAt, boolean available, boolean mailNotified, boolean verified) {
        this.createdAt = createdAt;
        this.available = available;
        this.mailNotified = mailNotified;
        this.verified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isAvailable() {
        return available;
    }

    public boolean isMailNotified() {
        return mailNotified;
    }

    public boolean isVerified() {
        return verified;
    }
}
