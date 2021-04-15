package models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserSession {

    String firstName;
    Connection myCon;


    public UserSession(String firstName) throws SQLException {
        this.firstName = firstName;
    }

    private void setMyCon() throws SQLException {
        myCon = DriverManager.getConnection("jdbc:mysql://139.177.181.92:3306/teamlink", "atak", "**CTRLaltBilkentg3m**");
    }

    public String getFirstName() {
        return firstName;
    }

    public List<String> getTeamMemberName() throws SQLException {
        setMyCon();
        Statement prepStmt = myCon.createStatement();
        ResultSet resultSet = prepStmt.executeQuery("SELECT * FROM team_members");
        List<String> listOfNames = new ArrayList<>();
        if(resultSet.next()){
            listOfNames.add(resultSet.getString("first_name"));
        }
        return listOfNames;
    }
}
