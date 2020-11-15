package my.challenge.mafia.controller;

public class MemberForm {
    // 클라이언트로부터 전달받은 데이터가 담긴다.
    // 다른 형태로 받을 경우 바꿀 수 있다.
    private String id;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}