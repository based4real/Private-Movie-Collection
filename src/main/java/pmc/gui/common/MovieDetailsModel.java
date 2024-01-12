package pmc.gui.common;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MovieDetailsModel {
    private final StringProperty description = new SimpleStringProperty();

    public MovieDetailsModel(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
