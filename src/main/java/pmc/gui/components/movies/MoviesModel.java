package pmc.gui.components.movies;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.gui.common.MovieModel;

public class MoviesModel {

    private final StringProperty title = new SimpleStringProperty("");
    private ObservableList<MovieModel> movieModels = FXCollections.observableArrayList();

    public StringProperty titleProperty() {
        return title;
    }

    public ObservableList<MovieModel> moviesProperty() {
        return movieModels;
    }

}
