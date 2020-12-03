import java.math.BigDecimal;
import java.util.Date;

public class Bank {
    private String name;
    private DBManager db;

    public Bank(String name) {

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
        return database.getCustomer(username, password);
    }

    //create a checking account
    public boolean createCheckingAccount(Customer customer, String currency, BigDecimal amount) {

        int account_ID = 0; //TODO - generate the unique account ID here

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

        database.updateInterests();

    }

    //deposit amount to a bank account
    //TODO - there might be a better implementation for database persistence
    protected boolean deposit(BankAccount ba, BigDecimal amount) {


        ba.deposit(amount);

        if (database.updateBankAccount(ba)) {
            return true;
        } else {

            //redo the deposit if the database failed to update
            ba.withdraw(amount);
            return false;
        }

    }

    //withdraw an amount from bank account
    //TODO - there might be a better implementation for database persistence
    protected boolean withdraw(BankAccount ba, BigDecimal amount) {

        ba.withdraw(amount);

        if (database.updateBankAccount(ba)) {
            return true;
        } else {

            //redo the withdraw if the database failed to update
            ba.deposit(amount);
            return false;
        }
    }

    protected boolean requestLoan(Customer customer, BigDecimal amount, String currency, String collateral) {

        //TODO - create the lid
        int lid = 0;

        Loan newLoan = new Loan(lid, customer.getUid(), currency, amount, collateral);

        customer.addLoan(newLoan);

        if (!database.updateCustomer(customer)) {

            //remove the loan if database failed to update the new loan
            customer.removeLoan(newLoan);
            return false;
        }

        return true;


    }

    //TODO - maintain database persistence
    public boolean transferMoney(BankAccount fromBank, BankAccount toBank, BigDecimal amount) {


        withdraw(fromBank, amount);
        deposit(toBank, amount);

        //TODO - need to make sure database is persistent
        database.updateBankAccount(fromBank);
        database.updateBankAccount(toBank);


        return true;
    }


    public void checkCustomer(Customer customer) {

        //TODO -
    }

    public void getDailyReport() {

        //TODO -
    }


}
