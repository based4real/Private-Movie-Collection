package pmc.gui.common;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.be.Genre;
import pmc.be.rest.tmdb.TMDBGenreEntity;

import java.util.List;

public class MovieModel {
    private final IntegerProperty tmdbId = new SimpleIntegerProperty();
    private final StringProperty posterPath = new SimpleStringProperty("");
    private final StringProperty filePath = new SimpleStringProperty("");

    private ObservableList<Genre> genres = FXCollections.observableArrayList();

    public MovieModel() {}

    public MovieModel(int tmdbid,
                      String posterPath,
                      String filePath,
                      List<Genre> genres) {
        this.tmdbId.set(tmdbid);
        this.posterPath.set("data/posters/" + posterPath);
        this.filePath.set("data/movies/" + filePath);
        this.genres.addAll(genres);
    }

    public IntegerProperty tmdbIdProperty() { return tmdbId; }

    public StringProperty posterPathProperty() {
        return posterPath;
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public ObservableList<Genre> genreObservableList() {
        return genres;
    }
}
