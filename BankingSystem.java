import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BankingSystem extends JFrame {

    private static ArrayList<User> users = new ArrayList<>();

    public BankingSystem() {
        setTitle("Banking System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 1));

        JButton createAccountButton = new JButton("Create Account");
        JButton transactionHistoryButton = new JButton("Transaction History");
        JButton transferFundsButton = new JButton("Transfer Funds");
        JButton balanceEnquiryButton = new JButton("Balance Enquiry");
        JButton loanManagementButton = new JButton("Loan Management");
        JButton loginButton = new JButton("Login");

        createAccountButton.addActionListener(new CreateAccountListener());
        transactionHistoryButton.addActionListener(new TransactionHistoryListener());
        transferFundsButton.addActionListener(new TransferFundsListener());
        balanceEnquiryButton.addActionListener(new BalanceEnquiryListener());
        loanManagementButton.addActionListener(new LoanManagementListener());
        loginButton.addActionListener(new LoginListener());

        mainPanel.add(createAccountButton);
        mainPanel.add(transactionHistoryButton);
        mainPanel.add(transferFundsButton);
        mainPanel.add(balanceEnquiryButton);
        mainPanel.add(loanManagementButton);
        mainPanel.add(loginButton);

        add(mainPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new BankingSystem();
    }

    private static class User {
        String username;
        String password;
        Account checkingAccount;
        Account savingsAccount;
        ArrayList<Transaction> transactionHistory = new ArrayList<>();
        ArrayList<Loan> loans = new ArrayList<>();

        User(String username, String password) {
            this.username = username;
            this.password = password;
            this.checkingAccount = new Account("Checking");
            this.savingsAccount = new Account("Savings");
        }
    }

    private static class Account {
        String type;
        double balance;

        Account(String type) {
            this.type = type;
            this.balance = 0.0;
        }
    }

    private static class Transaction {
        String type;
        double amount;
        String fromAccount;
        String toAccount;

        Transaction(String type, double amount, String fromAccount, String toAccount) {
            this.type = type;
            this.amount = amount;
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
        }
    }

    private static class Loan {
        double amount;
        double remainingAmount;

        Loan(double amount) {
            this.amount = amount;
            this.remainingAmount = amount;
        }
    }

    private static class CreateAccountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame createAccountFrame = new JFrame("Create Account");
            createAccountFrame.setSize(300, 200);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2));

            JLabel userLabel = new JLabel("Username:");
            JTextField userText = new JTextField();
            JLabel passwordLabel = new JLabel("Password:");
            JPasswordField passwordText = new JPasswordField();

            JButton createButton = new JButton("Create");

            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String username = userText.getText();
                    String password = new String(passwordText.getPassword());
                    users.add(new User(username, password));
                    JOptionPane.showMessageDialog(createAccountFrame, "Account created successfully!");
                    createAccountFrame.dispose();
                }
            });

            panel.add(userLabel);
            panel.add(userText);
            panel.add(passwordLabel);
            panel.add(passwordText);
            panel.add(new JLabel());
            panel.add(createButton);

            createAccountFrame.add(panel);
            createAccountFrame.setVisible(true);
        }
    }

    private static class TransactionHistoryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User user = authenticateUser();
            if (user == null) return;

            JFrame transactionHistoryFrame = new JFrame("Transaction History");
            transactionHistoryFrame.setSize(400, 300);

            JTextArea textArea = new JTextArea();
            for (Transaction t : user.transactionHistory) {
                textArea.append(t.type + " of $" + t.amount + " from " + t.fromAccount + " to " + t.toAccount + "\n");
            }
            transactionHistoryFrame.add(new JScrollPane(textArea));
            transactionHistoryFrame.setVisible(true);
        }
    }

    private static class TransferFundsListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User user = authenticateUser();
            if (user == null) return;

            JFrame transferFundsFrame = new JFrame("Transfer Funds");
            transferFundsFrame.setSize(300, 200);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2));

            JLabel amountLabel = new JLabel("Amount:");
            JTextField amountText = new JTextField();
            JLabel fromLabel = new JLabel("From (Checking/Savings):");
            JTextField fromText = new JTextField();
            JLabel toLabel = new JLabel("To (Checking/Savings):");
            JTextField toText = new JTextField();

            JButton transferButton = new JButton("Transfer");

            transferButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double amount = Double.parseDouble(amountText.getText());
                    String from = fromText.getText();
                    String to = toText.getText();

                    if (from.equalsIgnoreCase("Checking") && to.equalsIgnoreCase("Savings")) {
                        if (user.checkingAccount.balance >= amount) {
                            user.checkingAccount.balance -= amount;
                            user.savingsAccount.balance += amount;
                            user.transactionHistory.add(new Transaction("Transfer", amount, "Checking", "Savings"));
                            JOptionPane.showMessageDialog(transferFundsFrame, "Funds transferred successfully!");
                        } else {
                            JOptionPane.showMessageDialog(transferFundsFrame, "Insufficient funds in Checking account!");
                        }
                    } else if (from.equalsIgnoreCase("Savings") && to.equalsIgnoreCase("Checking")) {
                        if (user.savingsAccount.balance >= amount) {
                            user.savingsAccount.balance -= amount;
                            user.checkingAccount.balance += amount;
                            user.transactionHistory.add(new Transaction("Transfer", amount, "Savings", "Checking"));
                            JOptionPane.showMessageDialog(transferFundsFrame, "Funds transferred successfully!");
                        } else {
                            JOptionPane.showMessageDialog(transferFundsFrame, "Insufficient funds in Savings account!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(transferFundsFrame, "Invalid accounts specified!");
                    }
                }
            });

            panel.add(amountLabel);
            panel.add(amountText);
            panel.add(fromLabel);
            panel.add(fromText);
            panel.add(toLabel);
            panel.add(toText);
            panel.add(new JLabel());
            panel.add(transferButton);

            transferFundsFrame.add(panel);
            transferFundsFrame.setVisible(true);
        }
    }

    private static class BalanceEnquiryListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User user = authenticateUser();
            if (user == null) return;

            JFrame balanceEnquiryFrame = new JFrame("Balance Enquiry");
            balanceEnquiryFrame.setSize(300, 200);

            JTextArea textArea = new JTextArea();
            textArea.append("Checking Account Balance: $" + user.checkingAccount.balance + "\n");
            textArea.append("Savings Account Balance: $" + user.savingsAccount.balance + "\n");
            balanceEnquiryFrame.add(new JScrollPane(textArea));
            balanceEnquiryFrame.setVisible(true);
        }
    }

    private static class LoanManagementListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User user = authenticateUser();
            if (user == null) return;

            JFrame loanManagementFrame = new JFrame("Loan Management");
            loanManagementFrame.setSize(300, 200);

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 2));

            JLabel loanAmountLabel = new JLabel("Loan Amount:");
            JTextField loanAmountText = new JTextField();

            JButton applyLoanButton = new JButton("Apply for Loan");

            applyLoanButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    double loanAmount = Double.parseDouble(loanAmountText.getText());
                    user.loans.add(new Loan(loanAmount));
                    JOptionPane.showMessageDialog(loanManagementFrame, "Loan applied successfully!");
                }
            });

            panel.add(loanAmountLabel);
            panel.add(loanAmountText);
            panel.add(new JLabel());
            panel.add(applyLoanButton);

            loanManagementFrame.add(panel);
            loanManagementFrame.setVisible(true);
        }
    }

    private static class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            User user = authenticateUser();
            if (user == null) return;

            JFrame loginFrame = new JFrame("Login");
            loginFrame.setSize(300, 200);

            JTextArea textArea = new JTextArea();
            textArea.append("Login successful for user: " + user.username + "\n");
            loginFrame.add(new JScrollPane(textArea));
            loginFrame.setVisible(true);
        }
    }

    private static User authenticateUser() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            for (User user : users) {
                if (user.username.equals(username) && user.password.equals(password)) {
                    return user;
                }
            }

            JOptionPane.showMessageDialog(null, "Invalid username or password!");
        }
        return null;
    }
}
