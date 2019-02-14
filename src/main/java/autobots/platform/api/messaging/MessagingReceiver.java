package autobots.platform.api.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingReceiver {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessagingReceiver(final RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;

    }

    @RabbitListener(queues = MessagingConfig.QUEUE_GENERIC_NAME)
    public void receiveMessage(Message customMessage) {

        System.out.println("RECEIVE: " + Thread.activeCount());
        System.out.println("RECEIVE: " + Thread.currentThread().getId());
        System.out.println("RECEIVE: " + customMessage.toString());

    }

}
