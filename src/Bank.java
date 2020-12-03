import java.math.BigDecimal;
import java.util.Date;

public class Bank {
    private String name;
    private DBManager db;


    public Bank(String name) {

        this.db = new DBManager();
        this.name = name;
    }

    //create and return the customer object
    public Customer createCustomer(String name, String username, String password) {
       db.addUser(name,username,password,Role.CUSTOMER);
       Customer c = (Customer) db.getPerson(username);
       return c;

    }

    //authenticate username and password and return the customer if there is one
    public Customer userAuth(String username, String password) {

        //TODO - need to query the uid with the given username and password from db. username should be unique?
//        int uid = 0;// dummy uid
//        Person person = db.getPerson(uid);
        //TODO - check whether the person is customer or manager
        return null;

    }

    //create a checking account
    public boolean createCheckingAccount(Customer customer, String currency, BigDecimal amount) {


        //create the new checking account
        CheckingAccount account = (CheckingAccount) db.addAccount(customer.getUid(),AccountType.CHECKING,amount, currency);

        customer.addBankAccount(account);

        return true;


    }

    //create a savings account
    public boolean createSavingsAccount(Customer customer, String currency, BigDecimal amount) {
        SavingsAccount account = (SavingsAccount) db.addAccount(customer.getUid(),AccountType.SAVINGS,amount, currency);
        customer.addBankAccount(account);
        return true;

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
        db.updateAmount(ba.getAccountID(), ba.getBalance().add(amount));
        //then
        ba.deposit(amount);

        return false;
    }

    //withdraw an amount from bank account
    protected boolean withdraw(BankAccount ba, BigDecimal amount) {

        if (ba.hasEnoughBalance(amount)){
            //TODO - update to given bank account with given amount in db

            //if success
            db.updateAmount(ba.getAccountID(), ba.getBalance().subtract(amount));
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
