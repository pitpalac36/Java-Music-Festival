package festival.services;

import festival.domain.notification.Notification;

public interface NotificationSubscriber {
    void notificationReceived(Notification notif);
}