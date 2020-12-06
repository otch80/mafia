package my.challenge.mafia.controller;


import my.challenge.mafia.domain.InitRoomInfo;
import my.challenge.mafia.domain.Player;
import my.challenge.mafia.room.RoomManager;
import my.challenge.mafia.room.User;
import my.challenge.mafia.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static java.lang.Integer.parseInt;


@Controller
public class GameController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RoomManager roomManager;

    // 유저에게 랜덤하게 역할을 분배하기 위해 프론트에서 전달받은 역할 배열을 섞는 함수
    List<String> shuffle(List<String> players) { //현재 플레이어 배열을 매개변수로 가져옴, 무작위로 섞는 함수
        double j; //랜덤 함수넣을 변수
        String x; //빈값 변수
        int i; //매개변수로 받아온 변수 저장할 변수
        for (i = players.size(); i == 0; i--) {
            j = Math.floor(Math.random() * i);
            x = players.get(i - 1);
            players.set(i - 1, players.get((int) j));
            players.set((int) j, x);
        } //이 부분은 https://malonmiming.tistory.com/106 해당 링크를 많이 참조했음. 시간복잡도상 가장 효율적이라 판단
        return players;
    }


    @GetMapping("/game")
    public String home() {
        return "/game/day";
    }

    // 방 입장
    @PostMapping("/enter") // URL 혹은 특정한 방법으로 방번호를 전달한다. ################ 차후 수정 필요
    public ModelAndView enterRoom(ModelAndView model, @RequestParam("num") int roomNumber, HttpServletRequest request, HttpServletResponse response) {
        // 프론트에서 토큰 정보를 안직 받지 못한 차후 수정 필요
        String token = new String();
        for (int i = 0; i < request.getCookies().length; i++) {
            if (request.getCookies()[i].getName().equals("JW-TOKEN")) {
                token = request.getCookies()[i].getValue().substring(6);
            }
        }
        String username = jwtTokenUtil.getUsernameFromToken(token);

        User user = new User(username); // 유저 이름을 가져와서 User객체를 생성한다. 임시로 asdf로 설정한다

        // 방 입장 성공
        if (roomManager.enterRoom(roomNumber, user)) {
            List<String> userlist = roomManager.getUserList(roomNumber); // 현재 접속중인 유저 리스트

            model.setViewName("/game/day");
            model.addObject("roomNumber", roomNumber);
            model.addObject("username", username);
            model.addObject("userlist", userlist);
            //model.addObject("userlistLength",userlist.size());

            return model;
        }// 방 입장 실패
        else {
            // 방 입장에 실패하였습니다~~~ 라는 알림문을 띄울 수 있음
            model.setViewName("/home/ready");
            return model;
        }
    }

    // 방 생성
    @PostMapping("/makeRoom") // ################ 차후 수정 필요
    public ResponseEntity createRoom(@RequestBody InitRoomInfo initRoomInfo, HttpServletRequest request, HttpServletResponse response) {
        int roomNumber = parseInt(initRoomInfo.getRoom_number()); // 프론트에서 방 정보 전달받음

        // 클라이언트에서 토큰 정보를 아직 보내주지 않음
        // 토큰 정보를 보내주면 그 부분 처리 다시 해야함###############################################################################
        //String token = request.getCookies()[0].getValue().substring(6);

        // 유저 이름을 가져와서 User객체를 생성한다.(방장)
        // 임시로 asdf로 유저이름 설정 차후 수정 필요 #################################################################
        User user = new User("asdf", true);


        // 방 생성
        if (roomManager.makeRoom(roomNumber, user)) {
            System.out.println("I'm here");// 확인을 위한 녀석
            return new ResponseEntity("success", HttpStatus.OK);
        } else {
            // 방 생성에 실패하였습니다~~~ 라는 알림문을 띄울 수 있음
            return new ResponseEntity("fail", HttpStatus.BAD_REQUEST);
        }
    }

    // 방 퇴장
    @PostMapping("/exit") // ################ 차후 수정 필요
    public String exitRoom(HttpServletRequest request, HttpServletResponse response) {
        int roomNumber = 1; // 프론트에서 전달 받아야 한다.
        String token = request.getCookies()[0].getValue().substring(6);
        User user = new User(jwtTokenUtil.getUsernameFromToken(token)); // 유저 이름을 가져와서 User객체를 생성한다.

        if (roomManager.exitUser(roomNumber, user)) {
            return "/exitSuccess";
        } else {
            // 퇴장에 실패했습니다 다시 시도해주세요 알림문 가능
            return "/exitFail";
        }
    }

    // 게임 시작 ajax 통신
    @ResponseBody
    @PostMapping("/enter/{roomid}") // 게임시작 시 ajax 통신 처리용
    public Map<String, Object> inGame(@RequestParam("roomNumber") String roomNumber) {
        System.out.println("========eehehehehehehe");
        Map<String, Object> map = new HashMap<>();

        List<String> userlist = roomManager.getUserList(parseInt(roomNumber));
        map.put("playerNum", userlist.size());
        map.put("players", userlist);
        roomManager.startGame(parseInt(roomNumber));

        return map;
    }

    // 역할 할당 ajax 통신
    @ResponseBody
    @PostMapping("/assign/{roomid}")
    public Map<String, Object> assignRole(@PathVariable("roomid") String roomid, @RequestParam(value = "playerRoles[]") List<String> playerRoles) {
        Map<String, Object> map = new HashMap<>();
        playerRoles = shuffle(playerRoles); // 유저 능력 랜덤 설정
        List<String> userlist = roomManager.getUserList(parseInt(roomid));

        // 유저이름과 직업을 매핑한 리스트를 생성하는 반복문
        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < userlist.size(); i++) {
            Player player = new Player();
            player.setUserId(userlist.get(i));
            player.setJobName(playerRoles.get(i));
            playerList.add(player);
        }

        // 서버에 유저 정보 초기화
        HashMap<String, User> userListHash = roomManager.getUserListHash(parseInt(roomid));
        int len = playerList.size();
        for(int i = 0; i < len; i++){
            User tmpUser = new User(playerList.get(i).userId);
            tmpUser.setRole(playerList.get(i).jobName);
            if(playerList.get(i).jobName.equals("mafia") || playerList.get(i).jobName.equals("spy")){
                tmpUser.setGroup("citizen");
            }else{
                tmpUser.setGroup("mafia");
            }
            userListHash.put(playerList.get(i).userId, tmpUser);

        }

        map.put("players", playerList);
        return map;
    }

    // 투표 수행 ajax 통신
    @ResponseBody
    @PostMapping("/vote/{roomid}")
    public Map<String, Object> vote(@PathVariable("roomid") int roomid, @RequestParam(value = "votedPlayer") String playerName) {
        Map<String, Object> map = new HashMap<>();
        try {
            int voteCount = 999;
            //voteCount = roomManager.getVoteCount(Integer.parseInt(roomid));
            roomManager.voteUser(roomid, playerName);

            List<Integer> tmp1 = roomManager.getVoteList(roomid);
            List<String> tmp2 = roomManager.getVoteUserNameList(roomid);
            System.out.println("++++++++++++++++++++++++++++++++");
            for(int i = 0; i < tmp1.size(); i++){
                System.out.println(tmp1.get(i));
            }
            System.out.println("++++++++++++++++++++++++++++++++");
            for(int i = 0; i < tmp1.size(); i++){
                System.out.println(tmp2.get(i));
            }
            System.out.println("++++++++++++++++++++++++++++++++");
            return map;
        } catch (Exception e) {
            System.out.println(e);
        }
        return map;
    }

    // 투표 결과 리턴 ajax 통신
    @ResponseBody
    @PostMapping("/votefinal/{roomid}")
    public Map<String, Object> returnVote(@PathVariable("roomid") String roomid) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<String> voteUserNameList = roomManager.getVoteUserNameList(Integer.parseInt(roomid));
            List<Integer> voteList = roomManager.getVoteList(Integer.parseInt(roomid));

            int len = voteList.size();
            int max_voted_user_index = 0; // 최다 득표 유저 인덱스
            boolean same_voted = false; // 최다 득표자가 2명 이상이면 true

            // 최다 득표 유저 선정
            for (int i = 1; i < len; i++) {
                if (voteList.get(i) > voteList.get(max_voted_user_index)) {
                    max_voted_user_index = i;
                }
            }

            // 최다 득표 유저가 2명 이상이진 확인
            for (int i = 0; i < len; i++) {
                if (voteList.get(i) == voteList.get(max_voted_user_index)) {
                    if (i != max_voted_user_index) {
                        same_voted = true;
                    }
                }
            }

            // 최다 득표 유저가 2명 이상이 아닐 경우, 최다 득표 유저 이름 리턴
            if (same_voted != true) {
                map.put("kingVotedPlayer", voteUserNameList.get(max_voted_user_index));
            }
            return map;
        } catch (Exception e) {
            System.out.println(e);
            return map;
        }
    }


    // 경찰 능력 사용 - ajax 통신
    @ResponseBody
    @PostMapping("/cop/{roomid}")
    public Map<String, Object> policeAbility(@PathVariable("roomid") int roomid, @RequestParam("choicePlayer") String targetPlayer){
        Map<String, Object> map = new HashMap<>();
        System.out.println("=============" + targetPlayer);
        String target = roomManager.policeAbility(roomid, targetPlayer);
        System.out.println(target);
        map.put("job", target);
        return map;
    }

    // 스파이 능력 사용 - ajax 통신
    @ResponseBody
    @PostMapping("/spy/{roomid}")
    public Map<String, Object> spyAbility(@PathVariable("roomid") int roomid, @RequestParam("choicePlayer") String targetPlayer, @RequestParam("srcPlayer") String srcPlayer){
        Map<String, Object> map = new HashMap<>();
        String targetRole = roomManager.spyAbility(roomid, targetPlayer, srcPlayer);

        map.put("job", targetRole);
        return map;
    }

    // 마피아 능력 사용 - 죽이기 투표
    @ResponseBody
    @PostMapping("/maifavote/{roomid}")
    public Map<String, Object> maifaVote(@PathVariable("roomid") String roomid, @RequestParam(value = "votedPlayer") String playerName) {
        Map<String, Object> map = new HashMap<>();
        try {
            int voteCount = 999;
            //voteCount = roomManager.getVoteCount(Integer.parseInt(roomid));
            roomManager.voteUser(Integer.parseInt(roomid), playerName);
            return map;
        } catch (Exception e) {
            System.out.println(e);
        }
        return map;
    }

    // 의사 능력 사용 - ajax 통신
    @ResponseBody
    @PostMapping("/doctor/{roomid}")
    public Map<String, Object> doctorAbility(@PathVariable("roomid") int roomid, @RequestParam("choicePlayer") String targetPlayer){
        Map<String, Object> map = new HashMap<>();
        roomManager.doctorAbility(roomid, targetPlayer);
        return map;
    }




    // 밤이 끝나고 죽은 사람이 누구인지 리턴
    @ResponseBody
    @PostMapping("/mafiavotefinal/{roomid}")
    public Map<String, Object> mafiaVoteFinal(@PathVariable("roomid") int roomid) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<String> voteUserNameList = roomManager.getVoteUserNameList(roomid);
            List<Integer> voteList = roomManager.getVoteList(roomid);

            int len = voteList.size();
            int max_voted_user_index = 0; // 최다 득표 유저 인덱스

            // 최다 득표 유저 선정
            for (int i = 1; i < len; i++) {
                if (voteList.get(i) > voteList.get(max_voted_user_index)) {
                    max_voted_user_index = i;
                }
            }

            if(voteUserNameList.get(max_voted_user_index).equals(roomManager.getDoctorPick(roomid))){
                map.put("kingVotedPlayer", "save");
            }else{
                map.put("kingVotedPlayer", voteUserNameList.get(max_voted_user_index));
            }

            return map;
        } catch (Exception e) {
            System.out.println(e);
            return map;
        }
    }

    @ResponseBody
    @PostMapping("/checkWin/{roomid}")
    public Map<String, Object> checkWin(@PathVariable("roomid") int roomid){
        Map<String, Object> map = new HashMap<>();
        int mafiaAmount = roomManager.getMafiaAmount(roomid);
        int citizenAmount = roomManager.getCitizenAmount(roomid);

        map.put("mafiaLen", mafiaAmount);
        map.put("citizenLen", citizenAmount);
        return map;
    }

    // 게임 종료
    // 내 예상 - 승리 여부는 클라이언트에서 판단, 승리 여부를 나타내는 알람창을 띄움, 알람창을 클릭하면 아래 함수가 실행된다.
    @PostMapping("/endGame/{roomid}")
    public String endGame(@PathVariable("roomid") int roomid) {
        // 모든 관련 배열, 변수들을 초기화 하고 원래 화면으로 돌아간다.
//        if(roomManager.endGame(roomid) == true){
//            return ""
//        }
        roomManager.endGame(roomid);
        return "/enter"; // 보여줄 페이지는 다시 확인해봐야 할듯
    }

    // 유저 사망
    @ResponseBody
    @PostMapping("/kill/{roomid}")
    public Map<String, Object> die(@PathVariable("roomid") int roomid, @RequestParam("diePlayer") String userName) {
        Map<String, Object> map = new HashMap<>();
        Map<String, User> userList = roomManager.getUserListHash(roomid);
        for (String key : userList.keySet()) {
            User curUser = userList.get(key);
            if (userName.equals(curUser.getUsername())) {
                roomManager.killPlayer(roomid, userName);
                break;
            }
        }

        // 투표 초기화
        List<Integer> voteList = roomManager.getVoteList(roomid);
        int len = voteList.size();
        for (int i = 0; i < len; i++) {
            voteList.set(i, 0);
        }

        // 살아 있는 유저 수 리턴
        int livePlayerAmount = roomManager.getLivePlayerAmount(roomid);
        map.put("livePlayerLen", livePlayerAmount);
        return map;
    }

    // 유저 퇴장
    @PostMapping("/exit/{roomid}")
    public String exitUser(@PathVariable("roomid") int roomid, @RequestParam("exitUser") String userName) {
        User user = roomManager.getUserListHash(roomid).get(userName);
        if (roomManager.exitUser(roomid, user) == true) {
            return "/login";
        } else {
            return "/";
        }
    }

}
