package pmc.gui.components.categories;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import pmc.be.Category;
import pmc.gui.common.MovieModel;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriesModel {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty("");

    private ObservableList<MovieModel> matchingMovieModel = FXCollections.observableArrayList();

    public CategoriesModel() {}

    public CategoriesModel(Category category) {
        this.id.set(category.getId());
        this.name.set(category.getName());
    }

    public CategoriesModel(int id, String name, ObservableList<MovieModel> movieModels) {
        this.id.set(id);
        this.name.set(name);
        setMatchFromModels(movieModels);
    }

    public CategoriesModel(Category category, ObservableList<MovieModel> movieModels) {
        this.id.set(category.getId());
        this.name.set(category.getName());
        setMatchFromModels(movieModels);
    }

    private void setMatchFromModels(ObservableList<MovieModel> movieModels) {
        // håndter sådan at når man sletter en film fra en kategori
        // at den ikke tilføjer de ikke slettede film på som en eller anden form for duplikering

        movieModels.addListener((ListChangeListener.Change<? extends MovieModel> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .filter(movieModel -> movieModel.categoryObservableList().stream()
                                    .anyMatch(category -> category.getId() == this.idProperty().get()))
                            .forEach(matchingMovieModel::add);
                }
                if (change.wasRemoved()) {
                    change.getRemoved().stream()
                            .filter(movieModel -> movieModel.categoryObservableList().stream()
                                    .anyMatch(category -> category.getId() == this.idProperty().get()))
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
