package pmc.gui.common;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MovieModel {
    private final StringProperty posterPath = new SimpleStringProperty("");

    public MovieModel(String posterPath) {
        this.posterPath.set(posterPath);
    }

    public StringProperty posterPathProperty() {
        return posterPath;
    }
}
