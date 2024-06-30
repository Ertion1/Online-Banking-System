package org.example;

import java.sql.*;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "rootroot");
            System.out.println("Connected successfully");

//            Scanner scanner = new Scanner(System.in);
//            String usernameFilter = scanner.nextLine();
//
//            Statement statement = connection.createStatement();
//            ResultSet results = statement.executeQuery("SELECT * FROM users where username='Irikli1' and password='iri2' or 1=1");
//
//            while(results.next()) {
//                System.out.print(results.getInt("id") + ", ");
//                System.out.print(results.getString("username") + ", ");
//                System.out.println(results.getString("password"));
//            }

//            int affectedRows = statement.executeUpdate("DELETE FROM users WHERE password='admin'");
//            System.out.println(affectedRows);

//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USERS WHERE username=?");
//            preparedStatement.setString(1, "admin");

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users VALUES (?, ?, ?)");
            preparedStatement.setInt(1, 6);
            preparedStatement.setString(2, "newUser");
            preparedStatement.setString(3, "newPassword");

            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(affectedRows);
//            while(results.next()) {
//                System.out.print(results.getInt("id") + ", ");
//                System.out.print(results.getString("username") + ", ");
//                System.out.println(results.getString("password"));
//            }

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}