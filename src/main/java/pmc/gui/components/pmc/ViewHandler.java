package pmc.gui.components.pmc;

public class ViewHandler {
    private final PMCModel model;

    public ViewHandler(PMCModel model) {
        this.model = model;
    }

    public void changeView(ViewType newView) {
        model.previousViewProperty().set(model.activeViewProperty().get());
        model.activeViewProperty().set(newView);
    }

    public void previousView() {
        model.activeViewProperty().set(model.previousViewProperty().get());
    }
}