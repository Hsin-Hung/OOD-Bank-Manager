import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bank {
    private String name;
    private DBManager db;


    public Bank(String name) {
        this.db = new DBManager();
        // TODO eric remove
//        db.addTransaction(TransactionType.OPENSAVINGS, 2, 1, new BigDecimal(10000), "USD", -1, -1);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //create and return the customer object
    public Customer createCustomer(String name, String username, String password) {
        boolean isValidUser = db.isDistinctUsername(username);
        Customer c = null;
        if(!isValidUser) {
            System.out.println("not valid");
            return null;
        }
        if (db.addUser(name,username,password,Role.CUSTOMER)){

            c = (Customer) db.getPerson(username);
            Transaction t = db.addTransaction(TransactionType.SIGNUP,c.getUid(),-1,null,null,-1,-1, null);
            if(t != null)c.addTransaction(t);
        }
        return c;

    }

    //authenticate username and password and return the customer if there is one
    public Person userAuth(String username, String password) {

        return db.isValidUserAuth(username,password);

    }

    //create a checking account
    public boolean createCheckingAccount(Customer customer, String currency, BigDecimal amount) {

        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.CHECKING);
        if(!isValidAcc) {
            return false;
        }
        //create the new checking account
        CheckingAccount account = (CheckingAccount) db.addAccount(customer,AccountType.CHECKING,amount, currency);

        if (account != null){


            customer.addBankAccount(account);
            Transaction t = db.addTransaction(TransactionType.OPENCHECKING,customer.getUid(),account.getAccountID(),amount,currency,-1,-1,null);
            if(t != null)customer.addTransaction(t);
            chargeFee(customer, account,Constants.openAccountFee);
            return true;
        }

        return false;


    }

    //create a savings account
    public boolean createSavingsAccount(Customer customer, String currency, BigDecimal amount) {
        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.SAVINGS);
        if(!isValidAcc) {
            return false;
        }
        SavingsAccount account = (SavingsAccount) db.addAccount(customer,AccountType.SAVINGS,amount, currency);

        if(account != null){

            customer.addBankAccount(account);
            Transaction t = db.addTransaction(TransactionType.OPENSAVINGS,customer.getUid(),account.getAccountID(),amount,currency,-1,-1,null);
            if(t != null)customer.addTransaction(t);
            chargeFee(customer, account,Constants.openAccountFee);
            return true;
        }

        return false;

    }

    //create a security account
    public boolean createSecuritiesAccount(Customer customer, String currency, BigDecimal amount) {

        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.SECURITIES);
        if(!isValidAcc) {
            return false;
        }
        SecuritiesAccount account = (SecuritiesAccount) db.addAccount(customer,AccountType.SECURITIES,amount, currency);

        if(account != null){

            customer.addBankAccount(account);
            Transaction t = db.addTransaction(TransactionType.OPENSECURITIES,customer.getUid(),account.getAccountID(),amount,currency,-1,-1,null);
            if(t != null)customer.addTransaction(t);
            return true;
        }

        return false;

    }

    //close the given bank account
    public boolean closeAccount(Customer c, BankAccount bankAccount){


        if (db.deleteAccount(c, bankAccount.getAccountID())){

            chargeFee(c, bankAccount,Constants.closeAccountFee);
            c.removeBankAccount(bankAccount);
            Transaction t = db.addTransaction(TransactionType.CLOSE,c.getUid(),bankAccount.getAccountID(),null,null,-1,-1,null);
            if(t != null)c.addTransaction(t);

            return true;
        }

        return false;


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
    protected boolean deposit(Customer c, BankAccount ba, BigDecimal amount) {

        if(db.updateAmount(ba.getAccountID(), ba.getBalance().add(amount))){

            Transaction t = db.addTransaction(TransactionType.DEPOSIT, ba.getUSER_ID(), ba.getAccountID(),
                    amount, ba.getCurrency(), -1, -1, null);
            c.addTransaction(t);
            ba.deposit(amount);
            return true;

        }

        return false;
    }

    //withdraw an amount from bank account
    protected boolean withdraw(Customer c, BankAccount ba, BigDecimal amount) {

        if(db.updateAmount(ba.getAccountID(), ba.getBalance().subtract(amount))){

            Transaction t = db.addTransaction(TransactionType.WITHDRAW, ba.getUSER_ID(), ba.getAccountID(),
                    amount, ba.getCurrency(), -1, -1, null);
            if(t != null)c.addTransaction(t);
            ba.withdraw(amount);
            return true;

        }
        return false;
    }

    protected boolean requestLoan(Customer customer, BigDecimal amount, String currency, String collateral) {

        Loan loan = db.addLoan(customer, amount, currency, collateral);

        if (loan != null){

            customer.addLoan(loan);
            Transaction t = db.addTransaction(TransactionType.OPENLOAN, customer.getUid(), -1,
                    amount, currency, -1, -1, collateral);
            if(t != null)customer.addTransaction(t);
            return true;

        }

        return false;
    }

    //Function to transfer money from one account to another
    public boolean transferMoney(Customer c, BankAccount fromBank, BankAccount toBank, BigDecimal amount) {

        if (db.transferMoney(fromBank.getAccountID(), toBank.getAccountID(), fromBank.getBalance(), toBank.getBalance() )){

            Transaction t = db.addTransaction(TransactionType.TRANSFER,fromBank.getUSER_ID(),fromBank.getAccountID(),amount,
                    fromBank.getCurrency(),toBank.getUSER_ID(),toBank.getAccountID(),null);
            if(t != null)c.addTransaction(t);

            fromBank.setBalance(fromBank.getBalance().subtract(amount));
            toBank.setBalance(toBank.getBalance().add(amount));

            return true;
        }
        return false;
    }

    //get all the customers from the db
    public List<Customer> checkCustomer() {

            return db.getAllCustomers();

    }

    public List<Transaction> getDailyReportWithin24hrs(){

        List<Transaction> allTransactions = db.getAllTransaction();
        List<Transaction> within24Transactions = new ArrayList<>();

        long day = 24 * 60 * 60 * 1000;

        for (Transaction t : allTransactions) {

            Date tDate = t.getDate();
            if (tDate.getTime() > (System.currentTimeMillis() - day)) within24Transactions.add(t);

        }
        return within24Transactions;

    }


    //Function to charge an amount to the bank account
    public void chargeFee(Customer c, BankAccount account, BigDecimal amount) {

        if(db.updateAmount(account.getAccountID(),account.getBalance().subtract(amount))){
            account.setBalance(account.getBalance().subtract(amount));
        }
        c.addTransaction(db.addTransaction(TransactionType.CHARGEFEE,c.getUid(), account.getAccountID(), amount,account.getCurrency(),-1,-1,null));
    }

    //Function to apply interest on a loan
    public void applyLoanInterest(Loan loan, BigDecimal percentage) {
        BigDecimal perc = new BigDecimal("1" ).add(percentage);

        if(db.updateLoanAmount(loan.getLid(),loan.getAmount().multiply(perc))){

            loan.setAmount(loan.getAmount().multiply(perc));

        }
    }


    //Function to apply interest on a savings account
    public void applySavingsInterest(SavingsAccount account, BigDecimal percentage) {
        BigDecimal perc = new BigDecimal("1" ).add(percentage);

        if(db.updateAmount(account.getAccountID(),account.getBalance().multiply(perc))){

            account.setBalance(account.getBalance().multiply(perc));

        }
    }
}
