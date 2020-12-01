import javax.swing.*;

public abstract class MainWindow extends JFrame {
    protected boolean switchWindow = false;

    public MainWindow() {
        super("ATM");
        setLocationRelativeTo(null);
        setVisible(true);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (!switchWindow) {
                    System.exit(0);
                }
            }
        });
    }
}
