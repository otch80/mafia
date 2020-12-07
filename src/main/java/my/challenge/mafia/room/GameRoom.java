package my.challenge.mafia.room;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
    초기 방 생성시 유저를 번호로 구별한다.
    게임 시작시 클라이언트 쪽에서 각 방의 번호, 유저 이름, 역할, 그룹을 전달받는다. 이를 기반으로 User객체를 최신화하고 userList의 키값을 역할로 대체한다.
    이제 유저는 역할 명을 통해 구별한다.
*/

public class GameRoom {
    private HashMap<String, User> userList;
    private List<String> voteUserNameList = new ArrayList<>(); // 투표될 수 있는 사용자의 이름을 젖아하는 배열 - 1
    private List<Integer> voteList = new ArrayList<>(); // 유저 별 투표 수를 저장하는 배열 - 2
    private List<Ability> abilityList = new ArrayList<>(); // 능력 사용 여부를 저장하는 배열 - 3 => 1,2,3, 은 모두 같은 크기를 가진다.
    private int voteCount = 0; // 투표된 횟수
    private String doctorPick;
    private String maifaPick;

    public String getDoctorPick(){
        return doctorPick;
    }
    public void setDoctorPick(String target){
        this.doctorPick = target;
    }
    public String getMaifaPick(){
        return maifaPick;
    }
    public void setMaifaPick(String target){
        this.maifaPick = target;
    }


    // 유저리트스의 원본을 리턴한다. HashMap 그래도 리턴
    public HashMap<String, User> getUserListHash() {
        return userList;
    }

    // 투표 현황을 리턴
    public List<Integer> getVoteList() {
        return voteList;
    }

    // 유저 중 몇명이 투표 했는지 리턴, 0이면 모든 유저가 투표 한 것임
    public int getVoteCount() {
        return voteCount;
    }

    // 투표 현황인 voteList와 매핑된 유저 이름을 리턴한다.
    public List<String> getVoteUserNameList() {
        return voteUserNameList;
    }

    // 유저 죽이기 함수, 투표된 사람수
    public void setDiePlayer(String userName) {
        User tmpUser = userList.get(userName);
        tmpUser.setAlive(false);
        userList.put(userName, tmpUser);

        // 투표 가능한 유저를 줄임, 그에 따라 크기가 동일해야 하는 배열들도 크기를 줄여준다.
        if (voteUserNameList.remove(userName)) { // 해당 유저를 지운 결과가 false라면 실행 X - ajax 통신이기 때문에 방 안의 유저 수만큼 동작하기 때문에 사용한 if 문이다.
            voteList.remove(0);
            abilityList.remove(0);
        }

    }

    // 살아있는 사람의 수를 리턴
    public int getLivePlayerLan() {
        int livePlayerAmount = 0;
        for (String key : userList.keySet()) {
            if (userList.get(key).isAlive() == true) {
                livePlayerAmount += 1;
            }
        }
        return livePlayerAmount;
    }

    // 유저 현황 확인 함수
    public int getUserAmount() {
        return userList.size();
    }

    // 유저 이름만 포함된 배열 리턴
    public ArrayList<String> getUserList() {
        ArrayList<String> list = new ArrayList<String>();
        for (String key : userList.keySet()) {
            list.add(key);
        }
        return list;
    }

    // 시민팀 생존자 수 리턴
    public int countCitizenAmount(){
        int count = 0;
        for (String key : userList.keySet()) {
            if(userList.get(key).isAlive() == true && userList.get(key).getGroup() == "citizen")
                count += 1;
        }
        return count;
    }

    // 마피아팀 생존자 수 리턴
    public int countMafiaAmount(){
        int count = 0;
        for (String key : userList.keySet()) {
            if(userList.get(key).isAlive() == true && userList.get(key).getGroup() == "mafia")
                count += 1;
        }
        return count;
    }

    // 능력 사용
    public boolean useAbility(String srcPlayer, String srcPlayerJob, String targetPlayer) {
        int len = abilityList.size();

        if (srcPlayerJob.equals("doctor")) { // 의사 능력 사용
            for (int i = 0; i < len; i++) {
                if (targetPlayer.equals(voteUserNameList.get(i))) {
                    Ability tmpAbility = abilityList.get(i);
                    tmpAbility.setDoctor(true);
                    abilityList.set(i, tmpAbility);
                    return true;
                }
            }
        } else if (srcPlayerJob.equals("mafia")) { // 마피아 능력 사용
            for (int i = 0; i < len; i++) {
                if (targetPlayer.equals(voteUserNameList.get(i))) {
                    Ability tmpAbility = abilityList.get(i);
                    tmpAbility.setMafia(true);
                    abilityList.set(i, tmpAbility);
                    return true;
                }
            }
        } else if (srcPlayerJob.equals("police")) { // 경찰 능력 사용
            for (int i = 0; i < len; i++) {
                if (targetPlayer.equals(voteUserNameList.get(i))) {
                    Ability tmpAbility = abilityList.get(i);
                    tmpAbility.setPolice(true);
                    abilityList.set(i, tmpAbility);
                    return true;
                }
            }
        } // ... 등등 능력 추가 가능


        return false;
    }

    // 경찰 능력 사용
    public String policeAbility(String targetPlayer){
        System.out.println("hello");
        for(String key : userList.keySet()){
            if(userList.get(key).getUsername().equals(targetPlayer)){
                System.out.println(userList.get(key).getUsername());
                return userList.get(key).getRole();
            }
        }
        return null;
    }

    // 스파이 능력 사용
    public String spyAbility(String targetPlayer, String srcPlayer) {
        String targetRole = null;
        for(String key : userList.keySet()){
            if(userList.get(key).getUsername().equals(targetPlayer)){
                targetRole = userList.get(targetPlayer).getRole();
            }
        }
        if(targetRole.equals("mafia")) {
            userList.get(srcPlayer).setRole("mafia");
            userList.get(srcPlayer).setGroup("mafia");
        }
        return targetRole;
    }

    // 마피아 능력 사용 - 죽일 사람 투표
    public boolean mafiaVoteUser(String userName) {
        try {
            int len = voteList.size();
            for (int i = 0; i < len; i++) {
                if (voteUserNameList.get(i).equals(userName)) {
                    int voteNum = voteList.get(i);
                    voteList.set(i, voteNum + 1);
                    voteCount -= 1;
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    // 의사 능력 사용
    public void doctorAbility(String targetPlayer){
        doctorPick = targetPlayer;
    }


    // 방 생성 + 방장 입장 (생성자)
    public GameRoom(User user) {
        userList = new HashMap<String, User>();
        userList.put(user.getUsername(), user); // 방장 입장
        //userList.put("1", userList.remove("1")); // reomve는 삭제하는 key값의 데이터를 리턴한다.

        // 방장은 전체 채팅방 구독 ################################################
    }

    // 방 삭제 - 유저가 0명 일때
    public boolean gameRoomClose() {
        try {
            userList.clear();
            // 마지막 유저 전체 채팅방 구독 해제 ##########################################

            return true; // 방 닫기 성공
        } catch (Exception e) {
            System.out.println("My Error : closing room is fail" + "\n" + e);
            return false; // 방 닫기 실패
        }
    }

    // 유저 입장
    // 입장 가능 인원 수 제한
    public boolean enterUser(User user) {
        try {
            if (userList.size() < 9) {
                userList.put(user.getUsername(), user);
                // 입장 유저 전체 채팅방 구독 ###################################

                return true; // 방 입장 성공
            } else { // 인원 초과로 입장 실패
                return false;
            }
        } catch (Exception e) {
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
        try {
            for (String key : userList.keySet()) {
//                if(userList.get(key).getRole().equals("mafia")){
//                    // 직업이 마피아라면 마피아 전용 채팅방 구독 #############################################
//                }
                if (!voteUserNameList.contains(key)) { // 유저 수에 맞게 투표 수, 투표 유저 이름, 유저에게 적용된 능력 배열을 초기화 한다. - 방 안에 유저 수만큼 실행하므로 사용한 if문
                    voteUserNameList.add(key);
                    voteList.add(0);
                    abilityList.add(new Ability());
                }
            }
//            // 테스트 코드
//            int len = voteList.size();
//            System.out.println("++++++++++++++++++++++++++++++");
//            for(int i = 0; i < len; i++){
//                System.out.println(voteList.get(i));
//            }
//            for(int i = 0; i < voteUserNameList.size(); i++){
//                System.out.println(voteUserNameList.get(i));
//            }
//            System.out.println("++++++++++++++++++++++++++++++");
//            // 테스트 코드
            voteCount = userList.size(); // 유저 수 만큼으로 설정 0이 되면 모든 유저가 투표를 완료한 것임
            return true;
        } catch (Exception e) {
            System.out.println("My Error : startGame is fail" + "\n" + e);
            return false;
        }
    }

    // 특정 유저 투표하기
    public boolean voteUser(String userName) {
        try {
            int len = voteList.size();
            for (int i = 0; i < len; i++) {
                if (voteUserNameList.get(i).equals(userName)) {
                    int voteNum = voteList.get(i);
                    voteList.set(i, voteNum + 1);
                    voteCount -= 1;
                    System.out.println(voteUserNameList.get(i));
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    // 게임 종료
    public boolean endGame() {
        try {
            for (String key : userList.keySet()) { // keySet() : map의 key값만 모두 가져온다.
                if (userList.get(key).getRole().equals("mafia")) {
                    // 직업이 마피아라면 마피아 전용 채팅방 구독 해제 ##########################################3
                }
                // 사망자는 전체 채팅을 구독 해제했으므로 모든 유저 전체 채팅 재구독 ################################
            }
            // 모든 유저 전적 업데이트


            // 사용된 변수들을 모두 초기화 한다.
            userList.clear();
            voteUserNameList.clear();
            voteList.clear();
            abilityList.clear();
            voteCount = 0;
            return true;
        } catch (Exception e) {
            System.out.println("My Error : endGame is fail" + "\n" + e);
            return false;
        }
    }
}
