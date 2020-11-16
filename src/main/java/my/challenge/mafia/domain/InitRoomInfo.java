package my.challenge.mafia.domain;
/*
* InitRoomInfo는 초기 방 생성에서만 사용되는 객체이다.
*
*
*
* */
public class InitRoomInfo {
    String room_name;
    String room_person;
    String room_link;
    String room_number;

    public InitRoomInfo(){
        this.room_name = null;
        this.room_person = null;
        this.room_link = null;
        this.room_number = null;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getRoom_person() {
        return room_person;
    }

    public void setRoom_person(String room_person) {
        this.room_person = room_person;
    }

    public String getRoom_link() {
        return room_link;
    }

    public void setRoom_link(String room_link) {
        this.room_link = room_link;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public InitRoomInfo(String room_name, String room_person, String room_link, String room_number){
        this.room_name = room_name;
        this.room_person = room_person;
        this.room_link = room_link;
        this.room_number = room_number;
    }

}
