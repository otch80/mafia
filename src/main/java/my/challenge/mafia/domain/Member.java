package my.challenge.mafia.domain;

public class Member {
    private String id;
    private String password;
<<<<<<< HEAD
    //private String refresh;

=======
>>>>>>> 89eb01b1b390974067a5127f012df23423fa4786

    public Member(){
        this.id = null;
        this.password = null;
    }

    public Member(String id, String password){
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

<<<<<<< HEAD
//    public String getRefresh() {
//        return refresh;
//    }
//
//    public void setRefresh(String refresh) {
//        this.refresh = refresh;
//    }
=======
>>>>>>> 89eb01b1b390974067a5127f012df23423fa4786
}
