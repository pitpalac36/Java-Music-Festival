package festival.services;

public interface NotificationReceiver {
    void start(NotificationSubscriber subscriber);
    void stop();
}