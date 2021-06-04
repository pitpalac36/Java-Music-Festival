package festival.client;
import festival.domain.notification.Notification;
import festival.services.NotificationReceiver;
import festival.services.NotificationSubscriber;
import org.springframework.jms.core.JmsOperations;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NotificationReceiverImpl implements NotificationReceiver {

    private JmsOperations jmsOperations;
    private boolean running;
    public NotificationReceiverImpl(JmsOperations operations) {
        jmsOperations=operations;
    }
    ExecutorService service;
    NotificationSubscriber subscriber;

    @Override
    public void start(NotificationSubscriber subscriber) {
        System.out.println("Starting notification receiver ...");
        running=true;
        this.subscriber=subscriber;
        service = Executors.newSingleThreadExecutor();
        service.submit(this::run);
    }

    private void run() {
        while(running){
            System.out.println("Notification receiver running");
            Notification notif = (Notification) jmsOperations.receiveAndConvert();
            System.out.println("Received Notification... " + notif);
            subscriber.notificationReceived(notif);
        }
    }

    @Override
    public void stop() {
        running=false;
        try {
            service.awaitTermination(100, TimeUnit.MILLISECONDS);
            service.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopped notification receiver");
    }
}