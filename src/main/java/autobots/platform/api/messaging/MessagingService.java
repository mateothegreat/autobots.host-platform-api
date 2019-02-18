package autobots.platform.api.messaging;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessagingService(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;

    }

    public void send(Message message) {

        this.rabbitTemplate.convertAndSend(MessagingConfig.EXCHANGE_NAME, MessagingConfig.ROUTING_KEY, message);

    }

    //    @Scheduled(fixedRate = 1000)
//    public void sendMessage() {
//
//        Message<String> message = new Message<>();
//
//        message.setUuid(UUID.randomUUID());
//        message.setPayload("asdfasdf");
//
//        System.out.println("SENDING: " + message.toString());
//        System.out.println("SENDING: " + Thread.activeCount());
//        System.out.println("SENDING: " + Thread.currentThread().getId());
//        send(message);
//        send(message);
//        send(message);
//
//    }

}
