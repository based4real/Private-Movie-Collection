package pmc.gui.components.movies;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviePosterActions;
import pmc.gui.common.MoviesData;

import java.util.function.Consumer;

public class MoviesController implements IViewController {
    private final MoviesModel model;
    private final MoviesViewBuilder viewBuilder;

    private final StringProperty title = new SimpleStringProperty("");

    public MoviesController(ObservableList<MovieModel> model, MoviePosterActions moviePosterActions) {
        this.model = new MoviesModel();
        this.model.setMovies(model);
        this.viewBuilder = new MoviesViewBuilder(this.model, moviePosterActions);
    }

    @Override
    public Region getView() {
        return this.viewBuilder.build();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setModel(MoviesData movieData) {

//        this.model.moviesProperty().setAll(movieData.movies());
        this.model.titleProperty().set(movieData.title());
        viewBuilder.setMoviesFilter(movieModel -> movieData.movies().contains(movieModel));
    }

}
