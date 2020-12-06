package my.challenge.mafia.room;


// 능력의 사용여부를 저장하기 위한 클래스
public class Ability {
    private boolean doctor;
    private boolean mafia;
    private boolean police;

    public Ability(){
        this.doctor = false;
        this.mafia = false;
        this.police = false;
    }

    public Ability(boolean doctor, boolean mafia, boolean police) {
        this.doctor = doctor;
        this.mafia = mafia;
        this.police = police;
    }

    public boolean isDoctor() {
        return doctor;
    }

    public void setDoctor(boolean doctor) {
        this.doctor = doctor;
    }

    public boolean isMafia() {
        return mafia;
    }

    public void setMafia(boolean mafia) {
        this.mafia = mafia;
    }

    public boolean isPolice() {
        return police;
    }

    public void setPolice(boolean police) {
        this.police = police;
    }
}
