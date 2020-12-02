import java.sql.*;

public class DBManager {

    private Connection conn = null;

    public DBManager() {
        try {
            // db parameters
            String file = this.getClass().getResource("").getPath();
            file = file.substring(1,file.length()-4) + "src/db/";
            String url = "jdbc:sqlite://" + file + "atm.db";
            System.out.println("URL=" + url);
            // create a connection to the database

            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            //    createTables();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTables() {

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE \"USERS\" (\n" +
                    "\t\"ID\"\tINTEGER NOT NULL UNIQUE,\n" +
                    "\t\"NAME\"\tTEXT NOT NULL,\n" +
                    "\t\"USERNAME\"\tTEXT NOT NULL UNIQUE,\n" +
                    "\t\"PASSWORD\"\tTEXT NOT NULL,\n" +
                    "\t\"ROLE\"\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(\"ID\" AUTOINCREMENT)\n" +
                    ")";
            stmt.execute(sql);

            sql = "CREATE TABLE IF NOT EXISTS TRANSACTIONS();";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS STOCKS();";
            stmt.execute(sql);
            sql = "CREATE TABLE IF NOT EXISTS ACCOUNTS();";
            stmt.execute(sql);
            stmt.close();
            addDefaultManager();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void addDefaultManager() {

        String sql = "INSERT INTO USERS(NAME,USERNAME,PASSWORD,ROLE) VALUES (\"MANAGER\",\"admin\",\"admin\",\"MANAGER\");";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUser(String name, String user, String pass, String role) {
        String sql = "INSERT INTO USERS(NAME,USERNAME,PASSWORD,ROLE) VALUES (?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, user);
            stmt.setString(3, pass);
            stmt.setString(4,role);
            stmt.execute();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
