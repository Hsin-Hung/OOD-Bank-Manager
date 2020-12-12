/**
 * BankManager.java - class that extends person, creates a bank manager.
 */
public class BankManager extends Person {
    public BankManager(int user_id, String name, String username, String password) {
        super(user_id, name, username, password, Role.MANAGER);
    }
}
