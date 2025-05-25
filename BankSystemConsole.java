
import java.io.*;
import java.util.*;

// === Abstract Account Class ===
abstract class Account implements Serializable {
    protected String accountNumber;
    protected String holderName;
    protected double balance;

    public Account(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public abstract boolean withdraw(double amount);
    public abstract double calculateInterest();

    public String getDetails() {
        return "Account No: " + accountNumber + "\nName: " + holderName + "\nBalance: ₹" + balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }
}

class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.04;

    public SavingsAccount(String accNo, String name, double bal) {
        super(accNo, name, bal);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public double calculateInterest() {
        return balance * INTEREST_RATE;
    }
}

class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 5000;

    public CurrentAccount(String accNo, String name, double bal) {
        super(accNo, name, bal);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= balance + OVERDRAFT_LIMIT) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public double calculateInterest() {
        return 0;
    }
}

public class BankSystemConsole {
    private static final String FILE_NAME = "accounts.dat";
    private static HashMap<String, Account> accounts = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static Account currentUser;

    public static void main(String[] args) {
        loadAccounts();
        while (true) {
            System.out.println("\n=== Welcome to Console Bank System ===");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> {
                    saveAccounts();
                    System.out.println("Thank you for using our system.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Account Type (Savings/Current): ");
        String type = scanner.nextLine();
        String accNo = UUID.randomUUID().toString().substring(0, 6);
        Account acc;
        if (type.equalsIgnoreCase("savings")) {
            acc = new SavingsAccount(accNo, name, 0);
        } else if (type.equalsIgnoreCase("current")) {
            acc = new CurrentAccount(accNo, name, 0);
        } else {
            System.out.println("Invalid account type.");
            return;
        }
        accounts.put(accNo, acc);
        saveAccounts();
        System.out.println("Account created successfully!\nYour Account Number: " + accNo);
    }

    private static void login() {
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine();
        currentUser = accounts.get(accNo);
        if (currentUser != null) {
            dashboard();
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void dashboard() {
        while (true) {
            System.out.println("\n--- Dashboard ---");
            System.out.println("1. View Account Details");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Calculate Interest");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> System.out.println(currentUser.getDetails());
                case 2 -> {
                    System.out.print("Enter amount to deposit: ");
                    double amt = scanner.nextDouble();
                    currentUser.deposit(amt);
                    System.out.println("Amount Deposited.");
                    saveAccounts();
                }
                case 3 -> {
                    System.out.print("Enter amount to withdraw: ");
                    double amt = scanner.nextDouble();
                    if (currentUser.withdraw(amt)) {
                        System.out.println("Amount Withdrawn.");
                    } else {
                        System.out.println("Insufficient funds.");
                    }
                    saveAccounts();
                }
                case 4 -> System.out.println("Interest: ₹" + currentUser.calculateInterest());
                case 5 -> {
                    currentUser = null;
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.out.println("Error saving accounts.");
        }
    }

    private static void loadAccounts() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (HashMap<String, Account>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts.");
        }
    }
}
