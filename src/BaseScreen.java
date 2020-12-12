import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * The base screen for all custom screens.
 */
public abstract class BaseScreen extends JFrame {
    private static int openWindows = 0;
    protected final ATM atm;

    public BaseScreen(ATM atm) {
        super("ATM");
        this.atm = atm;
        atm.newScreen(this);

        openWindows += 1;

        BaseScreen s = this;
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                atm.closeScreen(s);
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
