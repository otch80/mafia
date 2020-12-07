package my.challenge.mafia.chat.controller;


import my.challenge.mafia.chat.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;


@org.springframework.stereotype.Controller
public class ChattingController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;



    // 각자 채팅방으로 전송
    // 각 방 ID 별로 /room으로 전송되는 채팅 처리
    @MessageMapping("/room/{id}")
	@SendTo("/topic/{id}")
    public Message room(Message message) throws Exception {
        return message;
    }

    // 게임시작
    @MessageMapping("/room/{id}/start")
    @SendTo("/start/{id}")
    public Message start(Message message) throws Exception {
        message.setMsg("initGame();");
        return message;
    }

    // 각자 채팅방으로 전송
    // 각 방 ID 별로 /room으로 전송되는 채팅 처리
    @MessageMapping("/room/{id}/mafia")
    @SendTo("/topic/{id}/mafia")
    public Message mafia_chat(Message message) throws Exception {
        return message;
    }

}

//    // 메세지 전송 경로
//    @MessageMapping("info")
//    // Send To User 는 1:1 통신 (보통 queue로 시작)
//    @SendToUser("/queue/info")
//    public String info(String message, SimpMessageHeaderAccessor messageHeaderAccessor) {
//        User talker = messageHeaderAccessor.getSessionAttributes().get(SESSION).get(USER_SESSION_KEY);
//        return message;
//    }
//
//    @MessageMapping("chat")
//    // Send To 는 1:n 통신 (보통 topic으로 시작)
//    @SendTo("/topic/message")
//    public String chat(String message, SimpMessageHeaderAccessor messageHeaderAccessor) {
//        User talker = messageHeaderAccessor.getSessionAttributes().get(SESSION).get(USER_SESSION_KEY);
//        if(talker == null) throw new UnAuthenticationException("로그인한 사용자만 채팅에 참여할 수 있습니다.");
//        return message;
//    }
//
//    @MessageMapping("bye")
//    @SendTo("/topic/bye")
//    public User bye(String message) {
//        User talker = messageHeaderAccessor.getSessionAttributes().get(SESSION).get(USER_SESSION_KEY);
//        return talker;
//    }
