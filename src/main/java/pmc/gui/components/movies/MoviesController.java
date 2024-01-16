package pmc.gui.components.movies;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviesData;

import java.util.function.Consumer;

public class MoviesController implements IViewController {
    private MoviesModel model;
    private final Builder<Region> viewBuilder;

    private ObservableList<MovieModel> movieModels = FXCollections.observableArrayList();
    private final StringProperty title = new SimpleStringProperty("");


    public MoviesController(Consumer<MovieModel> viewChangeHandler, Consumer<MovieModel> playMovieHandler) {
        this.model = new MoviesModel();
        this.viewBuilder = new MoviesViewBuilder(model, viewChangeHandler, playMovieHandler);
    }

    @Override
    public Region getView() {
        return this.viewBuilder.build();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setModel(MoviesData movieData) {
        this.model.moviesProperty().setAll(movieData.movies());
        this.model.titleProperty().set(movieData.title());

    }

}
