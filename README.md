# multiuser-banking-system

import java.io.*;
import java.util.*;

abstract class Account {
    protected String userId;
    protected String name;
    protected double balance;

    public Account(String userId, String name, double balance) {
        this.userId = userId;
        this.name = name;
        this.balance = balance;
    }

    public abstract double getInterestRate();
    public abstract double getWithdrawalLimit();
    public abstract String getType();

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= getWithdrawalLimit() && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public double getBalance() { return balance; }
}

class SavingsAccount extends Account {
    public SavingsAccount(String userId, String name, double balance) {
        super(userId, name, balance);
    }

    public double getInterestRate() { return 4.0; }
    public double getWithdrawalLimit() { return 10000; }
    public String getType() { return "SAVINGS"; }
}

class CurrentAccount extends Account {
    public CurrentAccount(String userId, String name, double balance) {
        super(userId, name, balance);
    }

    public double getInterestRate() { return 0.5; }
    public double getWithdrawalLimit() { return 50000; }
    public String getType() { return "CURRENT"; }
}

class FileUtils {
    private static final String FILE_NAME = "accounts.txt";

    public static List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                String id = parts[0], name = parts[1], type = parts[2];
                double balance = Double.parseDouble(parts[3]);

                Account acc = type.equals("SAVINGS") ?
                    new SavingsAccount(id, name, balance) :
                    new CurrentAccount(id, name, balance);

                accounts.add(acc);
            }
        } catch (IOException e) {
            System.out.println("No data found. Starting fresh.");
        }
        return accounts;
    }

    public static void saveAccounts(List<Account> accounts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Account acc : accounts) {
                bw.write(acc.getUserId() + "|" + acc.getName() + "|" + acc.getType() + "|" + acc.getBalance() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
}

class AccountDAO {
    private List<Account> accounts;

    public AccountDAO() {
        accounts = FileUtils.loadAccounts();
    }

    public void createAccount(Account acc) {
        accounts.add(acc);
        FileUtils.saveAccounts(accounts);
    }

    public Account getAccount(String userId) {
        for (Account acc : accounts)
            if (acc.getUserId().equals(userId))
                return acc;
        return null;
    }

    public void updateAccount(Account updatedAcc) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUserId().equals(updatedAcc.getUserId())) {
                accounts.set(i, updatedAcc);
                break;
            }
        }
        FileUtils.saveAccounts(accounts);
    }

    public boolean deleteAccount(String userId) {
        boolean removed = accounts.removeIf(acc -> acc.getUserId().equals(userId));
        if (removed) FileUtils.saveAccounts(accounts);
        return removed;
    }

    public List<Account> getAllAccounts() {
        return accounts;
    }
}

public class BankingSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountDAO dao = new AccountDAO();

        while (true) {
            System.out.println("\n===== Multi-User Banking System =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Account");
            System.out.println("5. Delete Account");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    sc.nextLine();
                    System.out.print("User ID: ");
                    String id = sc.nextLine();
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Account Type (SAVINGS/CURRENT): ");
                    String type = sc.nextLine().toUpperCase();
                    System.out.print("Initial Deposit: ");
                    double deposit = sc.nextDouble();

                    Account acc = type.equals("SAVINGS") ?
                            new SavingsAccount(id, name, deposit) :
                            new CurrentAccount(id, name, deposit);
                    dao.createAccount(acc);
                    System.out.println("✅ Account created successfully.");
                }
                case 2 -> {
                    sc.nextLine();
                    System.out.print("User ID: ");
                    String id = sc.nextLine();
                    Account acc = dao.getAccount(id);
                    if (acc != null) {
                        System.out.print("Amount to deposit: ");
                        double amt = sc.nextDouble();
                        acc.deposit(amt);
                        dao.updateAccount(acc);
                        System.out.println("✅ Deposited successfully.");
                    } else System.out.println("❌ Account not found.");
                }
                case 3 -> {
                    sc.nextLine();
                    System.out.print("User ID: ");
                    String id = sc.nextLine();
                    Account acc = dao.getAccount(id);
                    if (acc != null) {
                        System.out.print("Amount to withdraw: ");
                        double amt = sc.nextDouble();
                        if (acc.withdraw(amt)) {
                            dao.updateAccount(acc);
                            System.out.println("✅ Withdrawal successful.");
                        } else System.out.println("❌ Withdrawal failed. Check balance or limit.");
                    } else System.out.println("❌ Account not found.");
                }
                case 4 -> {
                    sc.nextLine();
                    System.out.print("User ID: ");
                    String id = sc.nextLine();
                    Account acc = dao.getAccount(id);
                    if (acc != null) {
                        System.out.println("\n--- Account Details ---");
                        System.out.println("User ID       : " + acc.getUserId());
                        System.out.println("Name          : " + acc.getName());
                        System.out.println("Account Type  : " + acc.getType());
                        System.out.println("Balance       : ₹" + acc.getBalance());
                        System.out.println("Interest Rate : " + acc.getInterestRate() + "%");
                        System.out.println("Withdraw Limit: ₹" + acc.getWithdrawalLimit());
                    } else System.out.println("❌ Account not found.");
                }
                case 5 -> {
                    sc.nextLine();
                    System.out.print("User ID: ");
                    String id = sc.nextLine();
                    if (dao.deleteAccount(id))
                        System.out.println("✅ Account deleted.");
                    else System.out.println("❌ Account not found.");
                }
                case 6 -> {
                    System.out.println("👋 Thank you for using the banking system!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice. Try again.");
            }
        }
    }
}
