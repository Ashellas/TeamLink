package models;

import java.sql.*;

public class deneme {
    public static void main(String[] args) throws SQLException {
        Connection databaseConnection = DriverManager.getConnection("jdbc:mysql://139.177.181.92:3306/teamlink", "atak", "**CTRLaltBilkentg3m**");
        PreparedStatement preparedStatement = databaseConnection.prepareStatement("SELECT * from team_members left join training_performances tp on team_members.member_id = tp.member_id join team_and_members tam on team_members.member_id = tam.team_member_id and team_id = 1 and team_role = \"Player\"");

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next())
        {
            System.out.println(rs.getInt("tp.id"));

        }
    }
}
