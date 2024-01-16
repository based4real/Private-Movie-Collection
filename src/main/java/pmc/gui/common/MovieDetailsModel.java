package pmc.gui.common;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.be.rest.tmdb.TMDBCreditEntity;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.dal.rest.tmdb.extra.TMDBLang;

import java.util.List;
import java.util.function.Consumer;

public class MovieDetailsModel {
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty director = new SimpleStringProperty();
    private final StringProperty release = new SimpleStringProperty();
    private final StringProperty runtime = new SimpleStringProperty();
    private final StringProperty rated = new SimpleStringProperty();

    private final ObservableList<TMDBGenreEntity> genres = FXCollections.observableArrayList();
    private final ObservableList<TMDBCreditEntity> credits = FXCollections.observableArrayList();

    private final StringProperty description = new SimpleStringProperty();

    public MovieDetailsModel(String title, String director, String release, String playTime, String rated, List<TMDBGenreEntity> genres, String description, List<TMDBCreditEntity> credits) {
        this.title.set(title);
        this.director.set(director);
        this.release.set(release);
        this.runtime.set(playTime);
        this.rated.set(rated);
        this.genres.setAll(genres);

        this.description.set(description);
        this.credits.setAll(credits);
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

    public ObservableList<TMDBGenreEntity> genresProperty() {
        return genres;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public ObservableList<TMDBCreditEntity> creditProperty() {
        return credits;
    }
}
