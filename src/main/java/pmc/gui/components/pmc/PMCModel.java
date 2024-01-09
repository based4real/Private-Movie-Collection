package pmc.gui.components.pmc;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class PMCModel {
    private final BooleanProperty homeSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty categoriesSelected = new SimpleBooleanProperty(false);

    public BooleanProperty homeSelectedProperty() {
        return homeSelected;
    }

    public BooleanProperty categoriesSelectedProperty() {
        return categoriesSelected;
    }
}
