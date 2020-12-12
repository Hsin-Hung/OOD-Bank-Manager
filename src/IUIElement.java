import javax.swing.*;

/**
 * An interface for elements to be displayed easily in the elements screen.
 */
public interface IUIElement {
    JComponent $$$getRootComponent$$$();

    void setOwner(ElementsScreen e);
}
