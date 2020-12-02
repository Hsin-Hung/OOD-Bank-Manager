public abstract class Person {
    protected String role;
    private int user_id;
    private String username;
    private String password;

    public Person(String username, int user_id, String password) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
    }

    public int getUid(){

        return user_id;

    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public abstract String getRole();
}
