package pmc.gui.components.pmc;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pmc.gui.components.genres.GenresModel;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesModel;

public class PMCModel {
    private final ObjectProperty<ViewType> activeView = new SimpleObjectProperty<>(ViewType.HOME);
    private final ObjectProperty<ViewType> previousView = new SimpleObjectProperty<>(ViewType.HOME);
    private final BooleanProperty isDialogOpen = new SimpleBooleanProperty(false);
    private final StringProperty backdropPath = new SimpleStringProperty("");
    private final ObservableList<MovieModel> movieModels = FXCollections.observableArrayList();
    private final ObservableList<GenresModel> genreModels = FXCollections.observableArrayList();
    private final ObservableList<CategoriesModel> categoryModels = FXCollections.observableArrayList();
    private final BooleanProperty copyingFile = new SimpleBooleanProperty(false);
    private final DoubleProperty fileProgress = new SimpleDoubleProperty(0);
    private final BooleanProperty isFullscreen = new SimpleBooleanProperty(false);
    private final BooleanProperty isCollapsed = new SimpleBooleanProperty(false);

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

    public ObservableList<GenresModel> genreModels() {
        return genreModels;
    }

    public ObservableList<CategoriesModel> categoryModels() {
        return categoryModels;
    }

    public BooleanProperty copyingFileProperty() {
        return copyingFile;
    }

    public DoubleProperty fileProgressProperty() { return fileProgress; }

    public BooleanProperty isFullscreenProperty() {
        return isFullscreen;
    }

    public BooleanProperty isCollapsedProperty() {
        return isCollapsed;
    }
}
