import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        db.addTransaction(TransactionType.SIGNUP,c.getUid(),-1,null,null,-1,-1);
        return c;

    }

    //authenticate username and password and return the customer if there is one
    public Person userAuth(String username, String password) {

        Person p = db.isValidUserAuth(username,password);
        if(p == null) {
            return null;
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
        chargeFee(account,Constants.openAccountFee);
        customer.addBankAccount(account);
        db.addTransaction(TransactionType.OPENCHECKING,customer.getUid(),account.getAccountID(),amount,currency,-1,-1);

        return true;


    }

    //create a savings account
    public boolean createSavingsAccount(Customer customer, String currency, BigDecimal amount) {
        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.SAVINGS);
        if(!isValidAcc) {
            return false;
        }
        SavingsAccount account = (SavingsAccount) db.addAccount(customer.getUid(),AccountType.SAVINGS,amount, currency);
        chargeFee(account,Constants.openAccountFee);
        customer.addBankAccount(account);
        db.addTransaction(TransactionType.OPENSAVINGS,customer.getUid(),account.getAccountID(),amount,currency,-1,-1);

        return true;

    }

    public String getAccountType(BankAccount ba) {
        return ba.getType().toString();
    }

    //update interests for all loans and all bank account
    public void updateInterests() {
        List<SavingsAccount> savingsList = db.getHighSavingAccounts();
        for(SavingsAccount sa: savingsList) {
            applySavingsInterest(sa,Constants.savingsInterestPercentage);
        }

        List<Loan> loans = db.getAllLoans();
        for(Loan l: loans) {
            applyLoanInterest(l,Constants.savingsInterestPercentage);
        }


    }

    //deposit amount to a bank account
    protected boolean deposit(BankAccount ba, BigDecimal amount) {

        if(db.updateAmount(ba.getAccountID(), ba.getBalance().add(amount))){

            ba.deposit(amount);
            return true;

        }

        return false;
    }

    //withdraw an amount from bank account
    protected boolean withdraw(BankAccount ba, BigDecimal amount) {

        if(db.updateAmount(ba.getAccountID(), ba.getBalance().subtract(amount))){

            ba.withdraw(amount);
            return true;

        }
        return false;
    }

    protected boolean requestLoan(Customer customer, BigDecimal amount, String currency, String collateral) {

        Loan loan = db.addLoan(customer.getUid(), amount, currency, collateral);

        if (loan != null){

            customer.addLoan(loan);
            return true;

        }

        return false;
    }

    //Function to transfer money from one account to another
    public boolean transferMoney(BankAccount fromBank, BankAccount toBank, BigDecimal amount) {
        //if from bank account is less than amount, return false
        if(fromBank.getBalance().compareTo(amount) < 0) {
            return false;
        }
        fromBank.setBalance(fromBank.getBalance().subtract(amount));
        toBank.setBalance(toBank.getBalance().add(amount));
        db.transferMoney(fromBank.getAccountID(), toBank.getAccountID(), fromBank.getBalance(), toBank.getBalance() );
        return true;
    }

    public void checkCustomer(Customer customer) {

        //TODO -
    }

    public void getDailyReport() {

        //TODO -
    }

    //Function to charge an amount to the bank account
    public void chargeFee(BankAccount account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
        db.updateAmount(account.getAccountID(),account.getBalance());
    }

    //Function to apply interest on a loan
    public void applyLoanInterest(Loan loan, BigDecimal percentage) {
        BigDecimal perc = new BigDecimal("1" ).add(percentage);
        loan.setAmount(loan.getAmount().multiply(perc));
        db.updateLoanAmount(loan.getLid(),loan.getAmount());
    }


    //Function to apply interest on a savings account
    public void applySavingsInterest(SavingsAccount account, BigDecimal percentage) {
        BigDecimal perc = new BigDecimal("1" ).add(percentage);
        account.setBalance(account.getBalance().multiply(perc));
        db.updateAmount(account.getAccountID(),account.getBalance());
    }
}
