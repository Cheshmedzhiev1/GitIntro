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