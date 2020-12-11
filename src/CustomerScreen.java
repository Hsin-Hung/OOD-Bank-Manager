import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/*
 * CustomerScreen.java - creates the customer screen gui and child screens
 */
public class CustomerScreen extends BaseScreen {
    public JPanel mainPanel;
    private JLabel userNameLabel;
    private JButton logoutBtn;
    private JButton checkBtn;
    private JButton loansBtn;
    private JButton stocksBtn;
    private JButton transactionBtn;
    private final ATM owner;
    private ScreenMode currentScreenMode;

    public CustomerScreen(ATM owner) {
        super(owner);
        currentScreenMode = ScreenMode.Customer;

        userNameLabel.setText(owner.getLoggedInCustomer().getName());
        setContentPane(mainPanel);
        setSize(700, 700);
        centerScreen();

        stocksBtn.setEnabled(owner.isQualifiedForSecuritiesAccount());

        this.owner = owner;

        logoutBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                owner.logout();
                closeScreen();
            }
        });
        checkBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                currentScreenMode = ScreenMode.Accounts;
                createScreen();
            }
        });
        loansBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                currentScreenMode = ScreenMode.Loans;
                createScreen();
            }
        });
        transactionBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                currentScreenMode = ScreenMode.Transactions;
                createScreen();
            }
        });

        stocksBtn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (owner.isQualifiedForSecuritiesAccount()) {
                    currentScreenMode = ScreenMode.Securities;
                    createScreen();
                }
            }
        });

        // it refreshes the view stock button when motion motion detected
        mainPanel.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                return;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                stocksBtn.setEnabled(owner.isQualifiedForSecuritiesAccount());
            }
        });

    }

    public void createScreen() {
        switch (currentScreenMode) {
            case Customer:
                break;
            case Transactions:
                createTransactionsScreen();
                break;
            case Loans:
                createLoanScreen();
                break;
            case Securities:
                new ViewStockScreen(owner);
                break;
            case Accounts:
                createAccountsScreen();
                break;
        }
    }
    /*
     * Function that loads the bank accounts gui
     */
    private void createAccountsScreen() {
        List<IUIElement> elements = new ArrayList<>();
        Customer customer = owner.getLoggedInCustomer();
        for (BankAccount bankAccount : customer.getBankAccounts()) {
            elements.add(new AccountsObject(owner, bankAccount));
        }
        new ElementsScreen(atm, elements, this::createNewAccount, "Create Account",
                (ElementsScreen s) -> new AccountsObject(owner, Helper.getLastItem(customer.getBankAccounts())));
    }

    /*
     * Function that loads the loan screen gui
     */
    private void createLoanScreen() {
        List<IUIElement> elements = new ArrayList<>();
        Customer customer = owner.getLoggedInCustomer();
        for (Loan loan : customer.getLoans()) {
            elements.add(new LoanObject(owner, loan));
        }

        new ElementsScreen(atm, elements, this::createNewLoan, "Request New Loan",
                (ElementsScreen s) -> new LoanObject(owner, Helper.getLastItem(customer.getLoans())));
    }

    /*
     * Function that loads the transaction screen gui
     */
    private void createTransactionsScreen() {
        List<IUIElement> elements = new ArrayList<>();
        for (Transaction transaction : owner.getLoggedInCustomer().getTransactions()) {
            elements.add(new TransactionObject(transaction));
        }
        new ElementsScreen(atm, elements, null, null, null);
    }

    /*
     * Function that loads the creating a new account gui
     */
    private Void createNewAccount(ElementsScreen s) {
        new NewAccountDialog(owner, s);
        return null;
    }

    /*
     * Function that loads the creating a new loan gui
     */
    private Void createNewLoan(ElementsScreen s) {
        new NewLoanScreen(owner, s);
        return null;
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 7, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        userNameLabel = new JLabel();
        userNameLabel.setAlignmentX(0.0f);
        userNameLabel.setText("Username");
        panel1.add(userNameLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logoutBtn = new JButton();
        logoutBtn.setText("Log out");
        panel1.add(logoutBtn, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(2, 5, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(2, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanel.add(spacer5, new GridConstraints(2, 2, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        checkBtn = new JButton();
        checkBtn.setHorizontalTextPosition(0);
        checkBtn.setText("View Accounts");
        panel2.add(checkBtn, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel2.add(spacer6, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        loansBtn = new JButton();
        loansBtn.setText("View Loans");
        panel2.add(loansBtn, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stocksBtn = new JButton();
        stocksBtn.setEnabled(false);
        stocksBtn.setText("View Stocks");
        panel2.add(stocksBtn, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        transactionBtn = new JButton();
        transactionBtn.setText("View Transactions");
        panel2.add(transactionBtn, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel3, new GridConstraints(1, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(10, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel4, new GridConstraints(1, 6, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(10, -1), null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel5, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 5), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }


    private enum ScreenMode {
        Customer,
        Transactions,
        Loans,
        Securities,
        Accounts,
    }
}
