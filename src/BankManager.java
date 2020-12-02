public class BankManager extends Person {
    public BankManager(String username, int user_id, String password) {
        super(username, user_id, password);
        role = "manager";
    }

    @Override
    public String getRole() {
        return role;
    }

}
