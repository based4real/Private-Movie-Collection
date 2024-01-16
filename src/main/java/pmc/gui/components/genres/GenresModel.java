package pmc.gui.components.genres;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.gui.common.MovieModel;

public class GenresModel {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty("");

    private ObservableList<MovieModel> matchingMovieModel = FXCollections.observableArrayList();

    public GenresModel() {}

    public GenresModel(int id, String name, ObservableList<MovieModel> movieModels) {
        this.id.set(id);
        this.name.set(name);
        setMatchFromModels(movieModels);
    }

    public GenresModel(TMDBGenreEntity genre, ObservableList<MovieModel> movieModels) {
        this.id.set(genre.getID());
        this.name.set(genre.getName());
        setMatchFromModels(movieModels);
    }

    private void setMatchFromModels(ObservableList<MovieModel> movieModels) {
        movieModels.addListener((ListChangeListener.Change<? extends MovieModel> change) -> {
            FilteredList<MovieModel> filteredModels = new FilteredList<>(movieModels, movieModel ->
                    movieModel.genreObservableList().stream().anyMatch(genre -> genre.getTmdbId() == this.idProperty().get())
            );

            matchingMovieModel.setAll(filteredModels);
       });
    }

    public ObservableList<MovieModel> getMovies() {
        return matchingMovieModel;
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

}
