package models;

public class FootballStats extends GameStats{
    private int goalsScored;
    private int assitsMade;
    private int savesMade;
    private boolean yellowCard;
    private boolean redCard;

    //TODO think about yellow and red cards

    public FootballStats(TeamMember player, int goalsScored, int assitsMade, int savesMade, boolean yellowCard, boolean redCard) {
        super(player);
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

    public int isYellowCard() {
        return yellowCard;
    }

    public int isRedCard() {
        return redCard;
    }
}
