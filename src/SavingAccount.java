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