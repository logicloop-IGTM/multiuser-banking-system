# Bank System Console Application

## Overview

This Java console-based application simulates a simple multi-user banking system, allowing users to create and manage Savings and Current accounts through a menu-driven interface. Account data is persisted to a file, ensuring that account information is retained between sessions.

## Features

* **Account Types**: Supports both Savings and Current accounts.
* **Create Account**: Generates a unique 6-character account number using UUID.
* **Deposit Funds**: Allows depositing any positive amount into an account.
* **Withdraw Funds**: Enables withdrawals with balance checks; Savings accounts may include interest logic, Current accounts support overdraft up to a limit.
* **Check Balance**: Displays the current balance of a specified account.
* **Display All Accounts**: Lists all existing accounts with details.
* **Data Persistence**: Saves account data to `accounts.dat` via Java serialization and loads it on startup.

## Prerequisites

* Java Development Kit (JDK) 8 or higher
* Command-line terminal or any Java-compatible IDE (e.g., IntelliJ IDEA, Eclipse)

## Setup and Compilation

1. **Navigate** to the project directory containing `BankSystemConsole.java`.
2. **Compile** the source file:

```bash
javac BankSystemConsole.java
```

## Running the Application

After successful compilation, run the program:

```bash
java BankSystemConsole
```

You will see a menu with options to create accounts, deposit, withdraw, check balances, display all accounts, or exit.

## Usage Guide

1. **Create Account**: Select option 1, enter the account holder's name and type (`Savings` or `Current`).
2. **Deposit Funds**: Select option 2, provide the account number and deposit amount.
3. **Withdraw Funds**: Select option 3, provide the account number and withdrawal amount.
4. **Check Balance**: Select option 4, enter the account number to view the balance.
5. **Display All Accounts**: Select option 5 to list every account stored in the system.
6. **Exit**: Select option 6 to save accounts to `accounts.dat` and exit the application.

## File Structure

```
├── BankSystemConsole.java   # Main application source code
└── accounts.dat             # Serialized account data file (generated at runtime)
```

## Data Persistence

* The application automatically **loads** existing account data from `accounts.dat` on startup.
* On exit, it **saves** all accounts back to `accounts.dat` ensuring data continuity.

## Extensibility

* **Interest Calculation**: Extend the `SavingsAccount` class to compute interest periodically.
* **Overdraft Settings**: Customize overdraft limits for `CurrentAccount`.
* **User Authentication**: Integrate login credentials for enhanced security.

## License

This project is licensed under the MIT License. Feel free to use, modify, and distribute.
