package my.challenge.mafia.controller;

import my.challenge.mafia.chat.domain.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public String home(){
        return "/chat/home";
    }

    @PostMapping("/chat/room")
    public Message chat(Message message){
        // property의 이름을 이용해서 값을 자동으로 연결 (Controller 특성)
        // 어떻게 경로도 안 알려줬는데 찾아가는지도 모르겠다
        return message;
    }

}
