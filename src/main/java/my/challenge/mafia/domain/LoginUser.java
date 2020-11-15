package my.challenge.mafia.domain;


/*
* 클라이언트에서 보내주는 로그인 정보를 받기 위한 객체
* */
public class LoginUser {
    String username;
    String password;

    public LoginUser(){
        username = null;
        password = null;
    }

    public LoginUser(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}