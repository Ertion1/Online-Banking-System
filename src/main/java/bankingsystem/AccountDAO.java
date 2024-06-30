package bankingsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public void createAccount(String accountNumber, String accountHolderName) throws SQLException {
        String query = "INSERT INTO accounts (account_number, account_holder_name) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            statement.setString(2, accountHolderName);
            statement.executeUpdate();
        }
    }

    public Account getAccount(String accountNumber) throws SQLException {
        String query = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Account(resultSet.getInt("id"), resultSet.getString("account_number"), resultSet.getDouble("balance"), resultSet.getString("account_holder_name"));
            }
        }
        return null;
    }

    public void updateBalance(String accountNumber, double amount) throws SQLException {
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setString(2, accountNumber);
            statement.executeUpdate();
        }
    }

    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                accounts.add(new Account(resultSet.getInt("id"), resultSet.getString("account_number"), resultSet.getDouble("balance"), resultSet.getString("account_holder_name")));
            }
        }
        return accounts;
    }
    // Search accounts by account number
    public List<Account> searchAccountsByNumber(String accountNumber) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE account_number LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + accountNumber + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(new Account(resultSet.getInt("id"), resultSet.getString("account_number"), resultSet.getDouble("balance"), resultSet.getString("account_holder_name")));
            }
        }
        return accounts;
    }

    // Search accounts by account holder name
    public List<Account> searchAccountsByName(String accountHolderName) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE account_holder_name LIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + accountHolderName + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                accounts.add(new Account(resultSet.getInt("id"), resultSet.getString("account_number"), resultSet.getDouble("balance"), resultSet.getString("account_holder_name")));
            }
        }
        return accounts;
    }
    // Update account information
    public void updateAccountInfo(int accountId, String newAccountNumber, String newAccountHolderName) throws SQLException {
        String query = "UPDATE accounts SET account_number = ?, account_holder_name = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newAccountNumber);
            statement.setString(2, newAccountHolderName);
            statement.setInt(3, accountId);
            statement.executeUpdate();
        }
    }
    // Close (delete) bank account
    public void closeAccount(int accountId) throws SQLException {
        String query = "DELETE FROM accounts WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, accountId);
            statement.executeUpdate();
        }
    }
}