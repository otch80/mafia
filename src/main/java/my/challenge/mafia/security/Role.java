package my.challenge.mafia.security;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String value;

    Role(String role_user) {
        setValue("ROLE_USER"); // 기본 세팅은 일반유져
        // 특정 아이디로 로그인 시 ROLE_ADMIN으로 설정한다.
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
