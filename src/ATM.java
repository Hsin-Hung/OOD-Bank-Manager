import java.math.BigDecimal;
import java.util.*;

// all the logics are done in the bank, ATM is just a fascade for the bank
public class ATM {
    private Bank bank;// the bank that connects this ATM
    private Customer loggedInCustomer;// the customer who is logged in to this atm

    public ATM(Bank bank) {
        this.bank = bank;

        startLogin();
    }

    private void startLogin() {
        new LoginScreen(this);
    }

    //create a new customer
    private boolean signUp(String name, String userName, String password) {

        Customer customer = bank.createCustomer(name, userName, password);
        if (customer != null) {
            loggedInCustomer = customer;
            return true;
        } else {

            //TODO - fail to create customer
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
//        Customer customer = bank.userAuth(userName, password);
//        if (customer != null) {
//            //TODO - valid log in
//
//        } else {
//            //TODO - fail log in
//
//        }

        new CustomerScreen(this);

        return true;
    }

    public void logout() {
        startLogin();
    }


    private boolean createCheckingAccount(String currency, BigDecimal startingBalance) {
        //TODO - database error checking
        return bank.createCheckingAccount(loggedInCustomer, currency, startingBalance);//will return boolean indicate success or not
    }

    private boolean createSavingsAccount(String currency, BigDecimal startingBalance) {
        //TODO - database error checking
        return bank.createSavingsAccount(loggedInCustomer, currency, startingBalance);//will return boolean indicate success or not
    }

    private boolean deposit(BankAccount ba, BigDecimal amount) {

        //TODO - database error checking

        //make sure deposit is positive number
        if (isPositive(amount)) {

            return bank.deposit(ba, amount);//will return boolean indicate success or not

        }

        return false;

    }

    private boolean withdraw(BankAccount ba, BigDecimal amount) {
        //TODO - database error checking

        //make sure withdraw is positive number
        if (isPositive(amount) && ba.hasEnoughBalance(amount)) {

            return bank.withdraw(ba, amount);//will return boolean indicate success or not
        }
        return false;


    }

    private boolean requestLoan(BigDecimal amount, String currency, String collateral) {

        //TODO - database error checking

        if (isPositive(amount)) {

            return bank.requestLoan(loggedInCustomer, amount, currency, collateral);//will return boolean indicate success or not

        }
        return false;
    }

    private boolean transferMoney(BankAccount fromBank, BankAccount toBank, BigDecimal amount) {

        //TODO - database error checking

        if (isPositive(amount)) {

            return bank.transferMoney(fromBank, toBank, amount);

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


    //starting point when using the ATM, which contains the flow
    public void useATM() {


        //login screen
        //customer screen
        //....

    }

}
