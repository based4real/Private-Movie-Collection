package pmc.gui.components.info;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InfoModel {
    private final StringProperty posterPath = new SimpleStringProperty("");

    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty director = new SimpleStringProperty("");
    private final StringProperty release = new SimpleStringProperty("");
    private final StringProperty runtime = new SimpleStringProperty("");
    private final StringProperty rated = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");

    public StringProperty posterPathProperty() {
        return posterPath;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty directorProperty() {
        return director;
    }

    public StringProperty releaseProperty() {
        return release;
    }

    public StringProperty runtimeProperty() {
        return runtime;
    }

    public StringProperty ratedProperty() {
        return rated;
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}
