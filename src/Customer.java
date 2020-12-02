import java.util.*;

public class Customer extends Person {
    private List<Loan> loans;
    private List<BankAccount> bankAccounts;

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public Customer(String username, int user_id, String password) {
        super(username, user_id, password);
        this.role = "customer";

        bankAccounts = new ArrayList<>();
        loans = new ArrayList<>();
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

    @Override
    public String getRole() {
        return role;
    }

}
