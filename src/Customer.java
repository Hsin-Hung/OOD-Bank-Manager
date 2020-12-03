import java.util.*;

public class Customer extends Person {
    private List<Loan> loans;
    private List<BankAccount> bankAccounts;



    public Customer( int user_id, String name, String username, String password) {
        super( user_id, name, username,password, Role.CUSTOMER);
        bankAccounts = new ArrayList<>();
        loans = new ArrayList<>();
    }


    public Customer( int user_id, String name, String username, String password, List<Loan> loans,List<BankAccount> bankAccounts ) {
        super( user_id, name, username,password, Role.CUSTOMER);
        this.bankAccounts = bankAccounts;
        this.loans = loans;
    }

    public void addLoan(Loan loan) {
        loans.add(loan);
    }

    public void removeLoan(Loan loan) {
        loans.remove(loan);
    }

    public void addBankAccount(BankAccount ba) {
        bankAccounts.add(ba);
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public List<Loan> getLoans() {
        return loans;
    }
}
