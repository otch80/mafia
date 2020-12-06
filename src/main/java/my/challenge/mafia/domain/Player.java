package my.challenge.mafia.domain;

public class Player {
    public String userId;
    public String jobName;

    public String getUserId(){
        return this.userId;
    }
    public String getJobName(){
        return this.jobName;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }
    public void setJobName(String jobName){
        this.jobName = jobName;
    }

}
