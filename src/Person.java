public abstract class Person {
    private Role role;
    private int user_id;
    private String name;
    private String username;
    private String password;

    public Person( int user_id,String name,String username, String password, Role role) {
        this.user_id = user_id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() { return name; }
}
