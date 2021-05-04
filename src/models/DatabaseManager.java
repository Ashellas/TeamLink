package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DatabaseManager {

    public static UserSession login( UserSession userSession, String email, String password) throws SQLException, IOException {
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
        HashMap<Team, ObservableList<Game>> gamesOfTheCurrentRound = createCurrentRoundGames(databaseConnection, userTeams, standings, 0);
        System.out.println("games found");
        ArrayList<Notification> notifications = createNotifications(databaseConnection, user,0);
        System.out.println("notifications found");
        HashMap<Team, ArrayList<Gameplan>> gameplans = createGameplans(databaseConnection, userTeams);
        System.out.println("gameplans found");
        ObservableList<Training> trainings = createTrainings(databaseConnection, userTeams);
        System.out.println("trainings found");
        ObservableList<TeamApplication> teamApplications = createApplication(databaseConnection, user, userTeams);
        System.out.println("teamapplications found");
        HashMap<Team, ArrayList<CalendarEvent>> calendarEvents = createCurrentCalendarEvents(databaseConnection, userTeams);
        Date lastSync = new Date();
        HashMap<Team, ArrayList<Announcement>> announcements= createAnnouncements(databaseConnection, userTeams);
        return new UserSession(user, userTeams, gamesOfTheCurrentRound, standings, notifications, calendarEvents, trainings, databaseConnection, teamApplications, gameplans, lastSync, announcements);
    }

    private static ObservableList<Training> createTrainings(Connection databaseConnection, ArrayList<Team> userTeams) throws SQLException   {
        if(userTeams.isEmpty()){
            return null;
        }
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
            prepStmt = databaseConnection.prepareStatement("SELECT id FROM training_performances where training_id = ?");
            prepStmt.setInt(1, trainingId);

            ResultSet performaceResultSet = prepStmt.executeQuery();
            boolean isRated;
            if(performaceResultSet.next()){
                isRated = true;
            }
            else{
                isRated = false;
            }
            trainings.add( new Training(trainingId, title, trainingDate, actionLink, "training", locationName, locationLink, userTeams.get(teamIndex),isRated));

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
                if(userTeams.get(i).getTeamId() == futureTrainingsResultSet.getInt("team_id")){
                    teamIndex = i % 5;
                }
            }
            prepStmt = databaseConnection.prepareStatement("SELECT id FROM training_performances where training_id = ?");
            prepStmt.setInt(1, trainingId);

            ResultSet performaceResultSet = prepStmt.executeQuery();
            boolean isRated;
            if(performaceResultSet.next()){
                isRated = true;
            }
            else{
                isRated = false;
            }
            trainings.add(0, new Training(trainingId, title, trainingDate, actionLink, "training",
                    locationName, locationLink, userTeams.get(teamIndex),isRated));
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
                    TeamMember applicant = new TeamMember(memberId, firstName, lastName, birthday, teamRole, email);
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
                    Team appliedTeam = new Team(teamId, teamName, city, ageGroup);
                    teamApplications.add(new TeamApplication(applicationId, user, appliedTeam , isDeclined));
            }
        }
        return FXCollections.observableArrayList(teamApplications);
    }

    public static void downloadGameplan( Connection databaseConnection, int fileId, String filePath) throws SQLException, IOException {
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select file from file_storage where id = ?");
        prepStmt.setInt(1, fileId);

        ResultSet resultSet = prepStmt.executeQuery();
        if(resultSet.next()){

            File teamLinkDirectory = new File(System.getProperty("user.home") + "\\Teamlink");
            if (!teamLinkDirectory.exists()){
                teamLinkDirectory.mkdir();
            }

            FileOutputStream output = new FileOutputStream(filePath);

            InputStream input = resultSet.getBinaryStream("file");
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0){
                output.write(buffer);
            }
        }
    }


    private static HashMap<Team, ArrayList<Gameplan>>  createGameplans(Connection databaseConnection, ArrayList<Team> userTeams) throws SQLException, IOException {

        if(userTeams.isEmpty()){
            return null;
        }
        HashMap<Team, ArrayList<Gameplan>> teamGameplans = new HashMap<>();
        for( Team team : userTeams){
            ArrayList<Gameplan> gameplans = new ArrayList<>();
            PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from gameplans where team_id = ?");
            prepStmt.setInt(1, team.getTeamId());
            ResultSet resultSet = prepStmt.executeQuery();
            while(resultSet.next()){
                int gameplanId = resultSet.getInt("gameplan_id");
                String title = resultSet.getString("title");
                int file_id = resultSet.getInt("file_id");

                gameplans.add(new Gameplan(gameplanId, title, file_id ));
            }
            teamGameplans.put(team, gameplans);
        }
        return teamGameplans;
    }

    public static ArrayList<Notification> createNotifications(Connection databaseConnection, TeamMember user, int pageNumber) throws SQLException {
        ArrayList<Notification> notifications = new ArrayList<>();
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from notifications join team_members tm" +
                " on tm.member_id = notifications.recipent_id join file_storage fs on " +
                "fs.id = tm.file_id and member_id = ? LIMIT ?, 5");
        prepStmt.setInt(1, user.getMemberId());
        prepStmt.setInt(2,pageNumber * 5);
        ResultSet resultSet = prepStmt.executeQuery();
        while (resultSet.next()){
            int id = resultSet.getInt("notifications.id");
            int senderId = resultSet.getInt("sender_id");
            String senderFirstName = resultSet.getString("first_name");
            String senderLastName = resultSet.getString("last_name");
            String title = resultSet.getString("title");
            String message = resultSet.getString("message");
            boolean isUnread = resultSet.getBoolean("is_unread");
            Date timeSent = resultSet.getDate("time_sent");
            String clickAction = resultSet.getString("click_action");
            Image profilePicture = null;
            byte[] photoBytes = resultSet.getBytes("file");
            if(photoBytes != null)
            {
                InputStream imageFile = resultSet.getBinaryStream("file");
                profilePicture = new Image(imageFile);
            }
            else{
                profilePicture = null;
            }

            Notification notification = new Notification(id, title, message, new TeamMember(senderId, senderFirstName, senderLastName, profilePicture)
                    , user ,clickAction, timeSent, isUnread);
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
            PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from league_teams join team_performances tp " +
                    "on league_teams.league_id = tp.league_id and tp.league_team_id = league_teams.league_team_id and tp.league_id = " +
                    "? order by points desc");
            prepStmt.setInt(1, team.getLeagueId());
            ResultSet resultSet = prepStmt.executeQuery();
                int placement = 1;
            while (resultSet.next()){
                int teamId = resultSet.getInt("tp.league_team_id");
                int id = resultSet.getInt("id");
                String teamName = resultSet.getString("team_name");
                String abbrevation = resultSet.getString("abbrevation");
                int gamesPlayed = resultSet.getInt("games_played");
                int gamesWon = resultSet.getInt("games_won");
                int gamesDrawn = resultSet.getInt("games_drawn");
                int gamesLost = resultSet.getInt("games_lost");
                int points = resultSet.getInt("points");
                TeamStats teamStats = new TeamStats(id, gamesPlayed, gamesWon, gamesLost, gamesDrawn, placement, points);
                if(team.getDatabaseTeamId() != teamId){
                    Team leagueTeam = new Team(teamId, teamName, abbrevation, teamStats);
                    teams.add(leagueTeam);
                }
                else{
                    team.getTeamStats().setPlacement(placement);
                    teams.add(team);
                }
                placement++;
            }
            standings.put(team, FXCollections.observableArrayList(teams));
        }
        return standings;
    }

    public static ObservableList<Game> getGames(Connection databaseConnection, ObservableList<Team> leagueTeams, int roundNo, int leagueId ) throws SQLException {
        ObservableList<Game> games = FXCollections.observableArrayList();

        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from league_games join leagues l " +
                "on league_games.league_id = l.league_id and league_games.round_no = ? and l.league_id = ? ");
        prepStmt.setInt(1, roundNo);
        prepStmt.setInt(2, leagueId);
        ResultSet gamesResultSet = prepStmt.executeQuery();
        while (gamesResultSet.next()){
            int gameId = gamesResultSet.getInt("game_id");
            Date gameDate = gamesResultSet.getDate("game_date_time");
            String gameLocationName = gamesResultSet.getString("game_location_name");
            String gameLocationLink = gamesResultSet.getString("game_location_link");
            String result = gamesResultSet.getString("final_score");
            int homeTeamId = gamesResultSet.getInt("home_team_id");
            int awayTeamId = gamesResultSet.getInt("away_team_id");
            int fileId = gamesResultSet.getInt("file_id");
            Team homeTeam = null;
            Team awayTeam = null;
            for (Team leagueTeam : leagueTeams){
                if(leagueTeam.getDatabaseTeamId() == homeTeamId){
                    homeTeam = leagueTeam;
                }
                if(leagueTeam.getDatabaseTeamId() == awayTeamId){
                    awayTeam = leagueTeam;
                }
            }
            Game game = new Game(gameId, "Game", gameDate, "/views/LeagueScreen.fxml","COLORCODE", roundNo, homeTeam, awayTeam, gameLocationName, gameLocationLink, result, fileId);
            games.add(game);
        }

        return games;
    }

    private static HashMap<Team, ObservableList<Game>> createCurrentRoundGames(Connection databaseConnection, ArrayList<Team> userTeams, HashMap<Team, ObservableList<Team>> standings, int diffFromCurrent ) throws SQLException {
        if(userTeams.isEmpty()){
            return null;
        }
        HashMap<Team, ObservableList<Game>> gamesOfTheCurrentRound = new HashMap<>();
        for (Team team : userTeams){
            ObservableList<Game> games = FXCollections.observableArrayList();
            PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from league_games join leagues l " +
                    "on league_games.league_id = l.league_id and league_games.round_no = (l.current_round - ? )and l.league_id = ? ");
            prepStmt.setInt(1, diffFromCurrent);
            prepStmt.setInt(2, team.getLeagueId());
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
                int fileId = gamesResultSet.getInt("file_id");
                Team homeTeam = null;
                Team awayTeam = null;
                for (Team leagueTeam : standings.get(team)){
                    if(leagueTeam.getDatabaseTeamId() == homeTeamId){
                        homeTeam = leagueTeam;
                    }
                    if(leagueTeam.getDatabaseTeamId() == awayTeamId){
                        awayTeam = leagueTeam;
                    }
                }
                Game game = new Game(gameId, "Game", gameDate, "/views/LeagueScreen.fxml","COLORCODE", roundNo, homeTeam, awayTeam, gameLocationName, gameLocationLink, result, fileId);
                games.add(game);
            }
            gamesOfTheCurrentRound.put(team, games);
        }
        return gamesOfTheCurrentRound;
    }

    private static  TeamMember createUser(Connection databaseConnection, String email, String password) throws SQLException { //TODO If player
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from team_members join file_storage fs on " +
                "fs.id = team_members.file_id and email = ? and password = MD5(?)");
        prepStmt.setString(1,email);
        prepStmt.setString(2,password);

        ResultSet resultSet = prepStmt.executeQuery();
        if(resultSet.next()){
            int memberId = resultSet.getInt("member_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
            String teamRole = resultSet.getString("team_role");
            String sportBranch = resultSet.getString("sport_branch");
            int fileId = resultSet.getInt("file_id");
            Image profilePicture;
            byte[] photoBytes = resultSet.getBytes("file");
            if(photoBytes != null)
            {
                InputStream imageFile = resultSet.getBinaryStream("file");
                profilePicture = new Image(imageFile);
            }
            else{
                profilePicture = null;
            }
            return new TeamMember(memberId, firstName, lastName, birthday, teamRole, email, sportBranch, profilePicture, fileId);
        }
        else{
            return null;
        }
    }

    private static ArrayList<Team> createUserTeams(TeamMember user, Connection databaseConnection) throws SQLException {
        ArrayList<Team> userTeams = new ArrayList<>();
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from team_and_members JOIN team_members tm on " +
                "tm.member_id = team_and_members.team_member_id and tm.member_id = ? " +
                "join teams t on t.team_id = team_and_members.team_id join file_storage fs on fs.id = tm.file_id;");
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
            int teamFileıd = teamsResultSet.getInt("file_id");
            Image teamLogo;
            byte[] photoBytes = teamsResultSet.getBytes("file");
            if(photoBytes != null)
            {
                InputStream imageFile = teamsResultSet.getBinaryStream("file");
                teamLogo = new Image(imageFile);
            }
            else{
                teamLogo = null;
            }
            prepStmt = databaseConnection.prepareStatement("select * from team_and_members join team_members tm on tm.member_id = " +
                    "team_and_members.team_member_id and team_id = ?");
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
                int memberFileId = membersResultSet.getInt("tm.file_id");
                if(user.getMemberId() == memberId){
                    teamMembers.add(user);
                }
                else{
                    teamMembers.add(new TeamMember(memberId, firstName, lastName, birthday, teamRole, email, sportBranch, null, memberFileId));
                }
            }

            prepStmt = databaseConnection.prepareStatement("select * from team_performances join leagues l on l.league_id = team_performances.league_id join league_teams lt on lt.league_team_id = team_performances.league_team_id and team_performances.league_team_id = ?;");
            prepStmt.setInt(1, databaseTeamId);
            ResultSet leagueResultSet = prepStmt.executeQuery();
            String leagueName = "";
            TeamStats teamStats = null;
            String databaseTeamName = "";
            if(leagueResultSet.next()){
                int statsId = leagueResultSet.getInt("id");
                leagueName = leagueResultSet.getString("title");
                int gamesPlayed = leagueResultSet.getInt("games_played");
                int gamesWon = leagueResultSet.getInt("games_won");
                int gamesDrawn = leagueResultSet.getInt("games_drawn");
                int gamesLost = leagueResultSet.getInt("games_lost");
                int points = leagueResultSet.getInt("points");
                int totalRounds = leagueResultSet.getInt("total_rounds");
                databaseTeamName = leagueResultSet.getString("team_name");
                //TODO calculate training averages
                TrainingPerformanceReport trainingPerformanceReport = getTeamTrainingPerformances(teamMembers, databaseConnection);
                //Placement will be modified in standings creation
                teamStats = new TeamStats(statsId, gamesPlayed, gamesWon, gamesLost, gamesDrawn, points, totalRounds, trainingPerformanceReport);
            }
            userTeams.add( new Team(teamId, databaseTeamId, databaseTeamName, leagueId, teamName, abbrevation, teamCode, leagueName, city, ageGroup, teamLogo, teamStats, teamMembers, teamFileıd));
        }
        return userTeams;
    }

    public static UserSession signUpUser(UserSession userSession, String firstName, String lastName, String email, java.sql.Date birthday, String password, String teamRole, String sportBranch, File selectedFile) throws SQLException, IOException {
        int fileId = 1;
        if(selectedFile != null){
            PreparedStatement preparedStatement = userSession.getDatabaseConnection().prepareStatement("INSERT INTO file_storage(file) values (?)",Statement.RETURN_GENERATED_KEYS);
            FileInputStream fileInputStream = new FileInputStream(selectedFile.getAbsolutePath());
            preparedStatement.setBinaryStream(1,fileInputStream,fileInputStream.available());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next())
            {
                fileId = rs.getInt(1);
            }

        }

        PreparedStatement predStmt = userSession.getDatabaseConnection().prepareStatement("INSERT INTO team_members( first_name, last_name, email, " +
                "birthday, password, team_role, sport_branch, file_id) values(?,?,?,?,MD5(?),?,?,?)");

        // Fills the statement with relevant info
        predStmt.setString(1, firstName);
        predStmt.setString(2, lastName);
        predStmt.setString(3, email);
        predStmt.setDate(4, birthday);
        predStmt.setString(5, password);
        predStmt.setString(6, teamRole);
        predStmt.setString(7, sportBranch);


        predStmt.setInt(8,fileId);
        // Prints out a report
        int row = predStmt.executeUpdate();
        if(row > 0) {
            System.out.println("Saved into the database");
            TeamMember user = createUser(userSession.getDatabaseConnection(), email, password);
            userSession.setUser(user);
            userSession.setLastSync(new Date());
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

    public static String isTeamCodeProper(UserSession user, String teamCode) throws SQLException    {
        //TODO age test, code's existence, sport_branch
        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement("select * from teams where team_code = ?");
        prepStmt.setString(1, teamCode);
        ResultSet teamsResultSet = prepStmt.executeQuery();
        if(teamsResultSet.next()){
              int age = Period.between(user.getUser().getBirthday(), LocalDate.now()).getYears();
              int maxAge = Integer.parseInt(teamsResultSet.getString("age_group").substring(1));
              if(age > maxAge && user.getUser().getTeamRole().equals("Player")){
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
        int fileId = 1;
        if(teamLogo != null){
            PreparedStatement preparedStatement = user.getDatabaseConnection().prepareStatement("INSERT INTO file_storage(file) values (?)",Statement.RETURN_GENERATED_KEYS);
            FileInputStream fileInputStream = new FileInputStream(teamLogo.getAbsolutePath());
            preparedStatement.setBinaryStream(1,fileInputStream,fileInputStream.available());
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next())
            {
                fileId = rs.getInt(1);
            }
        }

        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement("select * from league_teams join leagues l on l.league_id = league_teams.league_id and l.title = ? and team_name  = ?");
        prepStmt.setString(1, leagueName);
        prepStmt.setString(2, leagueTeamName);
        ResultSet resultSet = prepStmt.executeQuery();
        if(resultSet.next()){
            PreparedStatement preparedStatement = user.getDatabaseConnection().prepareStatement("INSERT INTO teams(team_name, city, abbrevation, age_group,  " +
                    "team_code, sport_branch, league_id, database_team_id, file_id) values (?,?,?,?,?,?,?,?,?)");
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
            preparedStatement.setInt(9, fileId);

            preparedStatement.executeUpdate();

            PreparedStatement preparedStmt = user.getDatabaseConnection().prepareStatement("select * from teams where team_code = ?");
            preparedStmt.setString(1, teamCode);

            ResultSet teamResultSet = preparedStmt.executeQuery();

            // Assigns user to that team
            if(teamResultSet.next()){
                int teamId = teamResultSet.getInt("team_id");
                prepStmt = user.getDatabaseConnection().prepareStatement("INSERT INTO team_and_members(team_member_id, team_id) VALUES (?,?)");
                prepStmt.setInt(1, user.getUser().getMemberId());
                prepStmt.setInt(2, teamId);

                prepStmt.executeUpdate();
                ArrayList<TeamMember> teamMembers = new ArrayList<>();

                ArrayList<Team> userTeams;
                if(user.getUserTeams().size() == 0){
                    userTeams = new ArrayList<>();
                }
                else{
                    userTeams = user.getUserTeams();
                }
                teamMembers.add(user.getUser());
                if(teamLogo != null){
                    userTeams.add(new Team(teamId, leagueTeamId, leagueTeamName, leagueId, teamName, abbrevation, teamCode, leagueName, city,  ageGroup, new Image(teamLogo.toURI().toString()), null, teamMembers,fileId));
                }
                else{
                    userTeams.add(new Team(teamId, leagueTeamId, leagueTeamName, leagueId, teamName, abbrevation, teamCode, leagueName, city,  ageGroup, null, null, teamMembers, fileId));
                }
                user.setUserTeams(userTeams);
            }
        }
        return user;
    }


    public static Image getPhoto(Connection databaseConnection, int fileId) throws SQLException {
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select file from file_storage where id = ?");
        prepStmt.setInt(1, fileId);
        ResultSet resultSet = prepStmt.executeQuery();

        if(resultSet.next()){
            byte[] photoBytes = resultSet.getBytes("file");
            if(photoBytes != null)
            {
                System.out.println("NOOO");
                InputStream imageFile = resultSet.getBinaryStream("file");
                return new Image(imageFile);
            }
        }
        return null;
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

    //TODO
    private static TrainingPerformanceReport getTeamTrainingPerformances(ArrayList<TeamMember> members, Connection databaseConnection) {
        return null;
    }

    //TODO
    private static TrainingPerformanceReport getMemberTrainingPerformances(int memberId, Connection databaseConnection) {
        return null;
    }

    private static HashMap<Team, ArrayList<CalendarEvent>> createCurrentCalendarEvents(Connection databaseConnection, ArrayList<Team> userTeams) throws SQLException {
        HashMap<Team, ArrayList<CalendarEvent>> teamsAndEvents = new HashMap<>();
        ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
        PreparedStatement preparedStatement = databaseConnection.prepareStatement("select * from calendar_events where event_date_time < NOW() + interval 5 day and " +
                "event_date_time > NOW() or DATE_FORMAT(event_date_time, \"%m-%Y\") = DATE_FORMAT(now(), \"%m-%Y\")");
        ResultSet calendarEventsResultSet = preparedStatement.executeQuery();

        //same for every team
        while (calendarEventsResultSet.next()){
            int eventId = calendarEventsResultSet.getInt("id");
            String title = calendarEventsResultSet.getString("title");
            Date eventDate = calendarEventsResultSet.getDate("event_date_time");
            String actionLink = calendarEventsResultSet.getString("action_link");
            calendarEvents.add(new CalendarEvent(eventId, title, eventDate, actionLink, "calendarEvent"));
        }

        for( Team team : userTeams){
            ArrayList<CalendarEvent> teamEvents = new ArrayList<>();
            for( CalendarEvent ce : calendarEvents){
                teamEvents.add(ce);
            }
            preparedStatement = databaseConnection.prepareStatement("select * from league_games join league_teams lt on " +
                    "lt.league_team_id = league_games.away_team_id where (game_date_time < NOW() + interval 5 day and " +
                    "game_date_time > NOW() or DATE_FORMAT(game_date_time, \"%m-%Y\") = DATE_FORMAT(now(), \"%m-%Y\")) " +
                    "and (home_team_id = ? or away_team_id = ?)");
            preparedStatement.setInt(1, team.getDatabaseTeamId());
            preparedStatement.setInt(2, team.getDatabaseTeamId());
            ResultSet gamesResultSet = preparedStatement.executeQuery();
            while (gamesResultSet.next()){
                int gameId = gamesResultSet.getInt("game_id");
                String title = "Game";
                Date eventDate = gamesResultSet.getDate("game_date_time");
                String actionLink = "/views/LeagueScreen.fxml";
                teamEvents.add(new CalendarEvent(gameId, title, eventDate, actionLink, "game"));
            }

            preparedStatement = databaseConnection.prepareStatement("select * from trainings where training_date_time < NOW() + " +
                    "interval 5 day and training_date_time > NOW() or " +
                    "DATE_FORMAT(training_date_time, \"%m-%Y\") = DATE_FORMAT(now(), \"%m-%Y\") and team_id = ?");
            preparedStatement.setInt(1, team.getTeamId());
            ResultSet trainingsResultSet = preparedStatement.executeQuery();
            while (trainingsResultSet.next()){

                int gameId = trainingsResultSet.getInt("training_id");
                String title = trainingsResultSet.getString("title");
                Date eventDate = trainingsResultSet.getDate("training_date_time");
                String actionLink = "/views/TrainingsScreen.fxml";
                teamEvents.add(new CalendarEvent(gameId, title, eventDate, actionLink, "training"));
            }
            teamsAndEvents.put(team, teamEvents);
        }
        return teamsAndEvents;
    }

    public static ArrayList<CalendarEvent> getCalendarEventByDate(Connection databaseConnection, Team team, java.util.Date date) throws SQLException {
        ArrayList<CalendarEvent> calendarEvents = new ArrayList<>();
        Timestamp ts=new Timestamp(date.getTime());
        PreparedStatement preparedStatement = databaseConnection.prepareStatement("select * from calendar_events where DATE_FORMAT(event_date_time, \"%m-%Y\") = DATE_FORMAT(?, \"%m-%Y\")");
        preparedStatement.setTimestamp(1, ts);
        ResultSet calendarEventsResultSet = preparedStatement.executeQuery();

        //same for every team
        while (calendarEventsResultSet.next()){
            int eventId = calendarEventsResultSet.getInt("id");
            String title = calendarEventsResultSet.getString("title");
            Date eventDate = calendarEventsResultSet.getDate("event_date_time");
            String actionLink = calendarEventsResultSet.getString("action_link");
            calendarEvents.add(new CalendarEvent(eventId, title, eventDate, actionLink, "calendarEvent"));
        }


        preparedStatement = databaseConnection.prepareStatement("select * from league_games join league_teams lt on lt.league_team_id = league_games.away_team_id" +
                " AND DATE_FORMAT(game_date_time, \"%m-%Y\") = DATE_FORMAT(?, \"%m-%Y\") and (home_team_id = ? or away_team_id = ?)");
        preparedStatement.setTimestamp(1, ts);
        System.out.println("Database Team Id " + team.getDatabaseTeamId());
        preparedStatement.setInt(2, team.getDatabaseTeamId());
        preparedStatement.setInt(3, team.getDatabaseTeamId());
        ResultSet gamesResultSet = preparedStatement.executeQuery();

        while (gamesResultSet.next()){
            int gameId = gamesResultSet.getInt("game_id");
            String title = "VS " ;
            Date eventDate = gamesResultSet.getDate("game_date_time");
            String actionLink = "/views/LeagueScreen.fxml";
            calendarEvents.add(new CalendarEvent(gameId, title, eventDate, actionLink, "game"));
        }

        preparedStatement = databaseConnection.prepareStatement("select * from trainings " +
                "where DATE_FORMAT(training_date_time, \"%m-%Y\") = DATE_FORMAT(?, \"%m-%Y\") and team_id = ?");
        preparedStatement.setTimestamp(1, ts);
        preparedStatement.setInt(2, team.getTeamId());
        System.out.println(" Team Id " + team.getTeamId());

        ResultSet trainingsResultSet = preparedStatement.executeQuery();
        while (trainingsResultSet.next()){
            int gameId = trainingsResultSet.getInt("training_id");
            String title = trainingsResultSet.getString("title");
            Date eventDate = trainingsResultSet.getDate("training_date_time");
            String actionLink = "/views/TrainingsScreen.fxml";
            calendarEvents.add(new CalendarEvent(gameId, title, eventDate, actionLink, "training"));
        }

        for (CalendarEvent ce : calendarEvents){
            System.out.println(ce.getEventTitle() + ce.getEventDateTime());
        }
        return calendarEvents;
    }

    public static void updateTeam(Team team, Connection databaseConnection, File logoFile) throws SQLException, IOException {
        PreparedStatement leagueStatement = databaseConnection.prepareStatement("select * from league_teams join leagues l on l.league_id = league_teams.league_id and l.title = ? and team_name  = ?");
        leagueStatement.setString(1, team.getLeagueName());
        leagueStatement.setString(2, team.getDatabaseTeamName());
        ResultSet resultSet = leagueStatement.executeQuery();
        if (resultSet.next()) {
            int leagueId = resultSet.getInt("l.league_id");
            int leagueTeamId = resultSet.getInt("league_team_id");
            team.setLeagueId(leagueId);
            team.setDatabaseTeamId(leagueTeamId);


            if(team.getFileId() != 1 || logoFile == null){
                PreparedStatement changeFileStatement = databaseConnection.prepareStatement("UPDATE file_storage fs SET file = ? WHERE id = ?");
                if (logoFile != null) {
                    FileInputStream fileInputStream = new FileInputStream(logoFile.getAbsolutePath());
                    changeFileStatement.setBinaryStream(1, fileInputStream, fileInputStream.available());
                } else {
                    changeFileStatement.setBlob(1, InputStream.nullInputStream());
                }
                changeFileStatement.setInt(2, team.getFileId());
                changeFileStatement.executeUpdate();
            }
            else{
                PreparedStatement newFileStatement = databaseConnection.prepareStatement("INSERT INTO file_storage(file) values(?)",Statement.RETURN_GENERATED_KEYS);
                FileInputStream fileInputStream = new FileInputStream(logoFile.getAbsolutePath());
                newFileStatement.setBinaryStream(1, fileInputStream, fileInputStream.available());
                newFileStatement.executeUpdate();
                ResultSet rs = newFileStatement.getGeneratedKeys();
                if(rs.next())
                {
                    int fileId = rs.getInt(1);
                    System.out.println("AAAAA");
                    team.setFileId(fileId);
                }
            }



            PreparedStatement prepStmt = databaseConnection.prepareStatement("UPDATE teams t SET t.team_name = ?, t.abbrevation = ?, t.city = ?, t.age_group = ?, t.database_team_id = ?, t.league_id = ?, t.file_id = ?  WHERE t.team_id = ?");
            prepStmt.setString(1, team.getTeamName());
            prepStmt.setString(2, team.getAbbrevation());
            prepStmt.setString(3, team.getCity());
            prepStmt.setString(4, team.getAgeGroup());
            prepStmt.setInt(5, leagueTeamId);
            prepStmt.setInt(6, leagueId);
            prepStmt.setInt(7, team.getFileId());
            prepStmt.setInt(8, team.getTeamId());
            int row = prepStmt.executeUpdate();


        }
    }

    public static void updateUser(UserSession user, File profilePhoto) throws SQLException, IOException {
        PreparedStatement preparedStatement;

        if(user.getUser().getFileId() != 1 || profilePhoto == null){
            preparedStatement = user.getDatabaseConnection().prepareStatement("UPDATE file_storage fs SET fs.file = ? WHERE fs.id = ?");
            if (profilePhoto != null) {
                FileInputStream fileInputStream = new FileInputStream(profilePhoto.getAbsolutePath());
                preparedStatement.setBinaryStream(1, fileInputStream, fileInputStream.available());
            } else {
                preparedStatement.setBlob(1, InputStream.nullInputStream());
            }
            preparedStatement.setInt(2,user.getUser().getFileId());
            preparedStatement.executeUpdate();
        }
        else{
            preparedStatement = user.getDatabaseConnection().prepareStatement("INSERT INTO file_storage(file) values(?)", Statement.RETURN_GENERATED_KEYS);
            FileInputStream fileInputStream = new FileInputStream(profilePhoto.getAbsolutePath());
            preparedStatement.setBinaryStream(1, fileInputStream, fileInputStream.available());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next())
            {
                int fileId = rs.getInt(1);
                user.getUser().setFileId(fileId);
            }
        }

        preparedStatement = user.getDatabaseConnection().prepareStatement("UPDATE team_members t SET t.first_name = ?, t.last_name = ?, " +
                "t.email = ?, t.birthday = ?, t.file_id = ? WHERE t.member_id = ?");
        preparedStatement.setString(1, user.getUser().getFirstName());
        preparedStatement.setString(2, user.getUser().getLastName());
        preparedStatement.setString(3, user.getUser().getEmail());
        preparedStatement.setDate(4, java.sql.Date.valueOf(user.getUser().getBirthday()));
        preparedStatement.setInt(5, user.getUser().getFileId());

        preparedStatement.setInt(6, user.getUser().getMemberId());

        int row = preparedStatement.executeUpdate();
    }

    public static boolean passwordChange(Connection databaseConnection, TeamMember member,  String newPassword) throws SQLException {
        PreparedStatement preparedStatement = databaseConnection.prepareStatement("UPDATE team_members t SET " +
                "t.password = MD5(?) WHERE t.member_id = ?");

        preparedStatement.setString(1, newPassword);
        preparedStatement.setInt(2, member.getMemberId());

        int row = preparedStatement.executeUpdate();
        if(row > 0){
            return  true;
        }
        return false;
    }


    public static boolean saveBasketballStats(UserSession user, TeamMember player, BasketballStats basketballStats, Game game) throws SQLException {
        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement(" INSERT INTO basketball_game_stats(points, assists, rebounds, steals, blocks,member_id,game_id)" +
                "values (?,?,?,?,?,?,?) ");
        prepStmt.setInt(1, Integer.parseInt(basketballStats.getFirstStat()));
        prepStmt.setInt(2, Integer.parseInt(basketballStats.getSecondStat()));
        prepStmt.setInt(3, Integer.parseInt(basketballStats.getThirdStat()));
        prepStmt.setInt(4, Integer.parseInt(basketballStats.getForthStat()));
        prepStmt.setInt(5, Integer.parseInt(basketballStats.getFirstStat()));
        prepStmt.setInt(6, player.getMemberId());
        prepStmt.setInt(7, game.getCalendarEventId());

        int row = prepStmt.executeUpdate();
        if(row > 0){
            return true;
        }
        return false;
    }

    public static boolean saveFootball(UserSession user, TeamMember player, FootballStats basketballStats, Game game) throws SQLException {
        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement(" INSERT INTO football_game_stats(goals, assists, saves, yellowcard, redcard, member_id, game_id)" +
                "values (?,?,?,?,?,?,?) ");
        prepStmt.setInt(1, Integer.parseInt(basketballStats.getFirstStat()));
        prepStmt.setInt(2, Integer.parseInt(basketballStats.getSecondStat()));
        prepStmt.setInt(3, Integer.parseInt(basketballStats.getThirdStat()));
        prepStmt.setInt(4, Integer.parseInt(basketballStats.getForthStat()));
        prepStmt.setInt(5, Integer.parseInt(basketballStats.getFirstStat()));
        prepStmt.setInt(6, player.getMemberId());
        prepStmt.setInt(7, game.getCalendarEventId());

        int row = prepStmt.executeUpdate();
        if(row > 0){
            return true;
        }
        return false;
    }


    public static void createNewTraining(Connection databaseConnection, Training training, ArrayList<TeamMember> additionalPlayers) throws SQLException {
        PreparedStatement prepStmt = databaseConnection.prepareStatement(" INSERT INTO trainings(title, training_date_time, " +
                "location_name, location_link, team_id) VALUES (?, ?, ?, ?,?)");

        prepStmt.setString(1, training.getEventTitle());
        java.sql.Date trainingTime = new java.sql.Date(training.getEventDateTime().getTime());
        prepStmt.setDate(2, trainingTime);
        prepStmt.setString(3, training.getTrainingLocationName());
        prepStmt.setString(4, training.getTrainingLocationLink());
        prepStmt.setInt(5, training.getTeam().getTeamId());


        prepStmt.executeUpdate();

        for( TeamMember player : additionalPlayers){
            prepStmt = databaseConnection.prepareStatement("INSERT INTO training_additional_players(training_id, member_id) VALUES (?,?)");
            prepStmt.setInt(1, training.getCalendarEventId());
            prepStmt.setInt(2, player.getMemberId());

            prepStmt.executeUpdate();
        }
    }

    public static void savePlayerRatings(Connection databaseConnection, Training training, ArrayList<RateHBox> playerRatings) throws SQLException {
        PreparedStatement preparedStatement = databaseConnection.prepareStatement("INSERT INTO training_performances(TRAINING_ID, MEMBER_ID, RATING) " +
                "values (?,?,?)");
        for (RateHBox playerRating : playerRatings){
            if(playerRating.getAttendance()){
                int memberId = playerRating.teamMember.getMemberId();
                int trainingId = training.getCalendarEventId();
                int rating = playerRating.getSliderValue();
                preparedStatement.setInt(1, trainingId);
                preparedStatement.setInt(2, memberId);
                preparedStatement.setInt(3, rating);

                preparedStatement.executeUpdate();
            }
        }
    }

    public static ArrayList<RateHBox> getPlayerRatings(Connection databaseConnection, Training training) throws SQLException {
        ArrayList<RateHBox> ratingBoxes = new ArrayList<>();
        PreparedStatement prepStmt = databaseConnection.prepareStatement("SELECT * from team_members tm left join " +
                "training_performances tp on tm.member_id = tp.member_id join team_and_members tam on " +
                "tm.member_id = tam.team_member_id and team_id = ? and team_role = \"Player\"");
        prepStmt.setInt(1, training.getTeam().getTeamId());

        ResultSet teamsMembersResultSet = prepStmt.executeQuery();
        while(teamsMembersResultSet.next()){
            int memberId = teamsMembersResultSet.getInt("tm.member_id");
            String firstName = teamsMembersResultSet.getString("first_name");
            String lastName = teamsMembersResultSet.getString("last_name");
            TeamMember member = new TeamMember(memberId, firstName, lastName);
            if(teamsMembersResultSet.getInt("tp.id") == 0){
                ratingBoxes.add(new RateHBox(member));
            }
            else{
                int rating = teamsMembersResultSet.getInt("rating");
                ratingBoxes.add(new RateHBox(member, rating));
            }
        }

        prepStmt = databaseConnection.prepareStatement("SELECT * FROM training_additional_players tap" +
                " left join training_performances tp on tap.training_id = tp.training_id and tap.member_id = tp.member_id " +
                " join team_members t on t.member_id = tap.member_id and tap.training_id = ?");

        prepStmt.setInt(1, training.getCalendarEventId());
        ResultSet additionalPlayersResultSet = prepStmt.executeQuery();

        while (additionalPlayersResultSet.next()){
            int memberId = teamsMembersResultSet.getInt("tap.member_id");
            String firstName = teamsMembersResultSet.getString("first_name");
            String lastName = teamsMembersResultSet.getString("last_name");
            TeamMember member = new TeamMember(memberId, firstName, lastName);
            if(teamsMembersResultSet.getInt("tp.id") == 0){
                ratingBoxes.add(new RateHBox(member));
            }
            else{
                int rating = teamsMembersResultSet.getInt("rating");
                ratingBoxes.add(new RateHBox(member, rating));
            }
        }
        return ratingBoxes;
    }

    public static HashMap<Team, ArrayList<Announcement>> createAnnouncements( Connection databaseConnection, ArrayList<Team> userTeams) throws SQLException {
        HashMap<Team, ArrayList<Announcement>> announcements = new HashMap<>();
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from announcements " +
                "join team_members tm on tm.member_id = announcements.sender_id and team_id = ? join file_storage fs on " +
                "fs.id = tm.file_id order by time_sent asc LIMIT 5 ");
        for ( Team userTeam : userTeams)
        {
            prepStmt.setInt(1, userTeam.getTeamId());

            ArrayList<Announcement> teamAnnouncements = new ArrayList<>();
            ResultSet resultSet = prepStmt.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String message = resultSet.getString("message");
                Date timeSent = resultSet.getDate("time_sent");
                int senderId = resultSet.getInt("sender_id");

                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Image profilePicture;
                byte[] photoBytes = resultSet.getBytes("file");
                if(photoBytes != null)
                {
                    InputStream imageFile = resultSet.getBinaryStream("file");
                    profilePicture = new Image(imageFile);
                }
                else{
                    profilePicture = null;
                }

                TeamMember member = new TeamMember(senderId, firstName, lastName, profilePicture);

                teamAnnouncements.add(new Announcement(id, title, message, member, timeSent));
            }
            announcements.put(userTeam, teamAnnouncements);
        }

        return announcements;
    }

    public static TeamMember getTeamMemberByEmail(String text) {
        return null;
    }


    public boolean createNewAnnouncement(Connection databaseConnection, Announcement announcement, Team team) throws SQLException {
        PreparedStatement prepStmt = databaseConnection.prepareStatement("INSERT INTO announcements(team_id, sender_id, title, message, time_sent)" +
                " values (?,?,?,?,?)");
        prepStmt.setInt(1, team.getTeamId());
        prepStmt.setInt(2, announcement.getSender().getMemberId());
        prepStmt.setString(3, announcement.getTitle());
        prepStmt.setString(4, announcement.getDescription());
        java.sql.Date announcedTime = new java.sql.Date(announcement.getTimeSent().getTime());
        prepStmt.setDate(5, announcedTime);

        int row = prepStmt.executeUpdate();
        if(row > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public static Announcement getAnnouncementsByIndex( Connection databaseConnection, Team team, int AnnouncementIndex) throws SQLException {
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from announcements " +
                "join team_members tm on tm.member_id = announcements.sender_id and team_id = ? join file_storage fs on " +
                "fs.id = tm.file_id order by time_sent asc LIMIT ?,1 ");
        prepStmt.setInt(1, team.getTeamId());
        prepStmt.setInt(2, AnnouncementIndex);

        ResultSet resultSet = prepStmt.executeQuery();
        if(resultSet.next()){
            int id = resultSet.getInt("id");
            String title = resultSet.getString("title");
            String message = resultSet.getString("message");
            Date timeSent = resultSet.getDate("time_sent");
            int senderId = resultSet.getInt("sender_id");

            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");

            Image profilePicture;
            byte[] photoBytes = resultSet.getBytes("file");
            if(photoBytes != null)
            {
                InputStream imageFile = resultSet.getBinaryStream("file");
                profilePicture = new Image(imageFile);
            }
            else{
                profilePicture = null;
            }

            TeamMember member = new TeamMember(senderId, firstName, lastName, profilePicture);

           return new Announcement(id, title, message, member, timeSent);
        }
        return null;
    }

    public static void sendNotificationToAUser(UserSession userSession, Notification notification) throws SQLException {
        PreparedStatement preparedStatement = userSession.getDatabaseConnection().prepareStatement("INSERT " +
                "INTO notifications(title, recipent_id, sender_id, message, time_sent, click_action)" +
                "values (?,?,?,?,?,?)");
        preparedStatement.setString(1, notification.getTitle());
        preparedStatement.setInt(2, notification.getRecipient().getMemberId());
        preparedStatement.setInt(3, notification.getSender().getMemberId());
        preparedStatement.setString(4, notification.getDescription());
        java.sql.Date sqlDate = new java.sql.Date(notification.getTimeSent().getTime());
        preparedStatement.setDate(5, sqlDate);
        preparedStatement.setString(6, notification.getClickAction());

        preparedStatement.executeUpdate();
    }

    public static ObservableList<TeamMember> getGameStats(UserSession userSession, Game game, Team team) throws SQLException {
        ObservableList<TeamMember> gamePlayers = FXCollections.observableArrayList();

        PreparedStatement prepStmt;
        if(userSession.getUser().getSportBranch().equals("Basketball")){
            prepStmt = userSession.getDatabaseConnection().prepareStatement("SELECT * from team_members tm left join " +
                    "basketball_game_stats bgs on tm.member_id = bgs.member_id join team_and_members tam on " +
                    "tm.member_id = tam.team_member_id and team_id = ? and team_role = \"Player\"");

            prepStmt.setInt(1, team.getDatabaseTeamId());

            ResultSet memberResultSet = prepStmt.executeQuery();
            while (memberResultSet.next()) {
                int memberId = memberResultSet.getInt("tm.member_id");
                String firstName = memberResultSet.getString("first_name");
                String lastName = memberResultSet.getString("last_name");
                GameStats gameStats;
                if(memberResultSet.getInt("bgs.id") == 0){
                    gameStats = null;
                }
                else {
                    int statId = memberResultSet.getInt("bgs.id");
                    int points = memberResultSet.getInt("points");
                    int assists = memberResultSet.getInt("assists");
                    int rebounds = memberResultSet.getInt("rebounds");
                    int steals = memberResultSet.getInt("steals");
                    int blocks = memberResultSet.getInt("blocks");
                    gameStats = new BasketballStats(statId, "" + points,"" + assists, "" + rebounds,
                            ""+ steals, "" + blocks);

                    TeamMember member = new TeamMember(memberId, firstName, lastName, gameStats);
                    gamePlayers.add(member);
                }
            }

            prepStmt = userSession.getDatabaseConnection().prepareStatement("SELECT * FROM games_additional_players gap" +
                    " left join basketball_game_stats bgs on gap.game_id = bgs.game_id and gap.member_id = bgs.member_id " +
                    " join team_members t on t.member_id = gap.member_id and gap.game_id = ?");
            prepStmt.setInt(1, game.getCalendarEventId());

            ResultSet additionalResultSet = prepStmt.executeQuery();
            while (additionalResultSet.next()){
                int memberId = memberResultSet.getInt("gap.member_id");
                String firstName = memberResultSet.getString("first_name");
                String lastName = memberResultSet.getString("last_name");
                GameStats gameStats;
                if(memberResultSet.getInt("tp.id") == 0){
                    gameStats = null;
                }
                else {
                    int statId = memberResultSet.getInt("bgs.id");
                    int points = memberResultSet.getInt("points");
                    int assists = memberResultSet.getInt("assists");
                    int rebounds = memberResultSet.getInt("rebounds");
                    int steals = memberResultSet.getInt("steals");
                    int blocks = memberResultSet.getInt("blocks");
                    gameStats = new BasketballStats(statId, "" + points,"" + assists, "" + rebounds,
                            ""+ steals, "" + blocks);

                    TeamMember member = new TeamMember(memberId, firstName, lastName, gameStats);
                    gamePlayers.add(member);
                }
            }
        }
        else{
            prepStmt = userSession.getDatabaseConnection().prepareStatement("SELECT * from team_members tm left join " +
                    " football_game_stats fgs on tm.member_id = fgs.member_id join team_and_members tam on " +
                    "tm.member_id = tam.team_member_id and team_id = ? and team_role = \"Player\"");
            prepStmt.setInt(1, team.getDatabaseTeamId());

            ResultSet memberResultSet = prepStmt.executeQuery();
            while (memberResultSet.next()){
                int memberId = memberResultSet.getInt("gap.member_id");
                String firstName = memberResultSet.getString("first_name");
                String lastName = memberResultSet.getString("last_name");
                GameStats gameStats;
                if(memberResultSet.getInt("fgs.id") == 0){
                    gameStats = null;
                }
                else{
                    int statId = memberResultSet.getInt("fgs.id");
                    int goals = memberResultSet.getInt("goals");
                    int assists = memberResultSet.getInt("assists");
                    int fouls_made = memberResultSet.getInt("fouls_made");
                    int passes_made = memberResultSet.getInt("passes_made");
                    int tackles_made = memberResultSet.getInt("tackles_made");
                    gameStats = new FootballStats(statId, "" + goals,"" + assists, "" + fouls_made, "" + passes_made,"" + tackles_made);


                    TeamMember member = new TeamMember(memberId, firstName, lastName, gameStats);
                    gamePlayers.add(member);
                }
            }

            prepStmt = userSession.getDatabaseConnection().prepareStatement("SELECT * FROM games_additional_players gap" +
                    " left join football_game_stats fgs on gap.game_id = fgs.game_id and gap.member_id = fgs.member_id " +
                    " join team_members t on t.member_id = gap.member_id and gap.game_id = ?");
            prepStmt.setInt(1, game.getCalendarEventId());

            ResultSet additionalResultSet = prepStmt.executeQuery();
            while (additionalResultSet.next()){
                int memberId = memberResultSet.getInt("gap.member_id");
                String firstName = memberResultSet.getString("first_name");
                String lastName = memberResultSet.getString("last_name");
                GameStats gameStats;
                if(memberResultSet.getInt("fgs.id") == 0){
                    gameStats = null;
                }
                else{
                    int statId = memberResultSet.getInt("fgs.id");
                    int goals = memberResultSet.getInt("goals");
                    int assists = memberResultSet.getInt("assists");
                    int fouls_made = memberResultSet.getInt("fouls_made");
                    int passes_made = memberResultSet.getInt("passes_made");
                    int tackles_made = memberResultSet.getInt("tackles_made");
                    gameStats = new FootballStats(statId, "" + goals,"" + assists, "" + fouls_made, "" + passes_made,"" + tackles_made);


                    TeamMember member = new TeamMember(memberId, firstName, lastName, gameStats);
                    gamePlayers.add(member);
                }
            }
        }



        return gamePlayers;

    }

    public static Gameplan createNewGameplan(UserSession user, Team team, File gameplanFile, String title) throws SQLException, IOException {


        PreparedStatement prepStmt = user.getDatabaseConnection().prepareStatement("INSERT INTO file_storage(file) value(?)", Statement.RETURN_GENERATED_KEYS);
        FileInputStream fileInputStream = new FileInputStream(gameplanFile);
        prepStmt.setBinaryStream(1, fileInputStream);
        System.out.println("file InputStream "  + fileInputStream);
        System.out.println("Step 1");

        prepStmt.executeUpdate();
        ResultSet rs = prepStmt.getGeneratedKeys();
        if(rs.next())
        {
            System.out.println("Step 2");

            int fileId = rs.getInt(1);
            user.getUser().setFileId(fileId);
            prepStmt = user.getDatabaseConnection().prepareStatement("INSERT INTO gameplans(title, team_id, file_id)" +
                    "values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, title);
            prepStmt.setInt(2, 1);
            prepStmt.setInt(3, fileId);

            prepStmt.executeUpdate();
            rs = prepStmt.getGeneratedKeys();
            if(rs.next()){
                System.out.println("Step 3");
                return new Gameplan(rs.getInt(1), title, fileId);
            }
        }
        return null;
    }


    public static void saveStats(UserSession userSession, Game game, ObservableList<TeamMember> players) throws SQLException {
        if(userSession.getUser().getSportBranch().equals("Basketball")){
            for (TeamMember player : players){
                //if player does not have any previous stat
                PreparedStatement prepStmt;
                if(player.getGameStats().getId() == 0){
                    prepStmt = userSession.getDatabaseConnection().prepareStatement("INSERT " +
                            "INTO basketball_game_stats (points, assists, rebounds, steals, blocks)" +
                            "values (?,?,?,?,?)");

                }
                else{
                    prepStmt = userSession.getDatabaseConnection().prepareStatement("UPDATE basketball_game_stats bgs" +
                            " SET bgs.points = ?, bgs.assists = ?, bgs.rebounds = ?, bgs.steals = ?, bgs.blocks = ? " +
                            "WHERE bgs.id = ?");
                    prepStmt.setInt(6, player.getGameStats().getId());
                }
                prepStmt.setInt(1, Integer.parseInt(player.getFirstColumnData()));
                prepStmt.setInt(2, Integer.parseInt(player.getSecondColumnData()));
                prepStmt.setInt(3, Integer.parseInt(player.getThirdColumnData()));
                prepStmt.setInt(4, Integer.parseInt(player.getForthColumnData()));
                prepStmt.setInt(5, Integer.parseInt(player.getFifthColumnData()));

                prepStmt.executeUpdate();
            }

        }
    }

    public static TeamMember getUserByMail(UserSession userSession,String email) throws SQLException {
        PreparedStatement prepStmt = userSession.getDatabaseConnection().prepareStatement("SELECT member_id FROM team_members " +
                "where email = ?");
        prepStmt.setString(1, email);
        ResultSet resultSet = prepStmt.executeQuery();
        if(resultSet.next()){
            int memberId = resultSet.getInt("member_id");
            return new TeamMember(memberId);
        }
        return null;
    }
}
