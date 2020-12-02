public abstract class Person {
    protected String role;
    protected String user_id;
    protected String username;
    protected String password;

    public Person(String username, String user_id, String password) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
    }

    public abstract String getRole();
}
