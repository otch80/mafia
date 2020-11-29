package my.challenge.mafia.room;


public class gameData {
    private String job_name;
    private String group;
    private boolean live;
    private boolean ability;
    private int playerNum;

    public gameData(){
        job_name = "";
        group = "";
        live = true;
        ability = false;
        playerNum = 0;
    }

    public gameData(String job_name, String group, boolean live, boolean ability, int playerNum){
        this.job_name = job_name;
        this.group = group;
        this.live = live;
        this.ability = ability;
        this.playerNum = playerNum;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public boolean isAbility() {
        return ability;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public void setAbility(boolean ability) {
        this.ability = ability;
    }
}
