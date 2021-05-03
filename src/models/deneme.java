package models;

import java.sql.*;

public class deneme {
    public static void main(String[] args) throws SQLException {
        Connection databaseConnection = DriverManager.getConnection("jdbc:mysql://139.177.181.92:3306/teamlink", "atak", "**CTRLaltBilkentg3m**");
        PreparedStatement preparedStatement = databaseConnection.prepareStatement("INSERT INTO file_storage(file) values (null)",Statement.RETURN_GENERATED_KEYS);

        preparedStatement.executeUpdate();

        ResultSet rs = preparedStatement.getGeneratedKeys();
        if(rs.next())
        {
            System.out.println(rs.getInt(1));
        }
    }
}
