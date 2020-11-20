package my.challenge.mafia.room;


import java.util.ArrayList;
import java.util.HashMap;

/*
    초기 방 생성시 유저를 번호로 구별한다.
    게임 시작시 클라이언트 쪽에서 각 방의 번호, 유저 이름, 역할, 그룹을 전달받는다. 이를 기반으로 User객체를 최신화하고 userList의 키값을 역할로 대체한다.
    이제 유저는 역할 명을 통해 구별한다.
*/

public class GameRoom {
    private HashMap<String, User> userList;
    private boolean start; // 게임의 시작 유무 판단
    private boolean day; // 낮인지 유무

    // 유저 현황 확인 함수
    public int getUserAmount(){
        return userList.size();
    }


    public ArrayList<String> getUserList(){
        ArrayList<String> list = new ArrayList<String>();
        for(String key : userList.keySet()){
            list.add(key);
        }
        return list;
    }


    // 방 생성 + 방장 입장 (생성자)
    public GameRoom(User user) {
        userList = new HashMap<String, User>();
        start = false; // 시작 전 설정
        day = true; // 낮 설정 - 있어야 하는지 의문, 어쩌피 낮밤인지는 프론트에서 처리할 것 같음
        userList.put(user.getUsername(), user); // 방장 입장
        //userList.put("1", userList.remove("1")); // reomve는 삭제하는 key값의 데이터를 리턴한다.

        // 방장은 전체 채팅방 구독 ################################################
    }

    // 방 삭제 - 유저가 0명 일때
    public boolean gameRoomClose() {
        try{
            userList.clear();
            // 마지막 유저 전체 채팅방 구독 해제 ##########################################

            return true; // 방 닫기 성공
        }catch (Exception e){
            System.out.println("My Error : closing room is fail" + "\n" + e);
            return false; // 방 닫기 실패
        }
    }

    // 유저 입장
    // 입장 가능 인원 수 제한
    public boolean enterUser(User user) {
        try{
            if(userList.size() < 9){
                userList.put(user.getUsername(), user);
                // 입장 유저 전체 채팅방 구독 ###################################

                return true; // 방 입장 성공
            }else{ // 인원 초과로 입장 실패
                return false;
            }
        }catch (Exception e){
            System.out.println("My Error : enterUser is fail" + "\n" + e);
            return false;
        }
    }

    // 유저 퇴장
    public boolean exitUser(User user) {
        try {
            userList.remove(user.getUsername());
            // 채팅방 구독 해제 ####################################


            return true; // 방 나가기 성공
        } catch (Exception e) {
            System.out.println("My Error : exitUser is fail" + "\n" + e);
            return false;
        }
    }


    // 게임 시작
    // 프론트에서 유저정보를 전달 받는다.
    public boolean startGame() {
        try{
            start = true;
            for(String key : userList.keySet()){
                if(userList.get(key).getRole().equals("mafia")){
                    // 직업이 마피아라면 마피아 전용 채팅방 구독 #############################################
                }
            }
            return true;
        }catch (Exception e){
            System.out.println("My Error : startGame is fail" + "\n" + e);
            return false;
        }
    }

    // 게임 종료
    public boolean endGame(String winGroup) {
        try{
            for(String key : userList.keySet()){ // keySet() : map의 key값만 모두 가져온다.
                if(userList.get(key).getRole().equals("mafia")){
                    // 직업이 마피아라면 마피아 전용 채팅방 구독 해제 ##########################################3
                }
                // 사망자는 전체 채팅을 구독 해제했으므로 모든 유저 전체 채팅 재구독 ################################
            }
            // 모든 유저 전적 업데이트
            start = false;
            return true;
        }catch (Exception e){
            System.out.println("My Error : endGame is fail" + "\n" + e);
            return false;
        }
    }

    // 유저 사망
    public boolean die(User user) {
        try{
            if(!user.isAlive()){
                user.setAlive(false); // 사망 처리
                // 해당 유저 전체 채팅 구독 해제 ################################
                // 해당 유저 사망자 채팅 구독 #############################
                return true;
            }else
                return false;

        }catch (Exception e){
            System.out.println("My Error : die is fail" + "\n" + e);
            return false;
        }
    }


}
