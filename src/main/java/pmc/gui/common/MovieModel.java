package pmc.gui.common;

import javafx.beans.property.*;

public class MovieModel {
    private final IntegerProperty tmdbId = new SimpleIntegerProperty();
    private final StringProperty posterPath = new SimpleStringProperty("");
    private final StringProperty filePath = new SimpleStringProperty("");

    public MovieModel() {}

    public MovieModel(int tmdbid,
                      String posterPath,
                      String filePath) {
        this.tmdbId.set(tmdbid);
        this.posterPath.set("data/posters/" + posterPath);
        this.filePath.set("data/movies/" + filePath);
    }

    public IntegerProperty tmdbIdProperty() { return tmdbId; }

    public StringProperty posterPathProperty() {
        return posterPath;
    }

    public StringProperty filePathProperty() {
        return filePath;
    }
}
