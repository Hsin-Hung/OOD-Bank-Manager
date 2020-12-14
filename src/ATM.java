import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ATM.java - class that implements all the logics are done in the bank
 * ATM is just a facade between the bank and the users
 */
public class ATM {
    private final List<BaseScreen> screens;
    private final Bank bank;// the bank that connects this ATM
    private CustomerScreen customerScreen;
    private Person loggedInPerson; // the user who is logged in to this ATM

    public ATM(Bank bank) {
        this.bank = bank;
        screens = new ArrayList<>();
        startLogin();
    }

    /**
     * Function that returns the current customer that is logged in.
     * @return Customer that is logged in.
     */
    public Customer getLoggedInCustomer() {
        return (Customer) loggedInPerson;
    }

    /**
     * Function that calls and create login screen for users.
     */
    private void startLogin() {
        new LoginScreen(this);
    }

    /**
     * Sign a new user up after checking whether the username is unique or not
     *
     * @param name
     * @param userName
     * @param password
     * @return true if sign up is successful
     */
    public boolean signUp(String name, String userName, String password) {

        Customer customer = bank.createCustomer(name, userName, password);
        if (customer != null) {
            loggedInPerson = customer;
            customerScreen = new CustomerScreen(this);
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

        Person person = bank.userAuth(userName, password);
        if (person == null) {
            return false;
        } else {
            loggedInPerson = person;
            switch (person.getRole()) {
                case CUSTOMER:
                    customerScreen = new CustomerScreen(this);
                    return true;
                case MANAGER:
                    new ManagerScreen(this);
                    return true;
                default:
                    throw new IllegalArgumentException("Person has a non existent role: " + person.getRole());
            }
        }
    }

    public void logout() {
        startLogin();
    }

    public boolean closeAccount(Customer c, BankAccount bankAccount) {
        return bank.closeAccount(c, bankAccount);
    }

    public boolean createCheckingAccount(String currency, BigDecimal startingBalance) {
        if (startingBalance.compareTo(Constants.minAccountOpeningBalance) < 0) {
            return false;
        }
        return bank.createCheckingAccount(getLoggedInCustomer(), currency, startingBalance);//will return boolean indicate success or not
    }

    public boolean createSavingsAccount(String currency, BigDecimal startingBalance) {
        if (startingBalance.compareTo(Constants.minAccountOpeningBalance) < 0) {
            return false;
        }
        return bank.createSavingsAccount(getLoggedInCustomer(), currency, startingBalance);//will return boolean indicate success or not
    }

    public boolean createSecuritiesAccount(BigDecimal startingBalance) {
        SavingsAccount savingsAccount = getLoggedInCustomer().getSavingsAccount("USD");

        //check if saving account is >= 5000, if starting balance of securities account is >= 1000, if saving account can maintain 2500
        if ((getLoggedInCustomer().getSecuritiesAccount() == null) && startingBalance.compareTo(new BigDecimal(1000)) >= 0
                && (savingsAccount.getBalance().subtract(startingBalance).compareTo(new BigDecimal(2500)) >= 0)) {

            getLoggedInCustomer().getSavingsAccount("USD").setAsSecurityBackingAccount();
            return bank.createSecuritiesAccount(getLoggedInCustomer(), "USD", startingBalance);//will return boolean indicate success or not
        }

        return false;
    }

    public boolean buyStock(String symbol, int shares) {
        if (shares >= 1) return bank.buyStocks(getLoggedInCustomer(), symbol, shares);
        return false;
    }

    /**
     * Function to refresh the current screen.
     * @param screen
     */
    public void newScreen(BaseScreen screen) {
        if (screens.size() > 0) {
            screens.get(screens.size() - 1).setVisible(false);
            screens.get(screens.size() - 1).revalidate();
            screens.get(screens.size() - 1).repaint();
        }
        screens.add(screen);
    }

    public void closeScreen(BaseScreen screen) {
        screens.remove(screen);
        if (screens.size() > 0) {
            screens.get(screens.size() - 1).setVisible(true);
            screens.get(screens.size() - 1).revalidate();
            screens.get(screens.size() - 1).repaint();
        }
    }

    public boolean sellStock(String symbol, int shares) {
        if (shares >= 1) {
            return bank.sellStocks(getLoggedInCustomer(), symbol, shares);
        }
        return false;
    }

    /**
     * Check if customer qualifies for securities (has $5000 in checkings)
     *
     * @return true if qualifies
     */
    public boolean isQualifiedForSecuritiesAccount() {
        if (getLoggedInCustomer().getSecuritiesAccount() != null) return true;
        SavingsAccount savingsAccount = getLoggedInCustomer().getSavingsAccount("USD");
        return (savingsAccount != null) && savingsAccount.hasEnoughBalance(Constants.vipThreshold);
    }

    public boolean deposit(Customer c, BankAccount ba, BigDecimal amount) {
        //make sure deposit is positive number
        if (ba.getType() == AccountType.CHECKING) {
            if (amount.compareTo(new BigDecimal(5)) == -1) {
                return false;
            }
        }
        if (isPositive(amount)) {
            return bank.deposit(c, ba, amount);//will return boolean indicate success or not
        }

        return false;
    }

    public boolean withdraw(Customer c, BankAccount ba, BigDecimal amount) {
        //make sure withdraw is positive number
        if (isPositive(amount) && ba.hasEnoughBalance(amount.add(new BigDecimal(5.0)))) {
            return bank.withdraw(c, ba, amount); //will return boolean indicate success or not
        }
        return false;
    }

    public void updateInterest() {
        bank.updateInterests();
    }

    public boolean requestLoan(BigDecimal amount, String currency, String collateral) {
        if (isPositive(amount)) {
            return bank.requestLoan(getLoggedInCustomer(), amount, currency, collateral);//will return boolean indicate success or not
        }
        return false;
    }

    public boolean payOffLoan(Loan loan, BigDecimal amount) {
        if (isPositive(amount)) {
            return bank.payOffLoan(getLoggedInCustomer(), loan, amount);
        } else {
            return false;
        }
    }

    /**
     * transfer money between two bank accounts of the same currency
     *
     * @param fromBank    transfer money from this bank
     * @param toAccountID transfer money to the bank with this account ID
     * @param amount      amount to transfer
     * @return true if transfer succeeded, false otherwise
     */
    public boolean transferMoney(BankAccount fromBank, int toAccountID, BigDecimal amount) {
        BigDecimal amountChecker = new BigDecimal(0);
        amountChecker = amountChecker.add(amount);

        if (fromBank.getType() == AccountType.CHECKING) {
            amountChecker = amountChecker.add(new BigDecimal(5));
        }
        if (isPositive(amount) && fromBank.hasEnoughBalance(amountChecker) && fromBank.getAccountID() != toAccountID) {
            boolean res = bank.transferMoney(getLoggedInCustomer(), fromBank, toAccountID, amount);
            if (res) {
                bank.reloadCustomerAccount(getLoggedInCustomer());
                screens.get(screens.size() - 1).closeScreen();
                customerScreen.createScreen();
                return true;
            }
        }
        return false;
    }

    private boolean isPositive(BigDecimal val) {
        return val.compareTo(new BigDecimal(0)) == 1;
    }

    public List<Customer> checkCustomer() {
        return bank.checkCustomer();
    }

    public List<Transaction> getDailyReportWithin24hrs() {
        return bank.getDailyReportWithin24hrs();
    }

    public HashMap<String, BankMainAccount> getBankBalances() {
        return bank.getBankBalances();
    }
}
