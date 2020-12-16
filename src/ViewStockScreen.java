import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Screen to view a user's securities and enables stock purchase and sales.
 */
public class ViewStockScreen extends BaseScreen {
    private final String HINT_TEXT = "Enter a stock symbol";
    private JButton createSecuritiesAccountButton;
    private JPanel elementPanel;
    private JPanel mainPanel;
    private JLabel balance;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel searchPane;
    private final ATM owner;

    public ViewStockScreen(ATM owner) {
        super(owner);
        $$$setupUI$$$();
        initialize();
        elementPanel.setLayout(new BoxLayout(elementPanel, BoxLayout.Y_AXIS));
        searchPane.setLayout(new BoxLayout(searchPane, BoxLayout.Y_AXIS));
        this.owner = owner;

        update();

        createSecuritiesAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewSecuritiesAccount();
            }
        });

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (searchField.getText().trim().equals(HINT_TEXT)) {
                    searchField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (searchField.getText().trim().equals("")) {
                    searchField.setText(HINT_TEXT);
                } else {
                    update();
                }
            }
        });
    }

    public void createNewSecuritiesAccount() {
        new NewSecuritiesAccountDialog(owner, this);
    }

    private void updateBalance() {
        SecuritiesAccount securitiesAccount = owner.getLoggedInCustomer().getSecuritiesAccount();
        if (securitiesAccount != null) {
            balance.setText(Constants.CURRENCY_FORMAT.format(securitiesAccount.getBalance().doubleValue()));
        } else {
            balance.setText(Constants.CURRENCY_FORMAT.format(0));
        }

    }

    // update the stock display area base on whether the user has a securities account or not
    public void update() {
        updateBalance();
        SecuritiesAccount securitiesAccount = owner.getLoggedInCustomer().getSecuritiesAccount();
        if (securitiesAccount == null) {
            createSecuritiesAccountButton.setEnabled(true);
        } else {
            createSecuritiesAccountButton.setEnabled(false);
            balance.setText(securitiesAccount.getBalance().toPlainString());

            List<IUIElement> elements = new ArrayList<>();
            List<IUIElement> searchElements = new ArrayList<>();

            List<StockPosition> stockPositions = securitiesAccount.getStockPositions();

            //first add the stocks this user owns
            for (StockPosition sp : stockPositions) {
                elements.add(new StockObject(owner, this, sp));
            }

            //add the rest of the stocks
            if (!searchField.getText().trim().isEmpty()) {
                for (Stock stock : StockMarket.getStocks()) {
                    if (!securitiesAccount.hasStock(stock.getSymbol())) {
                        if (stock.getSymbol().toLowerCase().contains(searchField.getText().trim().toLowerCase()) || stock.getName().toLowerCase().contains(searchField.getText().trim().toLowerCase())) {
                            searchElements.add(new StockObject(owner, this, stock));
                            if (searchElements.size() > 20) {
                                break;
                            }
                        }
                    }
                }
            }

            refreshUIElements(elements, searchElements);
        }
    }

    public void refreshUIElements(List<IUIElement> elements, List<IUIElement> searchElements) {
        elementPanel.removeAll();

        for (IUIElement element : elements) {
            element.$$$getRootComponent$$$().setAlignmentX(Component.CENTER_ALIGNMENT);
            elementPanel.add(element.$$$getRootComponent$$$());
        }

        elementPanel.repaint();
        elementPanel.revalidate();

        searchPane.removeAll();
        for (IUIElement element : searchElements) {
            element.$$$getRootComponent$$$().setAlignmentX(Component.CENTER_ALIGNMENT);
            searchPane.add(element.$$$getRootComponent$$$());
        }

        searchPane.repaint();
        searchPane.revalidate();
    }

    private void initialize() {
        setContentPane($$$getRootComponent$$$());
        setSize(700, 700);
        centerScreen();
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
        mainPanel.setLayout(new GridLayoutManager(5, 4, new Insets(10, 10, 10, 10), -1, -1));
        createSecuritiesAccountButton = new JButton();
        createSecuritiesAccountButton.setText("Create Securities Account");
        mainPanel.add(createSecuritiesAccountButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        elementPanel = new JPanel();
        elementPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollPane1.setViewportView(elementPanel);
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Balance");
        mainPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        balance = new JLabel();
        balance.setText("-1");
        mainPanel.add(balance, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchField = new JTextField();
        searchField.setText("");
        mainPanel.add(searchField, new GridConstraints(2, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        mainPanel.add(searchButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        mainPanel.add(scrollPane2, new GridConstraints(3, 0, 2, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        searchPane = new JPanel();
        searchPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scrollPane2.setViewportView(searchPane);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
