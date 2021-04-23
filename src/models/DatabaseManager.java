package models;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DatabaseManager {

    public static UserSession login( Connection databaseConnection, String email, String password) throws SQLException {
        TeamMember user = createUser(databaseConnection, email, password);
        if(user == null){
            return null;
        }
        ArrayList<Team> userTeams = createUserTeams(user, databaseConnection);
        //ArrayList<ArrayList<Team>> leagueTeams = createLeagueTeams(databaseConnection, userTeams);
        ArrayList<ArrayList<Game>> gamesOfTheCurrentRound = new ArrayList<>();
        for( Team team : userTeams){
            gamesOfTheCurrentRound.add(createGamesOfTheCurrentRound(databaseConnection, team));
        }
        return null;
    }

    private static ArrayList<Game> createGamesOfTheCurrentRound(Connection databaseConnection, Team team) throws SQLException {
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from league_games join leagues l " +
                "on league_games.league_id = l.league_id and league_games.round_no = l.current_round and l.league_id = ? " +
                "join league_teams lt on lt.league_team_id = league_games.away_team_id join league_teams t " +
                "on t.league_team_id = league_games.home_team_id");
        prepStmt.setInt(1, team.getLeagueId());
        ResultSet gamesResultSet = prepStmt.executeQuery();
        while (gamesResultSet.next()){
            Date gameDate = gamesResultSet.getDate("game_date_time");
            int roundNo = gamesResultSet.getInt("round_no");
            String gameLocationName = gamesResultSet.getString("game_location_name");
            String gameLocationLink = gamesResultSet.getString("game_location_link");
            String result = gamesResultSet.getString("final_score");
            //TODO get teams from the standings
        }
        return null;
    }

    private static  TeamMember createUser(Connection databaseConnection, String email, String password) throws SQLException { //TODO If player
        PreparedStatement prepStmt = databaseConnection.prepareStatement("SELECT * FROM team_members " +
                " where password = MD5(?) AND email = ?");

        prepStmt.setString(1,password);
        prepStmt.setString(2,email);

        ResultSet resultSet = prepStmt.executeQuery();
        if(resultSet.next()){
            int memberId = resultSet.getInt("member_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            Date birthday = resultSet.getDate("birthday");
            String teamRole = resultSet.getString("team_role");
            String sportBranch = resultSet.getString("sport_branch");
            Image profilePicture;
            byte[] photoBytes = resultSet.getBytes("photo");
            if(photoBytes != null)
            {
                InputStream imageFile = resultSet.getBinaryStream("photo");
                profilePicture = new Image(imageFile);
            }
            else{
                profilePicture = null;
            }
            if( teamRole.equals("Player")){
                boolean isCaptain = false; //TODO set it afterwards
                String position = resultSet.getString("position");
                ArrayList<Injury> playerInjuries = getPlayerInjuries(databaseConnection, memberId);
                PlayerStats playerStats = null;
                if(sportBranch.equals("Basketball")){
                    playerStats = getBasketballStats(databaseConnection, memberId);
                }
                else if (sportBranch.equals("Football")){
                    playerStats = getFootballStats(databaseConnection, memberId);
                }
                return new Player(memberId, firstName, lastName, birthday, teamRole, email, sportBranch, profilePicture, position, playerInjuries, playerStats, isCaptain);
            }
            else{
                return new TeamMember(memberId, firstName, lastName, birthday, teamRole, email, sportBranch, profilePicture);
            }
        }
        else{
            return null;
        }
    }

    private static ArrayList<Team> createUserTeams(TeamMember user, Connection databaseConnection) throws SQLException {
        ArrayList<Team> userTeams = new ArrayList<>();
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from team_and_members JOIN team_members tm " +
                "on tm.member_id = team_and_members.team_member_id and tm.member_id = ? " +
                "join teams t on t.team_id = team_and_members.team_id;");
        prepStmt.setInt(1, user.getMemberId());
        ResultSet teamsResultSet = prepStmt.executeQuery();
        while(teamsResultSet.next()){
            ArrayList<TeamMember> teamMembers = new ArrayList<>();
            int teamId = teamsResultSet.getInt("t.team_id");
            int databaseTeamId = teamsResultSet.getInt("database_team_id");
            int leagueId = teamsResultSet.getInt("league_id");
            String teamName = teamsResultSet.getString("team_name");
            String abbrevation = teamsResultSet.getString("abbrevation");
            String city = teamsResultSet.getString("city");
            String ageGroup = teamsResultSet.getString("age_group");
            int teamCode = teamsResultSet.getInt("team_code");
            int captainId = teamsResultSet.getInt("captain_id");
            Image teamLogo;
            byte[] photoBytes = teamsResultSet.getBytes("team_logo");
            if(photoBytes != null)
            {
                InputStream imageFile = teamsResultSet.getBinaryStream("team_logo");
                teamLogo = new Image(imageFile);
            }
            else{
                teamLogo = null;
            }
            prepStmt = databaseConnection.prepareStatement("select * from team_and_members join team_members tm on tm.member_id = team_and_members.team_member_id and team_id = ?");
            prepStmt.setInt(1, teamId);
            ResultSet membersResultSet = prepStmt.executeQuery();
            while(membersResultSet.next()){
                int memberId = membersResultSet.getInt("member_id");
                String firstName = membersResultSet.getString("first_name");
                String lastName = membersResultSet.getString("last_name");
                String email = membersResultSet.getString("email");
                Date birthday = membersResultSet.getDate("birthday");
                String teamRole = membersResultSet.getString("team_role");
                String sportBranch = membersResultSet.getString("sport_branch");
                Image profilePicture;
                byte[] profilePhotoBytes = membersResultSet.getBytes("photo");
                if(profilePhotoBytes != null)
                {
                    InputStream imageFile = membersResultSet.getBinaryStream("photo");
                    profilePicture = new Image(imageFile);
                }
                else{
                    profilePicture = null;
                }
                if( teamRole.equals("Player")){
                    boolean isCaptain = captainId == memberId;
                    String position = membersResultSet.getString("position");
                    ArrayList<Injury> playerInjuries = getPlayerInjuries(databaseConnection, memberId);
                    PlayerStats playerStats = null;
                    if(sportBranch.equals("Basketball")){
                        playerStats = getBasketballStats(databaseConnection, memberId);
                    }
                    else if (sportBranch.equals("Football")){
                        playerStats = getFootballStats(databaseConnection, memberId);
                    }
                    teamMembers.add(new Player(memberId, firstName, lastName, birthday, teamRole, email, sportBranch, profilePicture, position, playerInjuries, playerStats, isCaptain));
                }
                else{
                    teamMembers.add(new TeamMember(memberId, firstName, lastName, birthday, teamRole, email, sportBranch, profilePicture));
                }
            }
            prepStmt = databaseConnection.prepareStatement("select title from leagues where league_id = ?");
            prepStmt.setInt(1, leagueId);
            ResultSet leagueResultSet = prepStmt.executeQuery();
            String leagueName = "";
            if(leagueResultSet.next()){
                leagueName = leagueResultSet.getString(1);
            }
            TeamStats teamStats = getTeamStats( databaseConnection, teamId);
            userTeams.add( new Team(teamId, databaseTeamId, leagueId, teamName, abbrevation, teamCode, leagueName, city, ageGroup, teamLogo, teamStats, teamMembers));
        }
        return userTeams;
    }

    //TODO make this work
    private static TeamStats getTeamStats(Connection databaseConnection, int teamId) {
        return null;
    }

    //TODO make this Work
    private static PlayerStats getFootballStats(Connection databaseConnection, int memberId) {
        return null;
    }

    //TODO make this Work
    private static PlayerStats getBasketballStats(Connection databaseConnection, int memberId) {
        return null;
    }

    //TODO make this work
    private static ArrayList<Injury> getPlayerInjuries(Connection databaseConnection, int memberId) {
        return null;
    }

    private String formatDateToMYSQL(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

}
