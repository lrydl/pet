package com.lry.config;

import com.lry.handler.SpringWebSocketHandler;
import com.lry.interceptor.SpringWebSocketHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author:刘仁有
 * @desc:
 * @email:953506233@qq.com
 * @data:2019/10/24
 */
@Configuration
@EnableWebSocket
public class SpringWebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(),"/ws/server")
                .addInterceptors(new SpringWebSocketHandlerInterceptor()).setAllowedOrigins("*");

        registry.addHandler(webSocketHandler(), "/sockjs/server").setAllowedOrigins("http://192.168.31.25:9090/NoneConfig_war_exploded")
                .addInterceptors(new SpringWebSocketHandlerInterceptor()).withSockJS();
    }
    @Bean
    public TextWebSocketHandler webSocketHandler(){
        return new SpringWebSocketHandler();
    }
}
