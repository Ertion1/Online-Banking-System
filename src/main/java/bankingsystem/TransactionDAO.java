package bankingsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public void createTransaction(int accountId, String type, double amount) throws SQLException {
        String query = "INSERT INTO transactions (account_id, type, amount) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, accountId);
            statement.setString(2, type);
            statement.setDouble(3, amount);
            statement.executeUpdate();
        }
    }

    public List<String> getAccountTransactions(int accountId) throws SQLException {
        List<String> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                transactions.add(resultSet.getString("type") + ": " + resultSet.getDouble("amount") + " on " + resultSet.getTimestamp("timestamp"));
            }
        }
        return transactions;
    }
}