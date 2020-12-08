import java.util.*;

public class Customer extends Person {
    private List<Loan> loans;
    private List<BankAccount> bankAccounts;
    private List<Transaction> transactions;


    public Customer( int user_id, String name, String username, String password) {
        super( user_id, name, username,password, Role.CUSTOMER);
        bankAccounts = new ArrayList<>();
        loans = new ArrayList<>();
        transactions = new ArrayList<>();
    }


    public Customer( int user_id, String name, String username, String password, List<Loan> loans,
                     List<BankAccount> bankAccounts, List<Transaction> transactions ) {
        super( user_id, name, username,password, Role.CUSTOMER);
        this.bankAccounts = bankAccounts;
        this.loans = loans;
        this.transactions = transactions;
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
    }

    public void addTransaction(Transaction transaction){ transactions.add(transaction); }

    public void addBankAccount(BankAccount ba) {
        bankAccounts.add(ba);
    }

    public void removeBankAccount(BankAccount ba) { bankAccounts.remove(ba); }

    public SavingsAccount getSavingsAccount() {

        for(BankAccount ba : bankAccounts){

            if(ba.getType() == AccountType.SAVINGS){

                return (SavingsAccount) ba;

            }

        }

        return null;

    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
