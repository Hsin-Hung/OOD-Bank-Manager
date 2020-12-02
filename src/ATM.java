// all the logics are done in the bank, ATM is just a fascade for the bank
public class ATM {
    Bank bank;// the bank that connects this ATM
    Customer loggedInCustomer;

    public ATM(Bank bank) {
        this.bank = bank;

        startLogin();
    }

    private void startLogin() {
        new LoginScreen(this);
    }

    //create a new customer
    private void signUp() {
        //enter username and password
        // loggedInCustomer = bank.createCustomer(username,password)
    }

    /**
     * Check if valid login, if valid, swaps over to the corrent user screens.
     * @param userName userName that's logging in
     * @param password the inputted password field
     * @return true if login was successful
     */
    public boolean login(String userName, String password) {
        System.out.println("User name: " + userName + " Password: " + password);
        // TODO check login with db, transition into customer or manager. return results.
        new CustomerScreen(this);

        return true;
    }

    public void logout() {
        startLogin();
    }

    private void createCheckingAccount() {
        //bank.createCheckingAccount(Customer customer)
    }

    private void createSavingsAccount() {
        //bank.createSavingsAccount(Customer customer)
    }

    private void createSecuritiesAccount() {
        //bank.createSecuritiesAccount(Customer customer)
    }

    private void deposit() {
        //bank.deposit(BankAccount ba, int amount)
    }

    private void withdraw() {
        //bank.withdraw(BankAccount ba, int amount)
    }

    private void requestLoan() {
        //bank.requestLoan(int amount)
    }

    private void transferMoney() {
        //bank.transferMoney(BankAccount fromBank, BankAccount toBank, int amount)
    }

    private void viewTransactions() {
    }

    private void viewBalance() {
    }

}
