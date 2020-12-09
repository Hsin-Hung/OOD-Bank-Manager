import java.math.BigDecimal;
import java.util.*;

// all the logics are done in the bank, ATM is just a fascade for the bank
public class ATM {
    private Bank bank;// the bank that connects this ATM

    public Customer getLoggedInCustomer() {
        return (Customer) loggedInPerson;
    }

    private Person loggedInPerson;

    public ATM(Bank bank) {
        this.bank = bank;

        startLogin();
    }

    private void startLogin() {
        new LoginScreen(this);
    }

    //create a new customer
    public boolean signUp(String name, String userName, String password) {

        Customer customer = bank.createCustomer(name, userName, password);
        if (customer != null) {
            loggedInPerson = customer;
            new CustomerScreen(this) ;
            return true;
        } else {
            System.out.println("fail to create customer");
            return false;
        }
    }

    /**
     * Check if valid login, if valid, swaps over to the correct user screens.
     *
     * @param userName userName that's logging in
     * @param password the inputted password field
     * @return true if login was successful
     */
    public boolean login(String userName, String password) {
        System.out.println("User name: " + userName + " Password: " + password);

        // TODO check login with db, transition into customer or manager. return results.
        Person person = bank.userAuth(userName, password);
        if (person == null) {
            return false;
        } else {
            loggedInPerson = person;
            switch (person.getRole()) {
                case CUSTOMER:
                    new CustomerScreen(this);
                    return true;
                case MANAGER:
                    new ManagerScreen(this);
                    return true;
                default:
                    // TODO exception
            }
        }

        return true;
    }

    public void logout() {
        startLogin();
    }

    public boolean closeAccount(Customer c, BankAccount bankAccount){

        if(bank.closeAccount(c, bankAccount)){
            return true;
        }

        return false;

    }


    public boolean createCheckingAccount(String currency, BigDecimal startingBalance) {

        return bank.createCheckingAccount(getLoggedInCustomer(), currency, startingBalance);//will return boolean indicate success or not
    }

    public boolean createSavingsAccount(String currency, BigDecimal startingBalance) {

        return bank.createSavingsAccount(getLoggedInCustomer(), currency, startingBalance);//will return boolean indicate success or not
    }

    public boolean createSecuritiesAccount(BigDecimal startingBalance) {

        SavingsAccount savingsAccount = getLoggedInCustomer().getSavingsAccount("USD");

        //check if saving account is >= 5000, if starting balance of securities account is >= 1000, if saving account can maintain 2500
        if((getLoggedInCustomer().getSecuritiesAccount()==null) && startingBalance.compareTo(new BigDecimal(1000)) >=0
                && (savingsAccount.getBalance().subtract(startingBalance).compareTo(new BigDecimal(2500))>=0)){

            return bank.createSecuritiesAccount(getLoggedInCustomer(), "USD", startingBalance);//will return boolean indicate success or not

        }

        return false;

    }

    public boolean buyStock(String symbol, int shares){

        if(shares>=1) return bank.buyStocks(getLoggedInCustomer(), symbol, shares);

        return false;

    }

    public boolean sellStock(String symbol, int shares){

        if(shares>=1) return bank.sellStocks(getLoggedInCustomer(), symbol, shares);

        return false;


    }

    //check if the customer has at least 5000 in USD savings account
    public boolean isQualifiedForSecuritiesAccount(){

        SavingsAccount savingsAccount = getLoggedInCustomer().getSavingsAccount("USD");

        return (savingsAccount != null) && savingsAccount.hasEnoughBalance(Constants.vipThreshold);

    }

    public boolean deposit(Customer c, BankAccount ba, BigDecimal amount) {

        //make sure deposit is positive number
        if (isPositive(amount)) {

            boolean res = bank.deposit(c, ba, amount);//will return boolean indicate success or not

            return res;
        }

        return false;

    }

    public boolean withdraw(Customer c, BankAccount ba, BigDecimal amount) {
        //TODO - database error checking

        //make sure withdraw is positive number
        if (isPositive(amount) && ba.hasEnoughBalance(amount)) {

            boolean res = bank.withdraw(c, ba, amount);//will return boolean indicate success or not
            return res;
        }
        return false;


    }
    public void updateInterest() {
        bank.updateInterests();
    }

    public boolean requestLoan(BigDecimal amount, String currency, String collateral) {

        //TODO - database error checking

        if (isPositive(amount)) {

            return bank.requestLoan(getLoggedInCustomer(), amount, currency, collateral);//will return boolean indicate success or not

        }
        return false;
    }

    public boolean payOffLoan( Loan loan, BigDecimal amount) {
        if (isPositive(amount)) {
            return bank.payOffLoan(getLoggedInCustomer(),loan,amount);
        } else {
            return false;
        }
    }


    //fromBank is always gonna be from one of this logged in customer's bank account. can transfer to any existing account in this bank
    public boolean transferMoney(BankAccount fromBank, int toAccountID, BigDecimal amount) {

        //TODO - database error checking

        if (isPositive(amount) && fromBank.hasEnoughBalance(amount) && fromBank.getAccountID() != toAccountID) {

            return bank.transferMoney(getLoggedInCustomer(), fromBank, toAccountID, amount);

        }
        return false;
    }

    private void viewTransactions(BankAccount ba) {

        //TODO - show the transaction
    }

    private void viewBalance(BankAccount ba) {

        BigDecimal balance = ba.getBalance();
        //TODO - show the balance

    }

    //helper function
    private boolean isPositive(BigDecimal val) {
        return val.compareTo(new BigDecimal(0)) == 1;
    }

    public List<Customer> checkCustomer(){
        return bank.checkCustomer();
    }

    public List<Transaction> getDailyReportWithin24hrs(){
        return bank.getDailyReportWithin24hrs();
    }

    public HashMap<String, BankMainAccount> getBankBalances() {
        return bank.getBankBalances();
    }



    }
