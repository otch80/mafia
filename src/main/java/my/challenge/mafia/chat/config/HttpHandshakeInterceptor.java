package my.challenge.mafia.chat.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;


// 웹소켓은 처음 connect 시점에 handshake 라는 작업이 수행
// handshake 과정은 HTTP 통신을 기반으로 이루어지며 GET방식으로 통신
// 이때, HTTP 요청 헤더의 Connection 속성은 Upgrade 로 되어야 함
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    // HTTP에 존재하는 세션 (차후 수정)
    private static final Object SESSION = "test";

    @Override
    // beforeHandshake 는 클라이언트의 연결 요청이 들어오면 3번의 handshake 에서 호출 (3번 실행)
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map attributes) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            attributes.put(SESSION, session);
        }
        return true;
    }

    @Override
    // HandShake 과정 이후 호출
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
    }
}