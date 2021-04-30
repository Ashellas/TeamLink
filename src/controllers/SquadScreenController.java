package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import models.*;


public class SquadScreenController implements InitializeData {

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userRoleLabel;

    @FXML
    private Label lastSyncLabel;

    @FXML
    private ComboBox<String> teamBox;

    @FXML
    private ComboBox<String> memberFilterBox;

    @FXML
    private TableView<TeamMember> squadTable;

    @FXML
    private TableColumn<TeamMember, String> firstNameColumn;

    @FXML
    private TableColumn<TeamMember, String> lastNameColumn;

    @FXML
    private TableColumn<TeamMember, String> birthdayColumn;

    @FXML
    private TableColumn<TeamMember, String> teamRoleColumn;

    @FXML
    private TableColumn<TeamMember, String> emailColumn;

    @FXML
    private TableColumn<TeamMember, Button> detailedViewColumn;

    @FXML
    private Pane disablePane;

    @FXML
    private Pane detailsPane;

    @FXML
    private GridPane lastFiveTrainingsGrid;

    @FXML
    private GridPane trainingAveragesGrid;

    @FXML
    private GridPane averageGameStatsGrid;

    @FXML
    private GridPane lastGameStatsGrid;

    @FXML
    private Label detailsFullNameLabel;

    @FXML
    private Label detailsBirthdayLabel;

    @FXML
    private Label detailsEmailLabel;

    @FXML
    private Label detailsTeamRoleLabel;

    private UserSession user;

    private ObservableList<String> teamNames = FXCollections.observableArrayList();

    private ObservableList<String> memberFilterOptions = FXCollections.observableArrayList("All Members", "Players", "Assistant Coaches", "Head Coaches");

    private String[] footballAverages = {"Gpg","Apg","Spg","YCpg", "RCpg"};
    private String[] footballGameStats = {"Gls", "Ast","Svs","YC","RC"};

    private String[] basketballAverages = {"Ppg","Apg","Rpg","Spg", "Bpg"};
    private String[] basketballGameStats = {"Pts", "Ast","Rbd","Stl","Blk"};

    public void initData(UserSession user){
        this.user = user;
        userNameLabel.setText(user.getUser().getFirstName());
        userRoleLabel.setText(user.getUser().getTeamRole());
        if(user.getUser().getProfilePhoto() != null){
            profilePictureImageView.setImage(user.getUser().getProfilePhoto().getImage());
        }
        lastSyncLabel.setText(AppManager.getLastSyncText(user.getLastSync()));
        for(Team team : user.getUserTeams()){
            teamNames.add(team.getTeamName());
        }
        teamBox.getItems().addAll(teamNames);
        memberFilterBox.getItems().addAll(memberFilterOptions);
        teamBox.getSelectionModel().selectFirst();
        memberFilterBox.getSelectionModel().selectFirst();
        updateSquadTable();
        setUpDetailsPane();
        disablePane.setVisible(false);
        detailsPane.setVisible(false);
    }

    private void setUpDetailsPane() {
        //TODO get values from database and show it here
        for(int i = 0; i < 5; i++){
            if(user.getUser().getSportBranch().equals("Basketball")){
                Label averageLabel = new Label(basketballAverages[i]);
                Label statsLabel = new Label(basketballGameStats[i]);
                averageLabel.getStyleClass().add("detailsLabel");
                statsLabel.getStyleClass().add("detailsLabel");
                averageGameStatsGrid.add(averageLabel, i, 0);
                lastGameStatsGrid.add(statsLabel, i, 0);
            }
        }
    }

    public void updateSquadTable() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<TeamMember, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<TeamMember, String>("lastName"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<TeamMember, String>("birthdayString"));
        teamRoleColumn.setCellValueFactory(new PropertyValueFactory<TeamMember, String>("teamRole"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<TeamMember, String>("email"));


        for(Team team : user.getUserTeams()){
            if(team.getTeamName().equals(teamBox.getValue().toString())){
                if(memberFilterBox.getValue().toString().equals("All Members")){
                    ObservableList<TeamMember> allMembers = FXCollections.observableArrayList(team.getTeamMembers());
                    squadTable.setItems(allMembers);
                }
                else if(memberFilterBox.getValue().toString().equals("Players")){
                    squadTable.setItems(team.getMembersWithRole("Player"));
                }
                else if(memberFilterBox.getValue().toString().equals("Assistant Coaches")){
                    squadTable.setItems(team.getMembersWithRole("Assistant Coach"));
                }
                else if(memberFilterBox.getValue().toString().equals("Head Coaches")){
                    squadTable.setItems(team.getMembersWithRole("Head Coach"));
                }
            }
        }

        detailedViewColumn.setCellFactory(ButtonTableCell.<TeamMember>forTableColumn("View", (TeamMember p) -> {
            showPane(p);
            return p;
        }));
    }

    private void showPane(TeamMember member) {
        disablePane.setVisible(true);
        detailsPane.setVisible(true);
        detailsFullNameLabel.setText(member.getFullName());
        detailsBirthdayLabel.setText(member.getBirthdayString());
        detailsEmailLabel.setText(member.getEmail());
        detailsTeamRoleLabel.setText(member.getTeamRole());
    }

    public void toMainScreen(ActionEvent actionEvent) {
    }

    public void toSquadScreen(ActionEvent actionEvent) {
    }

    public void toCalendarScreen(ActionEvent actionEvent) {
    }

    public void toGameplanScreen(ActionEvent actionEvent) {
    }

    public void toTrainingsScreen(ActionEvent actionEvent) {
    }

    public void toLeagueScreen(ActionEvent actionEvent) {
    }

    public void toChatScreen(ActionEvent actionEvent) {
    }

    public void toSettingsScreen(ActionEvent actionEvent) {
    }

    public void logoutButtonPushed(ActionEvent actionEvent) {
    }

    public void helpButtonPushed(ActionEvent actionEvent) {
    }

    public void SynchronizeData(ActionEvent actionEvent) {
    }

    public void closeButtonPushed(ActionEvent event) {
    }
}
