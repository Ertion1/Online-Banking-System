package bankingsystem;

import bankingsystem.DAO.AccountDAO;
import bankingsystem.DAO.TransactionDAO;
import bankingsystem.DAO.UserDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static AccountDAO accountDAO = new AccountDAO();
    private static TransactionDAO transactionDAO = new TransactionDAO();
    private static UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        User loggedInUser = login(scanner);
        if (loggedInUser == null) {
            System.out.println("Invalid login. Exiting...");
            return;
        }

        while (true) {
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. View All Accounts");
            System.out.println("6. View Account Transactions");
            if (loggedInUser.isAdmin()) {
                System.out.println("7. Search Accounts");
                System.out.println("8. Update Account Information");
                System.out.println("9. Close Bank Account");
            }
            System.out.println("10. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter account number: ");
                        String accountNumber = scanner.nextLine();
                        System.out.print("Enter account holder name: ");
                        String accountHolderName = scanner.nextLine();
                        accountDAO.createAccount(accountNumber, accountHolderName);
                        break;
                    case 2:
                        System.out.print("Enter account number: ");
                        accountNumber = scanner.nextLine();
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = scanner.nextDouble();
                        accountDAO.updateBalance(accountNumber, depositAmount);
                        transactionDAO.createTransaction(accountDAO.getAccount(accountNumber).getId(), "DEPOSIT", depositAmount);
                        break;
                    case 3:
                        System.out.print("Enter account number: ");
                        accountNumber = scanner.nextLine();
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = scanner.nextDouble();
                        accountDAO.updateBalance(accountNumber, -withdrawAmount);
                        transactionDAO.createTransaction(accountDAO.getAccount(accountNumber).getId(), "WITHDRAW", withdrawAmount);
                        break;
                    case 4:
                        System.out.print("Enter source account number: ");
                        String sourceAccountNumber = scanner.nextLine();
                        System.out.print("Enter target account number: ");
                        String targetAccountNumber = scanner.nextLine();
                        System.out.print("Enter amount to transfer: ");
                        double transferAmount = scanner.nextDouble();
                        accountDAO.updateBalance(sourceAccountNumber, -transferAmount);
                        accountDAO.updateBalance(targetAccountNumber, transferAmount);
                        transactionDAO.createTransaction(accountDAO.getAccount(sourceAccountNumber).getId(), "TRANSFER_OUT", transferAmount);
                        transactionDAO.createTransaction(accountDAO.getAccount(targetAccountNumber).getId(), "TRANSFER_IN", transferAmount);
                        break;
                    case 5:
                        List<Account> accounts = accountDAO.getAllAccounts();
                        for (Account account : accounts) {
                            System.out.println("Account Number: " + account.getAccountNumber() + ", Balance: " + account.getBalance() + ", Account Holder: " + account.getAccountHolderName());
                        }
                        break;
                    case 6:
                        System.out.print("Enter account number: ");
                        accountNumber = scanner.nextLine();
                        List<String> transactions = transactionDAO.getAccountTransactions(accountDAO.getAccount(accountNumber).getId());
                        for (String transaction : transactions) {
                            System.out.println(transaction);
                        }
                        break;
                    case 7:
                        if (loggedInUser.isAdmin()) {
                            System.out.print("Enter account number or name to search: ");
                            String searchQuery = scanner.nextLine();
                            List<Account> foundAccountsByNumber = accountDAO.searchAccountsByNumber(searchQuery);
                            List<Account> foundAccountsByName = accountDAO.searchAccountsByName(searchQuery);

                            System.out.println("Accounts found by number:");
                            for (Account account : foundAccountsByNumber) {
                                System.out.println("Account Number: " + account.getAccountNumber() + ", Balance: " + account.getBalance() + ", Account Holder: " + account.getAccountHolderName());
                            }

                            System.out.println("Accounts found by name:");
                            for (Account account : foundAccountsByName) {
                                System.out.println("Account Number: " + account.getAccountNumber() + ", Balance: " + account.getBalance() + ", Account Holder: " + account.getAccountHolderName());
                            }
                        } else {
                            System.out.println("Invalid choice. Try again.");
                        }
                        break;
                    case 8:
                        if (loggedInUser.isAdmin()) {
                            System.out.print("Enter account ID to update: ");
                            int accountIdToUpdate = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Enter new account number: ");
                            String newAccountNumber = scanner.nextLine();
                            System.out.print("Enter new account holder name: ");
                            String newAccountHolderName = scanner.nextLine();
                            accountDAO.updateAccountInfo(accountIdToUpdate, newAccountNumber, newAccountHolderName);
                        } else {
                            System.out.println("Invalid choice. Try again.");
                        }
                        break;
                    case 9:
                        if (loggedInUser.isAdmin()) {
                            System.out.print("Enter account ID to close: ");
                            int accountIdToClose = scanner.nextInt();
                            scanner.nextLine();
                            accountDAO.closeAccount(accountIdToClose);
                        } else {
                            System.out.println("Invalid choice. Try again.");
                        }
                        break;
                    case 10:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static User login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = userDAO.login(username, password);
            if (user != null) {
                System.out.println("Login successful. Welcome " + user.getUsername() + " (" + user.getRole() + ")");
                return user;
            } else {
                System.out.println("Invalid username or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}