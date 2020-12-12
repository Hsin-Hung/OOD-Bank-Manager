import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * Bank.java - class that handles the backend which deals with all the logics and executes instructions that are sent from the ATM
 */
public class Bank {
    private final String name; // name of the bank
    private final DBManager db; //bank database
    private final HashMap<String, BankMainAccount> bankBalances;

    public Bank(String name) {
        this.db = new DBManager();
        this.name = name;
        this.bankBalances = new HashMap<>();
        List<BankMainAccount> accs = db.getAllBankMainAccounts();
        for (BankMainAccount acc : accs) {
            bankBalances.put(acc.getCurrency(), acc);
        }
    }

    public String getName() {
        return name;
    }

    /**
     * create a customer object if the given username is unique
     * and database added the new customer successfully
     *
     * @param name
     * @param username
     * @param password
     * @return the customer object
     */
    public Customer createCustomer(String name, String username, String password) {
        boolean isValidUser = db.isDistinctUsername(username);
        Customer c = null;
        if (!isValidUser) {
            System.out.println("not valid");
            return null;
        }
        if (db.addUser(name, username, password, Role.CUSTOMER)) {

            c = (Customer) db.getPerson(username);
            Transaction t = db.addTransaction(TransactionType.SIGNUP, c.getUid(), -1, null, null, -1, -1, null);
            if (t != null) c.addTransaction(t);
        }
        return c;
    }

    /**
     * authenticate given username and password
     *
     * @param username
     * @param password
     * @return the authenticated Person
     */
    public Person userAuth(String username, String password) {
        return db.isValidUserAuth(username, password);
    }

    /**
     * Function that reloads the accounts that a customer has.
     * @param customer
     */
    public void reloadCustomerAccount(Customer customer) {
        customer.reloadAccounts(db.getAllUserAccounts(customer.getUid()));
    }

    //create a checking account
    public boolean createCheckingAccount(Customer customer, String currency, BigDecimal amount) {
        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.CHECKING);
        if (!isValidAcc) {
            return false;
        }

        //create the new checking account
        CheckingAccount account = (CheckingAccount) db.addAccount(customer, AccountType.CHECKING, amount, currency);
        if (account != null) {
            customer.addBankAccount(account);
            Transaction t = db.addTransaction(TransactionType.OPENCHECKING, customer.getUid(), account.getAccountID(), amount, currency, -1, -1, null);
            if (t != null) customer.addTransaction(t);
            chargeFee(customer, account, Constants.openAccountFee);
            return true;
        }

        return false;
    }

    //create a savings account
    public boolean createSavingsAccount(Customer customer, String currency, BigDecimal amount) {
        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.SAVINGS);
        if (!isValidAcc) {
            return false;
        }
        SavingsAccount account = (SavingsAccount) db.addAccount(customer, AccountType.SAVINGS, amount, currency);

        if (account != null) {

            customer.addBankAccount(account);
            Transaction t = db.addTransaction(TransactionType.OPENSAVINGS, customer.getUid(), account.getAccountID(), amount, currency, -1, -1, null);
            if (t != null) customer.addTransaction(t);
            chargeFee(customer, account, Constants.openAccountFee);
            return true;
        }

        return false;
    }

    //create a securities account
    public boolean createSecuritiesAccount(Customer customer, String currency, BigDecimal amount) {

        boolean isValidAcc = db.isDistinctAccount(customer.getUid(), currency, AccountType.SECURITIES);
        if (!isValidAcc) {
            return false;
        }
        SavingsAccount saccount = customer.getSavingsAccount("USD");
        SecuritiesAccount account = (SecuritiesAccount) db.addAccount(customer, AccountType.SECURITIES, amount, currency);

        if (account != null) {

            Transaction transferT = db.addTransaction(TransactionType.TRANSFER, customer.getUid(), saccount.getAccountID(), amount,
                    "USD", customer.getUid(), account.getAccountID(), null);

            saccount.withdraw(amount);

            customer.addBankAccount(account);
            Transaction t = db.addTransaction(TransactionType.OPENSECURITIES, customer.getUid(), account.getAccountID(), amount, currency, -1, -1, null);
            if (t != null) customer.addTransaction(t);

            return true;
        }

        return false;
    }

    /**
     * Function that close the given bank account
     * @param c
     * @param bankAccount
     * @return true if able to close account, false if not able to.
     */
    public boolean closeAccount(Customer c, BankAccount bankAccount) {
        if (db.deleteAccount(c, bankAccount.getAccountID())) {
            chargeFee(c, bankAccount, Constants.closeAccountFee);
            Transaction t = db.addTransaction(TransactionType.CLOSE, c.getUid(), bankAccount.getAccountID(), bankAccount.getBalance(), bankAccount.getCurrency(), -1, -1, null);
            c.addTransaction(t);
            c.removeBankAccount(bankAccount);
            return true;
        }

        return false;
    }

    /**
     * Function that return account type of selected account
     * @param ba - BANKACCOUNT
     * @return String
     */
    public String getAccountType(BankAccount ba) {
        return ba.getType().toString();
    }

    /**
     * Function to update interests for all loans and all bank account
     */
    public void updateInterests() {
        List<SavingsAccount> savingsList = db.getHighSavingAccounts();
        for (SavingsAccount sa : savingsList) {
            Customer c = (Customer) db.getPersonFromAccount(sa.getAccountID());
            applySavingsInterest(c, sa, Constants.savingsInterestPercentage);
        }

        List<Loan> loans = db.getAllLoans();
        for (Loan l : loans) {
            Customer c = (Customer) db.getPersonFromLoan(l.getLid());
            applyLoanInterest(c, l, Constants.savingsInterestPercentage);
        }
    }

    //deposit amount to a bank account
    protected boolean deposit(Customer c, BankAccount ba, BigDecimal amount) {
        if (db.updateAmount(ba.getAccountID(), ba.getBalance().add(amount))) {
            Transaction t = db.addTransaction(TransactionType.DEPOSIT, ba.getUSER_ID(), ba.getAccountID(),
                    amount, ba.getCurrency(), -1, -1, null);
            c.addTransaction(t);
            ba.deposit(amount);
            if (ba.getType().equals(AccountType.CHECKING)) {
                chargeFee(c, ba, Constants.checkingFee);
            }
            return true;
        }

        return false;
    }

    //withdraw an amount from bank account
    protected boolean withdraw(Customer c, BankAccount ba, BigDecimal amount) {
        if (db.updateAmount(ba.getAccountID(), ba.getBalance().subtract(amount))) {
            Transaction t = db.addTransaction(TransactionType.WITHDRAW, ba.getUSER_ID(), ba.getAccountID(),
                    amount, ba.getCurrency(), -1, -1, null);
            if (t != null) c.addTransaction(t);
            ba.withdraw(amount);
            chargeFee(c, ba, Constants.withdrawFee);
            return true;
        }
        return false;
    }

    protected boolean requestLoan(Customer customer, BigDecimal amount, String currency, String collateral) {
        Loan loan = db.addLoan(customer, amount, currency, collateral);
        if (loan != null) {
            customer.addLoan(loan);
            Transaction t = db.addTransaction(TransactionType.OPENLOAN, customer.getUid(), -1,
                    amount, currency, -1, -1, collateral);
            if (t != null) customer.addTransaction(t);
            minusBankBalance(loan.getCurrency(), amount);
            return true;
        }

        return false;
    }

    protected boolean payOffLoan(Customer customer, Loan loan, BigDecimal amount) {
        if (amount.compareTo(loan.getAmount()) == 0) {
            customer.removeLoan(loan);
            db.removeLoan(loan.getLid());
            loan.setAmount(new BigDecimal(0));
        } else {
            BigDecimal amt = loan.getAmount().subtract(amount);
            db.updateLoanAmount(loan.getLid(), amt);

            loan.setAmount(amt);
        }

        Transaction t = db.addTransaction(TransactionType.PAYLOAN,
                customer.getUid(),
                loan.getLid(), amount, loan.getCurrency(), -1, -1, loan.getCollateral());
        customer.addTransaction(t);
        addBankBalance(loan.getCurrency(), amount);
        return true;
    }

    // transfer money from one bank account to another of the same currency
    public boolean transferMoney(Customer c, BankAccount fromBank, int toAccountID, BigDecimal amount) {
        BankAccount toBank = getBankAccount(c, toAccountID);

        if (toBank == null || !toBank.getCurrency().equals(fromBank.getCurrency())) return false;

        BigDecimal fromBankBalance = fromBank.getBalance().subtract(amount), toBankBalance = toBank.getBalance().add(amount);
        if (db.transferMoney(fromBank.getAccountID(), toAccountID, fromBankBalance, toBankBalance)) {
            Transaction t = db.addTransaction(TransactionType.TRANSFER, fromBank.getUSER_ID(), fromBank.getAccountID(), amount,
                    fromBank.getCurrency(), toBank.getUSER_ID(), toBank.getAccountID(), null);

            if (t != null) c.addTransaction(t);

            fromBank.setBalance(fromBankBalance);
            toBank.setBalance(toBankBalance);
            if (fromBank.getType().equals(AccountType.CHECKING)) {
                chargeFee(c, fromBank, Constants.checkingFee);
            }
            if (toBank.getType().equals(AccountType.CHECKING)) {
                chargeFee(c, toBank, Constants.checkingFee);
            }
            return true;
        }

        return false;
    }

    //first try to get this account from this customer, if he doesnt have this account, then get it from db
    public BankAccount getBankAccount(Customer c, int accountID) {
        BankAccount bankAccount = c.getBankAccount(accountID);
        if (bankAccount == null) {
            bankAccount = db.getAccount(accountID);
        }

        return bankAccount;
    }

    public boolean buyStocks(Customer customer, String symbol, int shares) {
        SecuritiesAccount securitiesAccount = customer.getSecuritiesAccount();
        Stock stock = StockMarket.getStock(symbol);

        if (stock == null || securitiesAccount == null) return false;

        if (securitiesAccount.buyStocks(symbol, shares, db)) {
            return withdraw(customer, securitiesAccount, stock.getMarketVal().multiply(new BigDecimal(shares)));
        }

        return false;
    }

    public boolean sellStocks(Customer customer, String symbol, int shares) {
        SecuritiesAccount securitiesAccount = customer.getSecuritiesAccount();
        Stock stock = StockMarket.getStock(symbol);
        if (securitiesAccount == null) return false;

        if (securitiesAccount.sellStocks(symbol, shares, db)) {
            return deposit(customer, securitiesAccount, stock.getMarketVal().multiply(new BigDecimal(shares)));
        }

        return false;
    }

    /**
     * @return return a list of all customers
     */
    public List<Customer> checkCustomer() {
        return db.getAllCustomers();
    }

    public List<Transaction> getDailyReportWithin24hrs() {
        return db.get24hrTransactions();
    }

    //Function to charge an amount to the bank account
    public void chargeFee(Customer c, BankAccount account, BigDecimal amount) {

        if (db.updateAmount(account.getAccountID(), account.getBalance().subtract(amount))) {
            account.setBalance(account.getBalance().subtract(amount));
        }
        c.addTransaction(db.addTransaction(TransactionType.CHARGEFEE, c.getUid(), account.getAccountID(), amount, account.getCurrency(), -1, -1, null));
        addBankBalance(account.getCurrency(), amount);
    }

    //Function to apply interest on a loan
    public void applyLoanInterest(Customer c, Loan loan, BigDecimal percentage) {
        BigDecimal amt = loan.getAmount().multiply(percentage);
        if (db.updateLoanAmount(loan.getLid(), loan.getAmount().add(amt))) {

            loan.setAmount(loan.getAmount().add(amt));
            Transaction t = db.addTransaction(TransactionType.ADDLOANINTEREST, loan.getUid(), loan.getLid(), amt, loan.getCurrency(), -1, -1, loan.getCollateral());
            c.addTransaction(t);
        }
    }

    //Function to apply interest on a savings account
    public void applySavingsInterest(Customer c, SavingsAccount account, BigDecimal percentage) {
        BigDecimal amt = account.getBalance().multiply(percentage);

        if (db.updateAmount(account.getAccountID(), account.getBalance().add(amt))) {

            account.setBalance(account.getBalance().add(amt));
            Transaction t = db.addTransaction(TransactionType.PAYSAVINGSINTEREST, account.getUSER_ID(), account.getAccountID(), amt, account.getCurrency(), -1, -1, null);
            c.addTransaction(t);
            minusBankBalance(account.getCurrency(), amt);
        }
    }

    public HashMap<String, BankMainAccount> getBankBalances() {
        return bankBalances;
    }

    public void addBankBalance(String c, BigDecimal amount) {
        if (bankBalances.containsKey(c)) {
            bankBalances.get(c).setBalance(bankBalances.get(c).getBalance().add(amount));
            db.updateBankMainAccount(bankBalances.get(c).getBalance(), c);
        } else {
            BankMainAccount acc = db.addBankMainAccount(amount, c);
            bankBalances.put(c, acc);
        }
    }

    public void minusBankBalance(String c, BigDecimal amount) {
        if (bankBalances.containsKey(c)) {
            bankBalances.get(c).setBalance(bankBalances.get(c).getBalance().subtract(amount));
            db.updateBankMainAccount(bankBalances.get(c).getBalance(), c);
        } else {
            BankMainAccount acc = db.addBankMainAccount(new BigDecimal(0).subtract(amount), c);
            bankBalances.put(c, acc);
        }
    }
}
