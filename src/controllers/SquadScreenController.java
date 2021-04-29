package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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

    private UserSession user;

    private ObservableList<String> teamNames = FXCollections.observableArrayList();

    private ObservableList<String> memberFilterOptions = FXCollections.observableArrayList("All Members", "Players", "Assistant Coaches", "Head Coaches");

    public void initData(UserSession user){
        this.user = user;
        userNameLabel.setText(user.getUser().getFirstName());
        userRoleLabel.setText(user.getUser().getTeamRole());
        if(user.getUser().getProfilePhoto() != null){
            System.out.println("OMG");
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
            showPane();
            return p;
        }));
    }

    public  void showPane(){

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
}
