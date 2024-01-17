package pmc.gui.components.movies;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.gui.common.MovieModel;

import java.util.function.Predicate;

public class MoviesModel {

    private final StringProperty title = new SimpleStringProperty("");
    private ObservableList<MovieModel> movieModels = FXCollections.observableArrayList();

    public StringProperty titleProperty() {
        return title;
    }

    public ObservableList<MovieModel> moviesProperty() {
        return movieModels;
    }

    public void setMovies(ObservableList<MovieModel> movieModels) {
        this.movieModels = movieModels;
    }

    public FilteredList<MovieModel> getFilteredMovies(Predicate<MovieModel> filter) {
        return new FilteredList<>(movieModels, filter);
    }
}
