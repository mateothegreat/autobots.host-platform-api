package autobots.platform.api.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    public static final String EXCHANGE_NAME       = "autobots-exchange";
    public static final String QUEUE_GENERIC_NAME  = "autobots-queue";
    public static final String QUEUE_SPECIFIC_NAME = "autobots-queue-specific";
    public static final String ROUTING_KEY         = "autobots-routing-key";

    @Bean
    public TopicExchange appExchange() {

        return new TopicExchange(EXCHANGE_NAME);

    }

    @Bean
    public Queue appQueueGeneric() {

        return new Queue(QUEUE_GENERIC_NAME);

    }

    @Bean
    public Queue appQueueSpecific() {

        return new Queue(QUEUE_SPECIFIC_NAME);

    }

    @Bean
    public Binding declareBindingGeneric() {

        return BindingBuilder.bind(appQueueGeneric()).to(appExchange()).with(ROUTING_KEY);

    }

    @Bean
    public Binding declareBindingSpecific() {

        return BindingBuilder.bind(appQueueSpecific()).to(appExchange()).with(ROUTING_KEY);

    }

    @Bean
    public MessageConverter jsonMessageConverter() {

        return new Jackson2JsonMessageConverter();

    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        template.setMessageConverter(jsonMessageConverter());

        return template;

    }

}
