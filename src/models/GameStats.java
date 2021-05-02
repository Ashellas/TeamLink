package models;

public abstract class GameStats {
    private TeamMember player;

    public GameStats(TeamMember player) {
        this.player = player;
    }

    public String getPlayerFullName() {
        return player.getFullName();
    }

    public TeamMember getPlayer(){
        return player;
    }
}
