package pmc.gui.components.categories;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import pmc.gui.common.MovieModel;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriesModel {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty("");

    private ObservableList<MovieModel> matchingMovieModel = FXCollections.observableArrayList();

    public CategoriesModel() {}

    public CategoriesModel(int id, String name, ObservableList<MovieModel> movieModels) {
        this.id.set(id);
        this.name.set(name);
        setMatchFromModels(movieModels);
    }

    private void setMatchFromModels(ObservableList<MovieModel> movieModels) {
        movieModels.addListener((ListChangeListener.Change<? extends MovieModel> change) -> {
            List<MovieModel> matchingMovies = movieModels.stream()
                    .filter(model -> model.genreObservableList().stream()
                            .anyMatch(genre -> genre.getTmdbId() == this.idProperty().get()))
                    .collect(Collectors.toList());

            matchingMovieModel.addAll(matchingMovies);
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
