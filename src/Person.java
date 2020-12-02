
public abstract class Person {

	String role;
	String user_id;
	String username;
	String password;

	public Person(String username, String user_id, String password){

		this.user_id = user_id;
	    this.username = username;
	    this.password = password;

    }

    public abstract String getRole();

}
