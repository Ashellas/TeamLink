package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager {

    public static UserSession login( UserSession userSession, String email, String password) throws SQLException {
        Connection databaseConnection = userSession.getDatabaseConnection();
        TeamMember user = createUser(databaseConnection, email, password);
        System.out.println("user found");
        if(user == null){
            System.out.println("haha");
            return userSession;
        }
        ArrayList<Team> userTeams = createUserTeams(user, databaseConnection);
        System.out.println("teams found");
        HashMap<Team, ObservableList<Team>> standings = createStandings(databaseConnection, userTeams);
        System.out.println("standings found");
        HashMap<Team, ObservableList<Game>> gamesOfTheCurrentRound = createGamesOfTheCurrentRound(databaseConnection, userTeams, standings);
        System.out.println("games found");
        ArrayList<Notification> notifications = createNotifications(databaseConnection, user,0);
        System.out.println("notifications found");
        HashMap<Team, ArrayList<Gameplan>> gameplans = createGameplans(databaseConnection, userTeams);
        System.out.println("gameplans found");
        ObservableList<Training> trainings = createTrainings(databaseConnection, userTeams);
        System.out.println("trainings found");
        ObservableList<TeamApplication> teamApplications = createApplication(databaseConnection, user, userTeams);
        System.out.println("teamapplications found");
        ArrayList<CalendarEvent> calendarEvents = createCalendarEvents(databaseConnection, user, userTeams);
        Date lastSync = new Date();
        return new UserSession(user, userTeams, gamesOfTheCurrentRound, standings, notifications, calendarEvents, trainings, databaseConnection, teamApplications, gameplans, lastSync);
    }

    private static ArrayList<CalendarEvent> createCalendarEvents(Connection databaseConnection, TeamMember user, ArrayList<Team> userTeams) {
        return null;
    }

    private static ObservableList<Training> createTrainings(Connection databaseConnection, ArrayList<Team> userTeams) throws SQLException {
        if(userTeams.isEmpty()){
            return null;
        }
        String[] colorCodes = {"a","b","c","d","e"};
        String teamIds = "" + userTeams.get(0).getTeamId();
        for( int i = 1; i < userTeams.size(); i++){
            teamIds += ", " + userTeams.get(i).getTeamId();
        }
        ArrayList<Training> trainings = new ArrayList<>();
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from trainings where training_date_time < now() " +
                "and team_id in (" + teamIds + ") ORDER BY training_date_time desc LIMIT 2");
        ResultSet pastTraininingsResultSet = prepStmt.executeQuery();
        while(pastTraininingsResultSet.next()){
            int trainingId = pastTraininingsResultSet.getInt("training_id");
            String title = pastTraininingsResultSet.getString("title");
            Date  trainingDate = pastTraininingsResultSet.getDate("training_date_time");
            String description = pastTraininingsResultSet.getString("training_description");
            String locationName = pastTraininingsResultSet.getString("location_name");
            String locationLink = pastTraininingsResultSet.getString("location_link");
            String actionLink = "/views/TrainingsScreen.fxml";
            int teamIndex = 0;
            for (int i = 0; i < userTeams.size(); i++){
                if(userTeams.get(i).getTeamId() == pastTraininingsResultSet.getInt("team_id")){
                    teamIndex = i % 5;
                }
            }
            trainings.add( new Training(trainingId, title, trainingDate, description, actionLink, colorCodes[teamIndex],
                    locationName, locationLink, userTeams.get(teamIndex)));
        }
        PreparedStatement preparedStatement = databaseConnection.prepareStatement("select * from trainings where training_date_time > now() " +
                "and team_id in (" + teamIds + ") ORDER BY training_date_time asc LIMIT ?");
        System.out.println(pastTraininingsResultSet.getRow() + "rows are made");
        preparedStatement.setInt(1, 8 - pastTraininingsResultSet.getRow());
        ResultSet futureTrainingsResultSet = preparedStatement.executeQuery();
        while(futureTrainingsResultSet.next()){
            int trainingId = futureTrainingsResultSet.getInt("training_id");
            String title = futureTrainingsResultSet.getString("title");
            Date  trainingDate = futureTrainingsResultSet.getDate("training_date_time");
            String description = futureTrainingsResultSet.getString("training_description");
            String locationName = futureTrainingsResultSet.getString("location_name");
            String locationLink = futureTrainingsResultSet.getString("location_link");
            String actionLink = "/views/TrainingsScreen.fxml";
            int teamIndex = 0;
            for (int i = 0; i < userTeams.size(); i++){
                if(userTeams.get(i).getTeamId() == pastTraininingsResultSet.getInt("team_id")){
                    teamIndex = i % 5;
                }
            }
            trainings.add(0, new Training(trainingId, title, trainingDate, description, actionLink, colorCodes[teamIndex],
                    locationName, locationLink, userTeams.get(teamIndex)));
        }
        return FXCollections.observableArrayList(trainings);
    }

    private static ObservableList<TeamApplication> createApplication(Connection databaseConnection, TeamMember user, ArrayList<Team> userTeams) throws SQLException {
        ArrayList<TeamApplication> teamApplications = new ArrayList<>();
        if(user.getTeamRole().equals("Head Coach") && !userTeams.isEmpty()){
            for(Team team : userTeams){
                PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from team_applications join  team_members tm on team_applications.applicant_id = tm.member_id and team_id = ? and isDeclined = false");
                prepStmt.setInt(1, team.getTeamId());
                ResultSet resultSet = prepStmt.executeQuery();
                while(resultSet.next()){
                    int applicationId = resultSet.getInt("id");
                    int memberId = resultSet.getInt("member_id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                    String teamRole = resultSet.getString("team_role");
                    //TODO get Image
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
                    TeamMember applicant = new TeamMember(memberId, firstName, lastName, birthday, teamRole, email, user.getSportBranch(), profilePicture);
                    teamApplications.add(new TeamApplication(applicationId, applicant, team, false));
                }
            }
        }
        else{
            PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from team_applications join teams t on team_applications.team_id = t.team_id and applicant_id = ?");
            prepStmt.setInt(1, user.getMemberId());
            ResultSet resultSet = prepStmt.executeQuery();
            while(resultSet.next()){
                    int applicationId = resultSet.getInt("id");
                    boolean isDeclined = resultSet.getBoolean("isDeclined");
                    int teamId = resultSet.getInt("t.team_id");
                    String teamName = resultSet.getString("team_name");
                    String city = resultSet.getString("city");
                    String ageGroup = resultSet.getString("age_group");
                    Image teamLogo;
                    byte[] photoBytes = resultSet.getBytes("team_logo");
                    if(photoBytes != null)
                    {
                        InputStream imageFile = resultSet.getBinaryStream("team_logo");
                        teamLogo = new Image(imageFile);
                    }
                    else{
                        teamLogo = null;
                    }
                    Team appliedTeam = new Team(teamId, teamName, city, ageGroup, teamLogo);
                    teamApplications.add(new TeamApplication(applicationId, user, appliedTeam , isDeclined));
            }
        }
        return FXCollections.observableArrayList(teamApplications);
    }

    private static HashMap<Team, ArrayList<Gameplan>>  createGameplans(Connection databaseConnection, ArrayList<Team> userTeams) throws SQLException {
        if(userTeams.isEmpty()){
            return null;
        }
        HashMap<Team, ArrayList<Gameplan>> teamGameplans = new HashMap<>();
        for( Team team : userTeams){
            ArrayList<Gameplan> gameplans = new ArrayList<>();
            PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from gameplans join team_and_gameplans tg on gameplans.gameplan_id = tg.gameplan_id and team_id = ?");
            prepStmt.setInt(1, team.getTeamId());
            ResultSet resultSet = prepStmt.executeQuery();
            while(resultSet.next()){
                int gameplanId = resultSet.getInt("tg.gameplan_id");
                String title = resultSet.getString("title");
                int version = resultSet.getInt("version");
                //TODO get pdf
                gameplans.add(new Gameplan(gameplanId, title, team, version ));
            }
            teamGameplans.put(team, gameplans);
        }
        return teamGameplans;
    }

    public static ArrayList<Notification> createNotifications(Connection databaseConnection, TeamMember user, int pageNumber) throws SQLException {
        ArrayList<Notification> notifications = new ArrayList<>();
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from notifications join team_members tm on tm.member_id = notifications.sender_id and recipent_id = ? LIMIT ?,8");
        prepStmt.setInt(1, user.getMemberId());
        prepStmt.setInt(2,pageNumber * 8);
        ResultSet resultSet = prepStmt.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            int senderId = resultSet.getInt("sender_id");
            String senderFirstName = resultSet.getString("first_name");
            String senderLastName = resultSet.getString("last_name");
            String title = resultSet.getString("title");
            String message = resultSet.getString("message");
            boolean isUnread = resultSet.getBoolean("is_unread");
            Date timeSent = resultSet.getDate("time_sent");
            String clickAction = resultSet.getString("click_action");
            Notification notification = new Notification(id, title, new TeamMember(senderId, senderFirstName, senderLastName), user ,clickAction, timeSent, isUnread, null);
            notifications.add(notification);
        }
        return notifications;
    }

    public static HashMap<Team, ObservableList<Team>> createStandings(Connection databaseConnection, ArrayList<Team> userTeams ) throws SQLException {
        if(userTeams.isEmpty()){
            return null;
        }
        HashMap<Team, ObservableList<Team>> standings = new HashMap<>();
        for( Team team: userTeams){
            ArrayList<Team> teams = new ArrayList<>();
            PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from league_teams join team_performances tp on league_teams.league_id = tp.league_id and tp.league_team_id = league_teams.league_team_id and tp.league_id = ? order by points desc");
            prepStmt.setInt(1, team.getLeagueId());
            ResultSet resultSet = prepStmt.executeQuery();
            int placement = 1;
            while (resultSet.next()){
                int teamId = resultSet.getInt("tp.league_id");
                int id = resultSet.getInt("id");
                String teamName = resultSet.getString("team_name");
                String abbrevation = resultSet.getString("abbrevation");
                int gamesPlayed = resultSet.getInt("games_played");
                int gamesWon = resultSet.getInt("games_won");
                int gamesDrawn = resultSet.getInt("games_drawn");
                int gamesLost = resultSet.getInt("games_lost");
                int points = resultSet.getInt("points");
                TeamStats teamStats = new TeamStats(id, gamesPlayed, gamesWon, gamesLost, gamesDrawn, placement, points);
                Team leagueTeam = new Team(teamId, teamName, abbrevation, teamStats);
                placement++;
                teams.add(leagueTeam);
            }
            standings.put(team, FXCollections.observableArrayList(teams));
        }
        return standings;
    }

    private static HashMap<Team, ObservableList<Game>> createGamesOfTheCurrentRound(Connection databaseConnection, ArrayList<Team> userTeams, HashMap<Team, ObservableList<Team>> standings) throws SQLException {
        if(userTeams.isEmpty()){
            return null;
        }
        HashMap<Team, ObservableList<Game>> gamesOfTheCurrentRound = new HashMap<>();
        for (Team team : userTeams){
            ArrayList<Game> games = new ArrayList<>();
            PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from league_games join leagues l " +
                    "on league_games.league_id = l.league_id and league_games.round_no = l.current_round and l.league_id = ? " +
                    "join league_teams lt on lt.league_team_id = league_games.away_team_id join league_teams t " +
                    "on t.league_team_id = league_games.home_team_id");
            prepStmt.setInt(1, team.getLeagueId());
            ResultSet gamesResultSet = prepStmt.executeQuery();
            while (gamesResultSet.next()){
                int gameId = gamesResultSet.getInt("game_id");
                Date gameDate = gamesResultSet.getDate("game_date_time");
                int roundNo = gamesResultSet.getInt("round_no");
                String gameLocationName = gamesResultSet.getString("game_location_name");
                String gameLocationLink = gamesResultSet.getString("game_location_link");
                String result = gamesResultSet.getString("final_score");
                int homeTeamId = gamesResultSet.getInt("home_team_id");
                int awayTeamId = gamesResultSet.getInt("away_team_id");
                Team homeTeam = null;
                Team awayTeam = null;
                for (Team leagueTeam : standings.get(team)){
                    if(leagueTeam.getTeamId() == homeTeamId){
                        homeTeam = leagueTeam;
                    }
                    if(leagueTeam.getTeamId() == awayTeamId){
                        awayTeam = leagueTeam;
                    }
                }
                Game game = new Game(gameId, "Game", gameDate, "","/views/LeagueScreen.fxml","COLORCODE", roundNo, homeTeam, awayTeam, gameLocationName, gameLocationName, result);
                games.add(game);
            }
            gamesOfTheCurrentRound.put(team, FXCollections.observableArrayList(games));
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
            LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
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
            String teamCode = teamsResultSet.getString("team_code");
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
                LocalDate birthday = membersResultSet.getDate("birthday").toLocalDate();
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

            prepStmt = databaseConnection.prepareStatement("select * from team_performances join leagues l on l.league_id = team_performances.league_id and league_team_id = ?");
            prepStmt.setInt(1, databaseTeamId);
            ResultSet leagueResultSet = prepStmt.executeQuery();
            String leagueName = "";
            TeamStats teamStats = null;
            if(leagueResultSet.next()){
                int statsId = leagueResultSet.getInt("id");
                leagueName = leagueResultSet.getString("title");
                int gamesPlayed = leagueResultSet.getInt("games_played");
                int gamesWon = leagueResultSet.getInt("games_won");
                int gamesDrawn = leagueResultSet.getInt("games_drawn");
                int gamesLost = leagueResultSet.getInt("games_lost");
                int points = leagueResultSet.getInt("points");
                int totalRounds = leagueResultSet.getInt("total_rounds");
                //TODO calculate training averages
                TrainingPerformanceReport trainingPerformanceReport = null;
                //Placement will be modified in standings creation
                teamStats = new TeamStats(statsId, gamesPlayed, gamesWon, gamesLost, gamesDrawn, points, totalRounds, trainingPerformanceReport);
            }
            userTeams.add( new Team(teamId, databaseTeamId, leagueId, teamName, abbrevation, teamCode, leagueName, city, ageGroup, teamLogo, teamStats, teamMembers));
        }
        return userTeams;
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

    public static UserSession signUpUser(UserSession userSession, String firstName, String lastName, String email, java.sql.Date birthday, String password, String teamRole, String sportBranch, File selectedFile) throws SQLException, IOException {
        PreparedStatement predStmt = userSession.getDatabaseConnection().prepareStatement("INSERT INTO team_members( first_name, last_name, email, " +
                "birthday, password, team_role, sport_branch, photo) values(?,?,?,?,MD5(?),?,?,?)");

        // Fills the statement with relevant info
        predStmt.setString(1, firstName);
        predStmt.setString(2, lastName);
        predStmt.setString(3, email);
        predStmt.setDate(4, birthday);
        predStmt.setString(5, password);
        predStmt.setString(6, teamRole);
        predStmt.setString(7, sportBranch);

        if(selectedFile != null){
            FileInputStream fileInputStream = new FileInputStream(selectedFile.getAbsolutePath());
            predStmt.setBinaryStream(8,fileInputStream,fileInputStream.available());
        }
        else {
            predStmt.setBlob(8, InputStream.nullInputStream());
        }
        // Prints out a report
        int row = predStmt.executeUpdate();
        if(row > 0) {
            System.out.println("Saved into the database");
            TeamMember user = createUser(userSession.getDatabaseConnection(), email, password);
            userSession.setUser(user);
            return userSession;
        }
        return userSession;
    }

    public static boolean isEmailTaken(Connection databaseConnection, String email) throws SQLException {
        PreparedStatement prepStmt = databaseConnection.prepareStatement("SELECT * FROM team_members " +
                "where email = ?");
        prepStmt.setString(1, email);
        ResultSet resultSet = prepStmt.executeQuery();
        if(resultSet.next()){
            return true;
        }
        return false;
    }


    public static String isTeamCodeProper(UserSession user, String teamCode) throws SQLException {
        //TODO age test, code's existence, sport_branch
        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement("select * from teams where team_code = ?");
        prepStmt.setString(1, teamCode);
        ResultSet teamsResultSet = prepStmt.executeQuery();
        if(teamsResultSet.next()){
              int age = Period.between(user.getUser().getBirthday(), LocalDate.now()).getYears();
              int maxAge = Integer.parseInt(teamsResultSet.getString("age_group").substring(1));
              if(age > maxAge){
                  return "Your age is bigger than team's age_group";
              }
              if(!user.getUser().getSportBranch().equals(teamsResultSet.getString("sport_branch"))){
                  return "This team does not play your sport";
              }
              prepStmt = user.getDatabaseConnection().prepareStatement("select * from team_applications where applicant_id = ? and team_id = ? and isDeclined = false");
              prepStmt.setInt(1, user.getUser().getMemberId());
              prepStmt.setInt(2, teamsResultSet.getInt("team_id"));
              ResultSet applicationResultSet = prepStmt.executeQuery();
              if(applicationResultSet.next()){
                  return "You have pending application to that team";
              }
              else{
                  if( applyTeam(user, teamsResultSet.getInt("team_id"))){
                      return "Success";
                  }
                  else{
                      return "an Error Occured";
                  }
              }
        }
        else{
            return "Team does not exist";
        }
    }

    public static UserSession updateApplications(UserSession userSession) throws SQLException {
        ObservableList<TeamApplication> teamApplications = createApplication(userSession.getDatabaseConnection(), userSession.getUser(), userSession.getUserTeams());
        userSession.setTeamApplications(teamApplications);
        return userSession;
    }
    public static boolean applyTeam(UserSession user, int teamId) throws SQLException {
        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement("INSERT INTO team_applications(applicant_id, team_id, isDeclined) VALUES (?,?, false )");
        prepStmt.setInt(1, user.getUser().getMemberId());
        prepStmt.setInt(2, teamId);
        int row = prepStmt.executeUpdate();
        if(row >= 1){
            return true;
        }
        return false;
    }

    public static ObservableList<String> getLeagues(UserSession user, String city, String ageGroup) throws SQLException {
        ArrayList<String> leagueNames = new ArrayList<>();
        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement("select * from leagues where city = ? and age_group = ?");
        prepStmt.setString(1, city);
        prepStmt.setString(2, ageGroup);
        ResultSet resultSet = prepStmt.executeQuery();
        while (resultSet.next()){
            leagueNames.add(resultSet.getString("title"));
        }
        return FXCollections.observableArrayList(leagueNames);
    }

    public static ObservableList<String> getLeagueTeams(UserSession user, String city, String ageGroup, String league) throws SQLException {
        ArrayList<String> teamNames = new ArrayList<>();
        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement("select team_name from league_teams join leagues l on l.league_id = league_teams.league_id and l.title = ? and l.age_group = ? and l.city = ?");
        prepStmt.setString(1, league);
        prepStmt.setString(2, ageGroup);
        prepStmt.setString(3, city);
        ResultSet resultSet = prepStmt.executeQuery();
        while (resultSet.next()){
            teamNames.add(resultSet.getString(1));
        }
        return FXCollections.observableArrayList(teamNames);
    }

    public static UserSession createTeam(UserSession user, String teamName, String abbrevation, String city, String ageGroup, String leagueName, String leagueTeamName, File teamLogo) throws SQLException, IOException {
        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement("select * from league_teams join leagues l on l.league_id = league_teams.league_id and l.title = ? and team_name  = ?");
        prepStmt.setString(1, leagueName);
        prepStmt.setString(2, leagueTeamName);
        ResultSet resultSet = prepStmt.executeQuery();
        if(resultSet.next()){
            PreparedStatement preparedStatement = user.getDatabaseConnection().prepareStatement("INSERT INTO teams(team_name, city, abbrevation, age_group,  " +
                    "team_code, sport_branch, league_id, database_team_id, team_logo) values (?,?,?,?,?,?,?,?,?)");
            String teamCode = createUniqueRandomTeamCode(user.getDatabaseConnection());
            int leagueId = resultSet.getInt("l.league_id");
            int leagueTeamId = resultSet.getInt("league_team_id");
            preparedStatement.setString(1, teamName);
            preparedStatement.setString(2, city);
            preparedStatement.setString(3, abbrevation);
            preparedStatement.setString(4, ageGroup);
            preparedStatement.setString(5, teamCode);
            preparedStatement.setString(6, user.getUser().getSportBranch());
            preparedStatement.setInt(7, leagueId);
            preparedStatement.setInt(8, leagueTeamId);
            if(teamLogo != null){
                FileInputStream fileInputStream = new FileInputStream(teamLogo.getAbsolutePath());
                preparedStatement.setBinaryStream(9,fileInputStream,fileInputStream.available());
            }
            else {
                preparedStatement.setBlob(9, InputStream.nullInputStream());
            }
            int row = preparedStatement.executeUpdate();

            PreparedStatement preparedStmt = user.getDatabaseConnection().prepareStatement("select * from teams where team_code = ?");
            preparedStmt.setString(1, teamCode);

            ResultSet teamResultSet = preparedStmt.executeQuery();

            // Assigns user to that team
            if(teamResultSet.next()){
                int teamId = teamResultSet.getInt("team_id");
                prepStmt = user.getDatabaseConnection().prepareStatement("INSERT INTO team_and_members(team_member_id, team_id) VALUES (?,?)");
                prepStmt.setInt(1, user.getUser().getMemberId());
                prepStmt.setInt(2, teamId);
                row = prepStmt.executeUpdate();
                ArrayList<TeamMember> teamMembers = new ArrayList<>();
                ArrayList<Team> userTeams = new ArrayList<>();
                teamMembers.add(user.getUser());
                if(teamLogo != null){
                    userTeams.add(new Team(teamId, leagueTeamId, leagueId, teamName, abbrevation, teamCode, leagueName, city,  ageGroup, new Image(teamLogo.getAbsolutePath()), null, teamMembers));
                }
                else{
                    userTeams.add(new Team(teamId, leagueTeamId, leagueId, teamName, abbrevation, teamCode, leagueName, city,  ageGroup, null, null, teamMembers));
                }
                user.setUserTeams(userTeams);
            }
        }
        return user;
    }


    /**
     * Creates a unique 8 digit code for the team
     * @return the created code
     * @throws SQLException
     */
    private static String createUniqueRandomTeamCode(Connection databaseConnection) throws SQLException {


        final int BOUND = 100000000;
        ResultSet resultSet;

        int teamCode;
        String tempCode;

        do{
            teamCode =  (int)(Math.random() * BOUND);
            PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from teams where team_code = ?");
            prepStmt.setInt(1,teamCode);
            resultSet = prepStmt.executeQuery();
        }while (resultSet.next());

        return "" + teamCode;
    }


    private String formatDateToMYSQL(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
