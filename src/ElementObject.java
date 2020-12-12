/**
 * An abstract class that implements a IUIElement for other objects to inherit
 */
public abstract class ElementObject implements IUIElement {
    protected ElementsScreen owner;

    @Override
    public void setOwner(ElementsScreen e) {
        owner = e;
    }
}
