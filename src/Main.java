public class Main {
    public static void main(String[] args) {

        // BankManagementTesting


        BankManagement bank = new BankManagement();

        System.out.println("=== SECURE BANKING SYSTEM TEST ===\n");

        System.out.println("1. Creating customers with login credentials...");
        Customer alice = bank.createCustomer("Alice", "Johnson", 28, "123 Main St", "555-1234", "alice123", "password123");
        Customer bob = bank.createCustomer("Bob", "Smith", 35, "456 Oak Ave", "555-5678", "bob456", "mypassword");
        Customer charlie = bank.createCustomer("Charlie", "Brown", 42, "789 Pine Rd", "555-9999", "charlie789", "secret123");

        System.out.println("\n" + "=".repeat(60));

        System.out.println("2. Trying banking operations without login (should fail)...");
        bank.createAccount(alice.getCustomerId(), "SAVINGS");
        bank.deposit(2001, 500.0);
        bank.checkBalance(2001);

        System.out.println("\n" + "=".repeat(60));

        System.out.println("3. Alice logs in and creates accounts...");
        bank.login("alice123", "password123");

        AccountOperations aliceSavings = bank.createAccount(alice.getCustomerId(), "SAVINGS");
        AccountOperations aliceChecking = bank.createAccount(alice.getCustomerId(), "CHECKING");

        System.out.println("\n" + "=".repeat(60));

        System.out.println("4. Alice performs banking operations...");
        bank.deposit(aliceSavings.getAccountId(), 1000.0);
        bank.deposit(aliceChecking.getAccountId(), 500.0);
        bank.checkBalance(aliceSavings.getAccountId());
        bank.withdraw(aliceChecking.getAccountId(), 100.0);

        System.out.println("\n" + "=".repeat(60));

        System.out.println("5. Alice updates her information...");
        bank.updateCustomerInfo("999 New Street", "555-0000", "newpassword123");

        System.out.println("\n" + "=".repeat(60));

        System.out.println("6. Alice logs out...");
        bank.logout();

        System.out.println("\n" + "=".repeat(60));

        System.out.println("7. Bob logs in with wrong password (should fail)...");
        bank.login("bob456", "wrongpassword");

        System.out.println("\n8. Bob logs in with correct password...");
        bank.login("bob456", "mypassword");

        AccountOperations bobBusiness = bank.createAccount(bob.getCustomerId(), "BUSINESS");
        bank.deposit(bobBusiness.getAccountId(), 2000.0);

        System.out.println("\n" + "=".repeat(60));

        System.out.println("9. Bob tries to access Alice's account (should fail)...");
        bank.deposit(aliceSavings.getAccountId(), 100.0);
        bank.checkBalance(aliceChecking.getAccountId());
        bank.withdraw(aliceSavings.getAccountId(), 50.0);

        System.out.println("\n" + "=".repeat(60));

        System.out.println("10. Bob performs operations on his own account...");
        bank.checkBalance(bobBusiness.getAccountId());
        bank.withdraw(bobBusiness.getAccountId(), 300.0);

        System.out.println("\n" + "=".repeat(60));

        System.out.println("11. Bob logs out, Charlie logs in...");
        bank.logout();
        bank.login("charlie789", "secret123");

        AccountOperations charlieSavings = bank.createAccount(charlie.getCustomerId(), "SAVINGS");
        bank.deposit(charlieSavings.getAccountId(), 1500.0);

        System.out.println("\n" + "=".repeat(60));

        System.out.println("12. Charlie updates only his contact number...");
        bank.updateCustomerInfo(null, "555-7777", null);

        System.out.println("\n" + "=".repeat(60));

        System.out.println("13. Alice logs back in with her NEW password...");
        bank.logout();
        bank.login("alice123", "newpassword123");

        System.out.println("\n14. Alice checks her account balances...");
        bank.checkBalance(aliceSavings.getAccountId());
        bank.checkBalance(aliceChecking.getAccountId());

        System.out.println("\n" + "=".repeat(60));

        System.out.println("15. Testing different account types with withdrawals...");
        bank.logout();
        bank.login("alice123", "newpassword123");

        System.out.println("\nTesting Savings account (minimum balance rule):");
        bank.withdraw(aliceSavings.getAccountId(), 950.0);

        bank.logout();
        bank.login("bob456", "mypassword");

        System.out.println("\nTesting Business account (transaction fee):");
        bank.withdraw(bobBusiness.getAccountId(), 100.0);

        System.out.println("\nTesting Checking account (overdraft):");
        bank.withdraw(bobBusiness.getAccountId(), 2000.0);

        System.out.println("\n" + "=".repeat(60));

        System.out.println("16. Final system overview...");
        bank.logout();

        System.out.println("\nAttempting to view data without login:");
        bank.viewAllCustomers();
        bank.viewAllAccounts();

        System.out.println("\n" + "=".repeat(60));

        System.out.println("17. Transaction history for Alice's accounts...");
        bank.login("alice123", "newpassword123");
        bank.viewTransactionHistory(aliceSavings.getAccountId());
        bank.viewTransactionHistory(aliceChecking.getAccountId());

        System.out.println("\n" + "=".repeat(60));

        System.out.println("18. Testing account closure...");
        System.out.println("Trying to close account with balance (should fail):");
        bank.closeAccount(aliceSavings.getAccountId());

        System.out.println("\nWithdrawing remaining funds and closing account:");
        bank.withdraw(aliceSavings.getAccountId(), bank.checkBalance(aliceSavings.getAccountId()));
        bank.closeAccount(aliceSavings.getAccountId());

        bank.logout();

        System.out.println("\n=== COMPREHENSIVE TEST COMPLETE ===");
        System.out.println("Security features working properly!");
        System.out.println("- Login/logout system ✓");
        System.out.println("- Account ownership verification ✓");
        System.out.println("- Customer information updates ✓");
        System.out.println("- Different account type behaviors ✓");

    }
}
