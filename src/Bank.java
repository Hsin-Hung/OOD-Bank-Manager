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
        boolean isValidUser = db.isDistinctUsername(username);
        if(!isValidUser) {
            return null;
        }
        db.addUser(name,username,password,Role.CUSTOMER);
        Customer c = (Customer) db.getPerson(username);
        return c;

    }

    //authenticate username and password and return the customer if there is one
    public Person userAuth(String username, String password) {

        Person p = db.isValidUserAuth(username,password);
        if(p == null) {
            return null;
        } else {
            Role r = p.getRole();
        }
        return p;

    }

    //create a checking account
    public boolean createCheckingAccount(Customer customer, String currency, BigDecimal amount) {

        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.CHECKING);
        if(!isValidAcc) {
            return false;
        }
        //create the new checking account
        CheckingAccount account = (CheckingAccount) db.addAccount(customer.getUid(),AccountType.CHECKING,amount, currency);
        customer.addBankAccount(account);

        return true;


    }

    //create a savings account
    public boolean createSavingsAccount(Customer customer, String currency, BigDecimal amount) {
        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.SAVINGS);
        if(!isValidAcc) {
            return false;
        }
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
