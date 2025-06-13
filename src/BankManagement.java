import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class BankManagement {

    private List<Customer> customers;
    private List<AccountOperations> accounts;
    private List<Transaction> transactions;

    private int nextCustomerId;
    private int nextAccountId;
    private int nextTransactionId;

    private Customer currentLoggedInCustomer;

    private boolean isAccountOwner(int accountId) {
        if (currentLoggedInCustomer == null) {
            System.out.println("Please log in first.");
            return false;
        }

        AccountOperations account = findAccountById(accountId);
        if (account == null) {
            System.out.println("Account not found.");
            return false;
        }

        if (account.getCustomerId() != currentLoggedInCustomer.getCustomerId()) {
            System.out.println("You don't have permission to access this account.");
            return false;
        }

        return true;
    }

    public BankManagement() {
        customers = new ArrayList<>();
        accounts = new ArrayList<>();
        transactions = new ArrayList<>();
        nextCustomerId = 1001;
        nextAccountId = 2001;
        nextTransactionId = 3001;
    }

    public boolean login(String username, String password) {
        for (Customer customer : customers) {
            if (customer.getUsername() != null && customer.getUsername().equals(username) && customer.getPassword() != null && customer.getPassword().equals(password)) {

                currentLoggedInCustomer = customer;
                System.out.println("Login successful! Welcome " + customer.getFirstName() + " " + customer.getLastName());
                return true;
            }
        }

        System.out.println("Login failed! Invalid username or password.");
        currentLoggedInCustomer = null;
        return false;
    }

    public void logout() {
        if (currentLoggedInCustomer != null) {
            System.out.println("Goodbye " + currentLoggedInCustomer.getFirstName() + "!");
            currentLoggedInCustomer = null;
        } else {
            System.out.println("No one is currently logged in.");
        }
    }

    // new customers
    public Customer createCustomer(String firstName, String lastName, int age, String address, String contactNumber, String username, String password) {
        Customer customer = new Customer();
        customer.setCustomerId(nextCustomerId++);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAge(age);
        customer.setAddress(address);
        customer.setContactNumber(contactNumber);
        customer.setUsername(username);
        customer.setPassword(password);


        customers.add(customer);

        System.out.println("Customer created successfully!");
        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Name: " + firstName + " " + lastName);

        return customer;
    }

    // creates acc for existing customers only
    public AccountOperations createAccount(int customerId, String accountType) {

        // checks if customer exists
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer with ID " + customerId + " not found!");
            return null;
        }

        AccountOperations account = null;
        int accountId = nextAccountId++;

        // creates new bank acc types
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                account = new SavingsAccount(accountId, customerId);
                break;
            case "CHECKING":
                account = new CheckingAccount(accountId, customerId);
                break;
            case "BUSINESS":
                account = new BusinessAccount(accountId, customerId);
                break;
            default:
                System.out.println("Invalid account type. Use SAVINGS, CHECKING, or BUSINESS");
                return null;
        }

        accounts.add(account);

        System.out.println("Account created successfully!");
        System.out.println("Account ID: " + accountId);
        System.out.println("Account Type: " + accountType);
        System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName());

        return account;
    }

    // closing acc
    public boolean closeAccount(int accountId) {
        if (!isAccountOwner(accountId)) {
            return false;
        }
        AccountOperations account = findAccountById(accountId);

        if (account == null) {
            System.out.println("Account with ID " + accountId + " not found!");
            return false;
        }

        if (account.getBalance() > 0) {
            System.out.println("Cannot close account with remaining balance of $" + account.getBalance());
            System.out.println("Please withdraw all funds first.");
            return false;
        }

        accounts.remove(account);
        System.out.println("Account " + accountId + " closed successfully!");
        return true;
    }

    // checks balance
    public double checkBalance(int accountId) {
        if (!isAccountOwner(accountId)) {
            return -1;
        }
        AccountOperations account = findAccountById(accountId);
        if (account == null) {
            System.out.println("Account with ID " + accountId + " not found!");
            return -1;
        }

        double balance = account.getBalance();
        System.out.println("Account ID: " + accountId);
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Current Balance: $" + balance);

        return balance;
    }

    // depositvame
    public boolean deposit(int accountId, double amount) {
        if (!isAccountOwner(accountId)) {
            return false;
        }
        if (amount <= 0) {
            System.out.println("Invalid deposit amount. Must be positive.");
            return false;
        }

        AccountOperations account = findAccountById(accountId);
        if (account == null) {
            System.out.println("Account with ID " + accountId + " not found!");
            return false;
        }

        account.deposit(amount);

        // zapis na tranzakciq
        recordTransaction(accountId, amount, "DEPOSIT", "Cash deposit");

        System.out.println("Deposit successful!");
        System.out.println("New balance: $" + account.getBalance());

        return true;
    }

    // teglene
    public boolean withdraw(int accountId, double amount) {
        if (!isAccountOwner(accountId)) {
            return false;
        }
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount. Must be positive.");
            return false;
        }

        AccountOperations account = findAccountById(accountId);
        if (account == null) {
            System.out.println("Account with ID " + accountId + " not found!");
            return false;
        }

        boolean success = account.withdraw(amount);

        if (success) {
            // Record transaction
            recordTransaction(accountId, amount, "WITHDRAWAL", "Cash withdrawal");
            System.out.println("Withdrawal successful!");
        }

        return success;
    }

    //namirame customer
    private Customer findCustomerById(int customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    //namirame acc
    private AccountOperations findAccountById(int accountId) {
        for (AccountOperations account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        return null;
    }

    private void recordTransaction(int accountId, double amount, String type, String description) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(nextTransactionId++);
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setTransactionType(type);
        transaction.setDescription(description);
        transaction.setTransactionTime(getCurrentTimestamp());

        transactions.add(transaction);
    }


    private String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public void viewAllCustomers() {
        System.out.println("\n=== All Customers ===");
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (Customer customer : customers) {
            System.out.println("ID: " + customer.getCustomerId() + " | Name: " + customer.getFirstName() + " " + customer.getLastName() + " | Age: " + customer.getAge());
        }
    }

    public void viewAllAccounts() {
        System.out.println("\n=== All Accounts ===");
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        for (AccountOperations account : accounts) {
            System.out.println("Account ID: " + account.getAccountId() + " | Type: " + account.getAccountType() + " | Customer ID: " + account.getCustomerId() + " | Balance: $" + account.getBalance());
        }
    }

    public void viewTransactionHistory(int accountId) {
        System.out.println("\n=== Transaction History for Account " + accountId + " ===");

        boolean foundTransactions = false;
        for (Transaction transaction : transactions) {
            if (transaction.getAccountId() == accountId) {
                System.out.println("ID: " + transaction.getTransactionId() + " | Type: " + transaction.getTransactionType() + " | Amount: $" + transaction.getAmount() + " | Time: " + transaction.getTransactionTime() + " | Description: " + transaction.getDescription());
                foundTransactions = true;
            }
        }

        if (!foundTransactions) {
            System.out.println("No transactions found for this account.");
        }
    }

    public boolean updateCustomerInfo(String newAddress, String newContactNumber, String newPassword) {
        //we use " null " if we do not want to change any of the above infromation
        if (currentLoggedInCustomer == null) {
            System.out.println("lease log in first to update your information.");
            return false;
        }

        boolean updated = false;

        if (newAddress != null && !newAddress.trim().isEmpty()) {
            currentLoggedInCustomer.setAddress(newAddress);
            System.out.println("Address updated successfully.");
            updated = true;
        }

        if (newContactNumber != null && !newContactNumber.trim().isEmpty()) {
            currentLoggedInCustomer.setContactNumber(newContactNumber);
            System.out.println("Contact number updated successfully.");
            updated = true;
        }

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            currentLoggedInCustomer.setPassword(newPassword);
            System.out.println("Password updated successfully.");
            updated = true;
        }

        if (!updated) {
            System.out.println("No valid information provided for update.");
            return false;
        }

        System.out.println("Customer information updated for: " + currentLoggedInCustomer.getFirstName() + " " + currentLoggedInCustomer.getLastName());
        return true;
    }
}
