package my.challenge.mafia.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import my.challenge.mafia.chat.xss.HTMLCharacterEscapes;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker // 웹소켓 서버 사용 설정
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {

    // 채팅 전송 내용 중 xss 검열
    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        messageConverters.add(escapingConverter());
        return true;
    }


    private MessageConverter escapingConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());

        MappingJackson2MessageConverter escapingConverter = new MappingJackson2MessageConverter();
        escapingConverter.setObjectMapper(objectMapper);

        return escapingConverter;
    }

    @Override
    // 클라이언트에서 웹 소켓에 접속하는 엔드포인트 등록, interceptor 추가 소켓을 등록
    // StompEndpointRegistry의 STOMP는 스프링의 STOMP 구현체 사용한다는 의미
    // websocket은 통신 프로토콜
    // 특정 주제에 가입한 사용자에게 메시지를 전송하는 기능을 제공하지 않기 때문에 이를 쉽게 사용하기 위해 STOMP를 사용
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 브라우저에서 웹 소켓 미지원 시 withSockJS()를 통해 fallback 옵션 활성화
        registry.addEndpoint("/chat").setAllowedOrigins("*").withSockJS();
    }


    @Override
    // 한 클라이언트에서 다른 클라이언트로 메시지를 라우팅 할 때 사용하는 브로커를 구성
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // /topic, /queue URL subscribe 할때 사용할 URL
        config.enableSimpleBroker("/topic", "/start");

        // /이 publish할 때 사용할 URL
        config.setApplicationDestinationPrefixes("/");

    }


}