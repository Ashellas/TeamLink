package models;

public class FootballStats extends GameStats{
    private String goalsScored;
    private String assitsMade;
    private String foulsMade;
    private String passesMade;
    private String tacklesMade;

    //TODO think about yellow and red cards

    public FootballStats(int id, String goalsScored, String assitsMade, String foulsMade, String passesMade, String tacklesMade) {
        super(id);
        this.goalsScored = goalsScored;
        this.assitsMade = assitsMade;
        this.foulsMade = foulsMade;
        this.passesMade = passesMade;
        this.tacklesMade = tacklesMade;
    }

    public String getFirstStat() {
        return goalsScored;
    }

    public String getSecondStat() {
        return assitsMade;
    }

    public String getThirdStat() {
        return foulsMade;
    }

    public String getForthStat() {
        return passesMade;
    }

    public String getFifthStat() {
        return tacklesMade;
    }

    @Override
    public void setFirstStat(String data) {
        goalsScored = data;
    }

    @Override
    public void setSecondStat(String data) {
        assitsMade = data;
    }

    @Override
    public void setThirdStat(String data) {
        foulsMade = data;
    }

    @Override
    public void setForthStat(String data) {
        passesMade = data;
    }

    @Override
    public void setFifthStat(String data) {
        tacklesMade = data;
    }


}
