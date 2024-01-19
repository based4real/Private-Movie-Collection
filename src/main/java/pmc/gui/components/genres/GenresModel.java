package pmc.gui.components.genres;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
        // samme løsning som i GenresModel

        movieModels.addListener((ListChangeListener.Change<? extends MovieModel> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .filter(movieModel -> movieModel.genreObservableList().stream()
                                    .anyMatch(genre -> genre.getTmdbId() == this.idProperty().get()))
                            .forEach(matchingMovieModel::add);
                }
                if (change.wasRemoved()) {
                    change.getRemoved().stream()
                            .filter(movieModel -> movieModel.genreObservableList().stream()
                                    .noneMatch(genre -> genre.getTmdbId() == this.idProperty().get()))
                            .forEach(matchingMovieModel::remove);
                }
            }
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
