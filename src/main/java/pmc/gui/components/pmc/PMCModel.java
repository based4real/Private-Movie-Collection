package pmc.gui.components.pmc;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.gui.common.GenreModel;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesModel;

import java.util.Arrays;

public class PMCModel {
    private final ObjectProperty<ViewType> activeView = new SimpleObjectProperty<>(ViewType.HOME);
    private final ObjectProperty<ViewType> previousView = new SimpleObjectProperty<>(ViewType.HOME);
    private final BooleanProperty isDialogOpen = new SimpleBooleanProperty(false);
    private final StringProperty backdropPath = new SimpleStringProperty("");
    private final ObservableList<MovieModel> movieModels = FXCollections.observableArrayList();
    private final ObservableList<GenreModel> genreModels = FXCollections.observableArrayList();
    private final ObservableList<CategoriesModel> categoryModels = FXCollections.observableArrayList();

    public ObjectProperty<ViewType> activeViewProperty() {
        return activeView;
    }

    public ObjectProperty<ViewType> previousViewProperty() {
        return previousView;
    }

    public BooleanProperty isDialogOpenProperty() { return isDialogOpen; }

    public StringProperty backdropPathProperty() {
        return backdropPath;
    }

    public ObservableList<MovieModel> movieModels(){
        return movieModels;
    }

    public ObservableList<GenreModel> genreModels() {
        return genreModels;
    }

    public ObservableList<CategoriesModel> categoryModels() {
        return categoryModels;
    }
}
