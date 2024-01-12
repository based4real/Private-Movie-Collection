package pmc.gui.common;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MovieDetailsModel {
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty backdropUrl = new SimpleStringProperty();

    public MovieDetailsModel(String description, String backdropUrl) {
        this.description.set(description);
        this.backdropUrl.set(backdropUrl);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty backdropUrlProperty() {
        return backdropUrl;
    }
}
