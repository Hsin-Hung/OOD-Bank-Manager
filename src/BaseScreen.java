import javax.swing.*;
import java.awt.event.WindowEvent;

public abstract class BaseScreen extends JFrame {
    private static int openWindows = 0;
    protected boolean switchWindow = false;

    public BaseScreen() {
        super("ATM");
        openWindows += 1;

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                openWindows -= 1;
                if (openWindows == 0) {
                    System.exit(0);
                }
            }
        });
    }

    protected void centerScreen() {
        setLocationRelativeTo(null);
        setVisible(true);
    }

    protected void closeScreen() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
