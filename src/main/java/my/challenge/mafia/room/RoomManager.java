package my.challenge.mafia.room;


import org.springframework.stereotype.Component;

import java.util.*;


/*
    방은 숫자로 구분한다.

 */

@Component
public class RoomManager {
    HashMap<String, GameRoom> roomList; // 현재 존재하는 방
    PriorityQueue<Integer> restRoomList; // 새로 생성 가능한 방

    public RoomManager() {
        roomList = new HashMap<String, GameRoom>();
        this.restRoomList = new PriorityQueue<>();
        for (int i = 1; i < 100; i++) {
            this.restRoomList.add(i);
        }
    }

    // 방 번호를 얻는 함수 - 현재 생성 가능한 방번호 중 가장 작은 수를 리턴한다.
    private int getRoomNumber() {
        int roomNumber = this.restRoomList.peek();
        this.restRoomList.poll();
        return roomNumber;
    }

    // 생성 가능한 방 번호를 갱신 - 우선 순위 큐에 다시 넣는 것임
    private void initRoomNumber(int number) {
        this.restRoomList.add(number);
    }


    // 특정 방 번호의 유저리스트 가져오기 - 유저 이름만 가져온다.
    public ArrayList<String> getUserList(int roomNumber) {
        return roomList.get(Integer.toString(roomNumber)).getUserList();
    }

    // 유저 리스트 원본을 가져온다. HashMap 상태 그대로
    public HashMap<String, User> getUserListHash(int roomNumber) {
        return roomList.get(Integer.toString(roomNumber)).getUserListHash();
    }

    // 투표된 유저 리스트 원본을 가져온다.
    public List<Integer> getVoteList(int roomNumber) {
        return roomList.get(Integer.toString(roomNumber)).getVoteList();
    }

    // 투표되는 유저의 이름을 가져온다.
    public List<String> getVoteUserNameList(int roomNumber) {
        return roomList.get(Integer.toString(roomNumber)).getVoteUserNameList();
    }

    public int getVoteCount(int roomNumber) {
        return roomList.get(Integer.toString(roomNumber)).getVoteCount();
    }

    // 특정 유저를 투표하는 함수
    public boolean voteUser(int roomNumber, String userName) {
        return roomList.get(Integer.toString(roomNumber)).voteUser(userName);
    }

    // 유저 죽이기 함수
    public void killPlayer(int roomNumber, String userName) {
        roomList.get(Integer.toString(roomNumber)).setDiePlayer(userName);
    }

    // 살아있는 유저 수를 리턴
    public int getLivePlayerAmount(int roomNubmer) {
        return roomList.get(Integer.toString(roomNubmer)).getLivePlayerLan();
    }

    // 시민팀 생존자 수 리턴
    public int getCitizenAmount(int roomNumber){
        return roomList.get(Integer.toString(roomNumber)).countCitizenAmount();
    }

    // 마피아팀 생존자 수 리턴
    public int getMafiaAmount(int roomNumber){
        return roomList.get(Integer.toString(roomNumber)).countMafiaAmount();
    }

    // 의사가 선택한 유저 리턴
    public String getDoctorPick(int roomNumber){
        return roomList.get(Integer.toString(roomNumber)).getDoctorPick();
    }

    // 살아있는 유저를 대상으로 능력을 사용
    public boolean useAbility(int roomNumber, String srcPlayer, String srcPlayerJob, String targetPlayer) {
        return roomList.get(Integer.toString(roomNumber)).useAbility(srcPlayer, srcPlayerJob, targetPlayer);
    }

    // 경찰의 능력 사용
    public String policeAbility(int roomNumber, String targetPlayer){
        System.out.println(roomNumber);
        System.out.println(targetPlayer);
        return roomList.get(Integer.toString(roomNumber)).policeAbility(targetPlayer);
    }

    // 스파이 능력 사용
    public String spyAbility(int roomNubmer, String targetPlayer, String srcPlayer){
        return roomList.get(Integer.toString(roomNubmer)).spyAbility(targetPlayer, srcPlayer);
    }

    // 마피아 능력 사용 - 죽일 사람 투ㅛ
    public boolean mafiaVoteUser(int roomNumber, String userName) {
        return roomList.get(Integer.toString(roomNumber)).voteUser(userName);
    }

    // 의사 능력 사용
    public void doctorAbility(int roomNumber, String targetPlayer){
        roomList.get(Integer.toString(roomNumber)).doctorAbility(targetPlayer);
    }

    // 방 생성
    public boolean makeRoom(int roomNumber, User user) {
        try {
            // 방번호 최대 값이 1000이다. 이를 넘을 경우 방 생성 실패
            if (roomNumber > 100)
                throw new Exception();
            roomList.put(Integer.toString(roomNumber), new GameRoom(user));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 게임방 삭제
    public boolean deleteRoom(int roomNumber) {
        boolean gameRoomCloseCheck = roomList.get(Integer.toString(roomNumber)).gameRoomClose();// 게임방 삭제 및 채팅방 구독 해제
        roomList.remove(Integer.toString(roomNumber)); // 게임방 리스트에서 해당 게임방 삭제
        initRoomNumber(roomNumber); // 생성 가능한 방 초기화
        return gameRoomCloseCheck;
    }

    // 유저 입장
    // 방 번호, 입장 유저 정보
    public boolean enterRoom(int roomNumber, User user) {
        // roomNumber == 0이면 랜덤 입장, 아니면 해당 roomNumber로 입장
        if (roomNumber != 0) {
            // 들어갈 수 있는 방이 없으면 방 생성 - 방 번호는 현재 생성 가능한 방 번호 중 가장 작은 수로 설정
            if (roomList.get(Integer.toString(roomNumber)) == null) {
                makeRoom(roomNumber, user);
            }
            return roomList.get(Integer.toString(roomNumber)).enterUser(user); // 유저의 방 입장
        } else {
            Iterator<String> keys = roomList.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (roomList.get(key).getUserAmount() < 100) { // 들어 갈 수 있는 방이 있으면 입장
                    return roomList.get(key).enterUser(user);
                }

            }
            return makeRoom(getRoomNumber(), user);
        }
    }

    // 게임 시작
    // 방 번호, 모든 유저 정보
    public boolean startGame(int roomNumber) {
        return roomList.get(Integer.toString(roomNumber)).startGame();
    }

    // 게임 종료
    public boolean endGame(int roomNumber) {
        return roomList.get(Integer.toString(roomNumber)).endGame();
    }

    // 유저 퇴장
    public boolean exitUser(int roomNumber, User user) {
        if (roomList.get(Integer.toString(roomNumber)).getUserAmount() == 1) // 모든 유저가 나갈 경우 방 삭제
            deleteRoom(roomNumber);
        return roomList.get(Integer.toString(roomNumber)).exitUser(user);
    }

}
