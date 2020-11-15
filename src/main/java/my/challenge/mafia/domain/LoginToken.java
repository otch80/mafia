package my.challenge.mafia.domain;

/*
* 클라이언트에게 토큰을 보내기 위한 객체
* 멤버변수 token에는 서버에서 생성한 로그인 토큰이 담긴다.
* 멤버변수인 toekn과 클라이언트에서 사용하는 toekn변수와 매핑된다.
*
* */
public class LoginToken {
    String token;

    public LoginToken(){
        token = null;
    }
    public LoginToken(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
