package models;

public class FootballStats extends GameStats{
    private int goalsScored;
    private int assitsMade;
    private int savesMade;
    private boolean yellowCard;
    private boolean redCard;

    //TODO think about yellow and red cards

    public FootballStats(int overallRating, int goalsScored, int assitsMade, int savesMade, boolean yellowCard, boolean redCard) {
        super(overallRating);
        this.goalsScored = goalsScored;
        this.assitsMade = assitsMade;
        this.savesMade = savesMade;
        this.yellowCard = yellowCard;
        this.redCard = redCard;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public int getAssitsMade() {
        return assitsMade;
    }

    public int getSavesMade() {
        return savesMade;
    }

    public boolean isYellowCard() {
        return yellowCard;
    }

    public boolean isRedCard() {
        return redCard;
    }
}
