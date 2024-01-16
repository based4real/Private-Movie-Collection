package pmc.gui.components.info;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.be.rest.tmdb.TMDBCreditEntity;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.gui.common.MovieModel;

import java.util.function.Consumer;

public class InfoModel {
    private final StringProperty posterPath = new SimpleStringProperty("");

    private final StringProperty title = new SimpleStringProperty("");
    private final StringProperty director = new SimpleStringProperty("");
    private final StringProperty release = new SimpleStringProperty("");
    private final StringProperty runtime = new SimpleStringProperty("");
    private final StringProperty rated = new SimpleStringProperty("");

    private final ObservableList<TMDBGenreEntity> genres = FXCollections.observableArrayList();
    private final ObservableList<TMDBCreditEntity> credits = FXCollections.observableArrayList();

    private final StringProperty description = new SimpleStringProperty("");

    private MovieModel movieModel;

    public void setMovieModel(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    public MovieModel getMovieModel() {
        return movieModel;
    }

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

    public ObservableList<TMDBGenreEntity> genresProperty() {
        return genres;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public ObservableList<TMDBCreditEntity> creditsProperty() {
        return credits;
    }

}
