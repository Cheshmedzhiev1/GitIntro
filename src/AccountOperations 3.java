
interface AccountOperations {
    void deposit(double amount);

    boolean withdraw(double amount);

    double getBalance();

    int getAccountId();

    int getCustomerId();

    String getAccountType();

    void addTransaction(Transaction transaction);
}

abstract class BaseAccount implements AccountOperations {
    protected int accountId;
    protected int customerId;
    protected double balance;
    protected String accountType;

    public BaseAccount(int accountId, int customerId, String accountType) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.accountType = accountType;
        this.balance = 0.0;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public int getAccountId() {
        return accountId;
    }

    @Override
    public int getCustomerId() {
        return customerId;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    @Override
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited $" + amount + " to " + accountType + " account");
        }
    }

    @Override
    public abstract boolean withdraw(double amount);

    @Override
    public void addTransaction(Transaction transaction) {

        System.out.println("Transaction recorded for account: " + accountId);
    }
}

class SavingsAccount extends BaseAccount {
    private static final double MINIMUM_BALANCE = 100.0;

    public SavingsAccount(int accountId, int customerId) {
        super(accountId, customerId, "SAVINGS");
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }

        if (balance - amount >= MINIMUM_BALANCE) {
            balance -= amount;
            System.out.println("Withdrew $" + amount + " from Savings account");
            System.out.println("Remaining balance: $" + balance);
            return true;
        } else {
            System.out.println("Insufficient funds. Minimum balance of $" + MINIMUM_BALANCE + " required");
            return false;
        }
    }
}

class CheckingAccount extends BaseAccount {
    private static final double OVERDRAFT_LIMIT = 500.0;

    public CheckingAccount(int accountId, int customerId) {
        super(accountId, customerId, "CHECKING");
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }

        if (balance + OVERDRAFT_LIMIT >= amount) {
            balance -= amount;
            System.out.println("Withdrew $" + amount + " from Checking account");

            if (balance < 0) {
                System.out.println("Account overdrawn. Balance: $" + balance);
                System.out.println("Overdraft used: $" + Math.abs(balance));
            } else {
                System.out.println("Remaining balance: $" + balance);
            }
            return true;
        } else {
            System.out.println("Insufficient funds. Overdraft limit exceeded");
            return false;
        }
    }
}

class BusinessAccount extends BaseAccount {
    private static final double TRANSACTION_FEE = 2.0;

    public BusinessAccount(int accountId, int customerId) {
        super(accountId, customerId, "BUSINESS");
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }

        double totalAmount = amount + TRANSACTION_FEE;

        if (balance >= totalAmount) {
            balance -= totalAmount;
            System.out.println("Withdrew $" + amount + " from Business account");
            System.out.println("Transaction fee: $" + TRANSACTION_FEE);
            System.out.println("Remaining balance: $" + balance);
            return true;
        } else {
            System.out.println("Insufficient funds including transaction fee of $" + TRANSACTION_FEE);
            return false;
        }
    }
}


class BankExample {
    public static void main(String[] args) {

        AccountOperations savings = new SavingsAccount(1001, 123);
        AccountOperations checking = new CheckingAccount(1002, 123);
        AccountOperations business = new BusinessAccount(1003, 456);


        testAccount(savings, "Savings");
        testAccount(checking, "Checking");
        testAccount(business, "Business");
    }

    public static void testAccount(AccountOperations account, String accountName) {
        System.out.println("\n=== Testing " + accountName + " Account ===");

        account.deposit(1000);
        System.out.println("Balance: $" + account.getBalance());

        account.withdraw(200);
        System.out.println("Final balance: $" + account.getBalance());
    }
}