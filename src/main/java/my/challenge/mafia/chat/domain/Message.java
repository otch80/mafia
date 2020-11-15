package my.challenge.mafia.chat.domain;

import lombok.Data;

@Data
public class Message {
    private String id;
    private String msg;
    private String roomid;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getMsg() { return msg; }

    public void setMsg(String msg) { this.msg = msg; }

    public String getRoomid() { return roomid; }

    public void setRoomid(String id){ this.roomid = id; }

}