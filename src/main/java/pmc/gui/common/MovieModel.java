package pmc.gui.common;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MovieModel {
    private final StringProperty posterPath = new SimpleStringProperty("");
    private final StringProperty filePath = new SimpleStringProperty("");

    public MovieModel(String posterPath, String filePath) {
        this.posterPath.set("data/posters/" + posterPath);
        this.filePath.set("data/movies/" + filePath);
    }

    public StringProperty posterPathProperty() {
        return posterPath;
    }

    public StringProperty filePathProperty() {
        return filePath;
    }
}
