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