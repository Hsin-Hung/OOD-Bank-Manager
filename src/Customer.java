import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
    private List<Loan> loans;
    private List<BankAccount> bankAccounts;

    public List<BankAccount> getBankAccounts() { return bankAccounts; }
    public List<Loan> getLoans() { return loans; }

    public Customer(String username, String user_id, String password) {
        super(username, user_id, password);
        this.role = "customer";

        bankAccounts = new ArrayList<>();
        loans = new ArrayList<>();
    }

    @Override
    public String getRole() {
        return role;
    }

}
