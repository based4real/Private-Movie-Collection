package pmc.gui.components.info;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InfoModel {
    private final StringProperty posterPath = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    public StringProperty posterPathProperty() {
        return posterPath;
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
