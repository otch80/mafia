package my.challenge.mafia.room;

public class User {
    private String username; // 플레이어의 계정 이름
    private String role; // 플레이어 역할
    private String group; // 플레이어 그룹
    private boolean alive; // 생존 여부
    private boolean master; // 방장 여부


    // 일반 유저 생성자
    public User(String username){
        this.username = username;
        this.alive = true;
        this.master = false;
        this.role = null;
        this.group = null;
    }

    // 방장 유저 생성자
    public User(String username, boolean master){
        this.username = username;
        this.alive = true;
        this.master = master;
        this.role = null;
        this.group = null;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

}
