public class App {
    public static void main(String[] args) {
        DBManager db = new DBManager();
        Bank bank = new Bank("Bank of CPK");
        ATM atm = new ATM(bank);
    }
}
