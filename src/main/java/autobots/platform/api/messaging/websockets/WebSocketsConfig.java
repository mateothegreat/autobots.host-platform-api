package autobots.platform.api.messaging.websockets;

import autobots.platform.api.users.UserLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.socket.config.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketsConfig extends WebSocketMessageBrokerConfigurationSupport implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        config.enableStompBrokerRelay("/topic", "/user", "/queue").setRelayHost("rabbitmq").setRelayPort(61613).setClientLogin("guest").setClientPasscode("guest");
        config.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/socket").setAllowedOrigins("https://app.autobots.host", "https://admin.autobots.host", "http://localhost:4200").withSockJS().setInterceptors(httpHandshakeInterceptor());

    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {

        super.configureWebSocketTransport(registry);

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    Optional.ofNullable(accessor.getNativeHeader("Authorization")).ifPresent(ah -> {

                        String bearerToken = ah.get(0).replace("Bearer ", "");

                        try {

                            Jws<Claims> jws = Jwts.parser().setSigningKey(UserLogin.SECRET).parseClaimsJws(bearerToken);

                            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//                        grantedAuthorities.add(new SimpleGrantedAuthority(optionalUser.get().role));

                            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jws.getBody().getSubject(), "password", grantedAuthorities);

                            accessor.setUser(auth);

                        } catch (SignatureException e) {

                            System.out.println(e);

                        }

                    });

                }

                return message;

            }

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

            }

            @Override
            public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {

            }

            @Override
            public boolean preReceive(MessageChannel channel) {
                return false;
            }

            @Override
            public Message<?> postReceive(Message<?> message, MessageChannel channel) {
                return null;
            }

            @Override
            public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {

            }

        });

    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {

        super.configureClientOutboundChannel(registration);

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

        super.addArgumentResolvers(argumentResolvers);

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

        super.addReturnValueHandlers(returnValueHandlers);

    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {

        return super.configureMessageConverters(messageConverters);

    }

    @Bean
    public HttpHandshakeInterceptor httpHandshakeInterceptor() {

        return new HttpHandshakeInterceptor();

    }

//    public WebSocketHandler subProtocolWebSocketHandler() {
//
//        return new WebSocketsSubProtocolHandler(clientInboundChannel(), clientOutboundChannel());
//
//    }

}
