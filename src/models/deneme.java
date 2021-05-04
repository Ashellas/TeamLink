package models;

import java.sql.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class deneme {
    public static void main(String[] args) throws SQLException, ParseException {
        Connection databaseConnection = DriverManager.getConnection("jdbc:mysql://139.177.181.92:3306/teamlink", "atak", "**CTRLaltBilkentg3m**");
        PreparedStatement prepStmt = databaseConnection.prepareStatement("select * from trainings");
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()){
            String date = rs.getString("training_date_time");
            Date date1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            System.out.println(date);
        }
    }
}
