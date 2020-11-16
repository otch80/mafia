package my.challenge.mafia.domain;



/*
* 방 입장시 사용되는 객체
* 유저 정보와 방 번호가 전달된다.
*
*
* */
public class RoomInfo {
    String room_number;
    String user_id;

    public RoomInfo(){
        room_number = null;
        user_id = null;
    }

    public RoomInfo(String room_number, String user_id) {
        this.room_number = room_number;
        this.user_id = user_id;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
