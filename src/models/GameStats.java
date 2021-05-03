package models;

public abstract class GameStats {
    private TeamMember player;

    public GameStats(TeamMember player) {
        this.player = player;
    }

    public String getPlayerName(){
        return player.getFullName();
    }

    abstract public String getFirstStat();
    abstract public String getSecondStat();
    abstract public String getThirdStat();
    abstract public String getForthStat();
    abstract public String getFifthStat();

    abstract public void setFirstStat(String data);
    abstract public void setSecondStat(String data);
    abstract public void setThirdStat(String data);
    abstract public void setForthStat(String data);
    abstract public void setFifthStat(String data);
}
