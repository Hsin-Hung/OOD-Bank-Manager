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
                    new CustomerScreen(this) ;
                    return true;
                case MANAGER:
                    // TODO create new manager screen;
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

            //TODO - display the fees and info for closing account

            return true;
        }

        return false;

    }


    public boolean createCheckingAccount(String currency, BigDecimal startingBalance) {
        //TODO - database error checking
        return bank.createCheckingAccount(getLoggedInCustomer(), currency, startingBalance);//will return boolean indicate success or not
    }

    public boolean createSavingsAccount(String currency, BigDecimal startingBalance) {
        //TODO - database error checking
        return bank.createSavingsAccount(getLoggedInCustomer(), currency, startingBalance);//will return boolean indicate success or not
    }

    public boolean createSecuritiesAccount(String currency, BigDecimal startingBalance) {
        //TODO - database error checking

        SavingsAccount savingsAccount = getLoggedInCustomer().getSavingsAccount();

        //check if saving account is >= 5000, if starting balance of securities account is >= 1000, if saving account can maintain 2500
        if(savingsAccount.hasEnoughBalance(new BigDecimal(5000)) && startingBalance.compareTo(new BigDecimal(1000)) >=0
                && (savingsAccount.getBalance().subtract(startingBalance).compareTo(new BigDecimal(2500))>=0)){

            return bank.createSecuritiesAccount(getLoggedInCustomer(), currency, startingBalance);//will return boolean indicate success or not

        }

        return false;

    }

    private boolean deposit(Customer c, BankAccount ba, BigDecimal amount) {

        //TODO - database error checking

        //make sure deposit is positive number
        if (isPositive(amount)) {

            return bank.deposit(c, ba, amount);//will return boolean indicate success or not

        }

        return false;

    }

    private boolean withdraw(Customer c, BankAccount ba, BigDecimal amount) {
        //TODO - database error checking

        //make sure withdraw is positive number
        if (isPositive(amount) && ba.hasEnoughBalance(amount)) {

            return bank.withdraw(c, ba, amount);//will return boolean indicate success or not
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

    private boolean transferMoney(Customer c, BankAccount fromBank, BankAccount toBank, BigDecimal amount) {

        //TODO - database error checking

        if (isPositive(amount) && fromBank.hasEnoughBalance(amount)) {

            return bank.transferMoney(c, fromBank, toBank, amount);

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

    private void checkCustomer(){

        //TODO - display info of all customers or specific customer
        List<Customer> allCustomers = bank.checkCustomer();


    }

    private void getDailyReportWithin24hrs(){

        //TODO - display all transactions within 24 hrs
        List<Transaction> within24Transactions =  bank.getDailyReportWithin24hrs();


    }


    //starting point when using the ATM, which contains the flow
    public void useATM() {


        //login screen
        //customer screen
        //....

    }

}
