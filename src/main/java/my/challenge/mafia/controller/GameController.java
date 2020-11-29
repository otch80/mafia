package my.challenge.mafia.controller;


import my.challenge.mafia.domain.InitRoomInfo;
import my.challenge.mafia.domain.RoomInfo;
import my.challenge.mafia.room.RoomManager;
import my.challenge.mafia.room.User;
import my.challenge.mafia.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

// checkinghello
// chekkkkkk 3333333
// ㅇㅇㅇㅇ
@Controller
public class GameController {


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RoomManager roomManager;


    /*
     ####################### 수정 중 ################################


    */



    @GetMapping("/game")
    public String home() {
        return "/game/day";
    }

    // 방 입장
    @PostMapping("/enter") // URL 혹은 특정한 방법으로 방번호를 전달한다. ################ 차후 수정 필요
    public ModelAndView enterRoom(ModelAndView model, @RequestParam("num") int roomNumber, HttpServletRequest request, HttpServletResponse response){
        // 프론트에서 토큰 정보를 안직 받지 못한 차후 수정 필요
        String token = new String();
         for(int i = 0; i < request.getCookies().length; i++){
             if(request.getCookies()[i].getName().equals("JW-TOKEN")){
                 token = request.getCookies()[i].getValue().substring(6);
             }
         }
         String username = jwtTokenUtil.getUsernameFromToken(token);

        User user = new User(username); // 유저 이름을 가져와서 User객체를 생성한다. 임시로 asdf로 설정한다



        // 방 입장 성공
        if(roomManager.enterRoom(roomNumber, user)){
            List<String> userlist = roomManager.getUserList(roomNumber); // 현재 접속중인 유저 리스트

            model.setViewName("/game/day");
            model.addObject("roomNumber",roomNumber);
            model.addObject("username",username);
            model.addObject("userlist",userlist);
            //model.addObject("userlistLength",userlist.size());

            return model;
        }// 방 입장 실패
        else{
            // 방 입장에 실패하였습니다~~~ 라는 알림문을 띄울 수 있음
            model.setViewName("/home/ready");
            return model;

        }
    }

    // 방 생성
    @PostMapping("/makeRoom") // ################ 차후 수정 필요
    public ResponseEntity createRoom(@RequestBody InitRoomInfo initRoomInfo, HttpServletRequest request, HttpServletResponse response){
        int roomNumber = parseInt(initRoomInfo.getRoom_number()); // 프론트에서 방 정보 전달받음

        // 클라이언트에서 토큰 정보를 아직 보내주지 않음
        // 토큰 정보를 보내주면 그 부분 처리 다시 해야함###############################################################################
        //String token = request.getCookies()[0].getValue().substring(6);

        // 유저 이름을 가져와서 User객체를 생성한다.(방장)
        // 임시로 asdf로 유저이름 설정 차후 수정 필요 #################################################################
        User user = new User("asdf", true);



        // 방 생성
        if(roomManager.makeRoom(roomNumber, user)){
            System.out.println("I'm here");// 확인을 위한 녀석
            return new ResponseEntity("success", HttpStatus.OK);
        }else{
            // 방 생성에 실패하였습니다~~~ 라는 알림문을 띄울 수 있음
            return new ResponseEntity("fail", HttpStatus.BAD_REQUEST);
        }
    }

    /*
    * ############################################### 프론트 상에서 아직 미구현 #######################################################
    * ############################################### 프론트 상에서 아직 미구현 #######################################################
    * */

    // 방 퇴장
    @PostMapping("/exit") // ################ 차후 수정 필요
    public String exitRoom(HttpServletRequest request, HttpServletResponse response){
        int roomNumber = 1; // 프론트에서 전달 받아야 한다.
        String token = request.getCookies()[0].getValue().substring(6);
        User user = new User(jwtTokenUtil.getUsernameFromToken(token)); // 유저 이름을 가져와서 User객체를 생성한다.

        if(roomManager.exitUser(roomNumber, user)){
            return "/exitSuccess";
        }else{
            // 퇴장에 실패했습니다 다시 시도해주세요 알림문 가능
            return "/exitFail";
        }
    }

    // 게임 시작
    public String startGame(HttpServletRequest request, HttpServletResponse response){
        int roomNumber = 1; // 프론트에서 전달 받아야 한다.
        int userNumber = 8; // 프론트에서 전달 받는다.
        User[] users = new User[userNumber]; // 프론트에서 유저 리스트를 받아온다.

        if(roomManager.startGame(roomNumber, users)){
            return "/startSuccess";
        }else{
            // 게임 시작에 실패했습니다. 다시 시도해 주세요 알림문 가능
            return "/startFail";
        }
    }

    @ResponseBody
    @PostMapping("/enter/{roomid}") // 게임시작 시 ajax 통신 처리용
    public Map<String, Object> inGame(@RequestParam("roomNumber") String roomNumber){
        System.out.println("========eehehehehehehe");
        Map<String, Object> map = new HashMap<>();

        List<String> userlist = roomManager.getUserList(parseInt(roomNumber));
        map.put("playerNum", userlist.size());

        return map;
    }


    // 게임 종료
    public String endGame(HttpServletRequest request, HttpServletResponse response) {
        int roomNumber = 1; // 프론트에서 전달 받아야 한다.

        String winGroup = "mafia"; // 프론트에서 승리 팀 정보 전달받는다.

        if(roomManager.endGame(roomNumber, winGroup)){
            return "/endSuccess";
        }else{
            return "/endSuccess";
        }

    }

    // 유저 사망
    public String die(HttpServletRequest request, HttpServletResponse response) {
        int roomNumber = 1; // 프론트에서 전달 받아야 한다.


        return "/die";
    }

    // 유저 퇴장
    public String exitUser(HttpServletRequest request, HttpServletResponse response) {
        int roomNumber = 1; // 프론트에서 전달 받아야 한다.

        return "/exitUser";
    }

}
