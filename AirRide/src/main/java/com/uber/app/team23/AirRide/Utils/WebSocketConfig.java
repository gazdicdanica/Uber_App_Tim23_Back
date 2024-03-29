package com.uber.app.team23.AirRide.Utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/socket").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/sub")
                .enableSimpleBroker("/ride-driver","/ride-cancel" ,"/ride-panic", "/ride-passenger",
                        "/scheduledNotifications", "/map-updates", "/linkPassengers", "/message", "/update-vehicle-location",
                        "/driver-arrived", "/notify15", "/notify5", "/notify10");

    }
}
