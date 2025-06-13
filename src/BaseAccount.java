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