import java.math.BigDecimal;
import java.util.Date;

public class Bank {
    private String name;
    private DBManager database;

    public Bank(String name) {

        this.database = new DBManager();
        this.name = name;
    }

    //create and return the customer object
    public Customer createCustomer(String name, String username, String password) {


        //TODO - database will create and return the created customer. make sure username is unique?

        //if following stmt succeed, then return the created customer
        database.addUser(name, username, password, Role.CUSTOMER.toString());
        return null;

    }

    //authenticate username and password and return the customer if there is one
    public Customer userAuth(String username, String password) {

        //TODO - need to query the uid with the given username and password from db. username should be unique?
        int uid = 0;// dummy uid
        Person person = database.getPerson(uid);
        //TODO - check whether the person is customer or manager
        return null;

    }

    //create a checking account
    public boolean createCheckingAccount(Customer customer, String currency) {

        //TODO - check if new account is added successfully to db, then return true
        database.addAccount(customer.getUid(), AccountType.CHECKING.toString(), new BigDecimal(0), currency);

        return false;

    }

    //create a savings account
    public boolean createSavingsAccount(Customer customer, String currency) {

        //TODO - check if new account is added successfully to db, then return true
        database.addAccount(customer.getUid(), AccountType.SAVINGS.toString(), new BigDecimal(0), currency);

        return false;

    }

    public String getAccountType(BankAccount ba) {
        return ba.getType().toString();
    }

    //update interests for all loans and all bank account
    public void updateInterests() {

        //TODO - update all interests in db

        // database.updateInterests();

    }

    //deposit amount to a bank account
    protected boolean deposit(BankAccount ba, BigDecimal amount) {

        //TODO - update to given bank account with given amount in db

        //if success
        database.updateAmount(ba.getAccountID(), ba.getBalance().add(amount));
        //then
        ba.deposit(amount);

        return false;
    }

    //withdraw an amount from bank account
    protected boolean withdraw(BankAccount ba, BigDecimal amount) {

        if (ba.hasEnoughBalance(amount)){
            //TODO - update to given bank account with given amount in db

            //if success
            database.updateAmount(ba.getAccountID(), ba.getBalance().subtract(amount));
            //then
            ba.withdraw(amount);
            return true;

        }

        return false;
    }

    protected boolean requestLoan(Customer customer, BigDecimal amount, String currency, String collateral) {

        //TODO - add the loan to db and update the customer
        //database.addLoan(int userid, String type, BigDecimal amount, String currency, String collateral)

        return false;
    }

    public boolean transferMoney(BankAccount fromBank, BankAccount toBank, BigDecimal amount) {

        //TODO - db persistence


        return true;
    }


    public void checkCustomer(Customer customer) {

        //TODO -
    }

    public void getDailyReport() {

        //TODO -
    }


}
