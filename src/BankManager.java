//this represents a bank manager, which is a person
public class BankManager extends Person {
    public BankManager(int user_id, String name, String username, String password) {
        super(user_id, name, username, password, Role.MANAGER);
    }
}
