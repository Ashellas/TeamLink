package models;

public abstract class GameStats {
    private int id;
    public GameStats(int id) {
        this.id = id;
    }


    public int getId(){return id;}

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
