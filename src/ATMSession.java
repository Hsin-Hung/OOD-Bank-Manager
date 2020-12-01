import javax.swing.*;

public class ATMSession {
    JFrame mainWindow;
    public ATMSession() {

        startLogin();
    }

    private void startLogin() {
        mainWindow = new Login(this);
    }

    public void login(String userName, String password) {
        System.out.println("User name: " + userName + " Password: " + password);
        mainWindow = new UserScreen(this);
    }
}
