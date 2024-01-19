package pmc.gui.components.movies;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import pmc.gui.common.*;

import java.util.function.Predicate;

public class MoviesController implements IViewController {
    private final MoviesModel model;
    private final MoviesViewBuilder viewBuilder;

    private final StringProperty title = new SimpleStringProperty("");
    private MovieDataWrapper wrap;

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

    public ObservableList<MovieModel> movieProperty() {
        return this.model.moviesProperty();
    }

    public void setMovies(ObservableList<MovieModel> movies) {
        this.model.setMovies(movies);
    }

    public void setMovieFilter(MovieModel movie) {
        Predicate<MovieModel> filter = movieModel -> movieModel.equals(movie);
        viewBuilder.setMoviesFilter(filter);
    }

    public void setDetails(String title, ObservableList<MovieModel> list) {
        this.model.titleProperty().set(title);

        // todo: må faktisk ikke gøres - at kalde viewBuilder herfra, men lige nu er det løsningen
        viewBuilder.setMoviesFilter(movieModel -> list.contains(movieModel));
    }

    public MovieDataWrapper getMoviedata() {
        return wrap;
    }

    public void setModel(MoviesData movieData) {
        MovieDataWrapper wrap = movieData.movieDataWrapper();
        this.wrap = wrap;

        switch(wrap.getDataType()) {
            case GENRE -> setDetails(wrap.getGenreModel().nameProperty().get(), wrap.getGenreModel().getMovies());
            case CATEGORY -> setDetails(wrap.getCategoryModel().nameProperty().get(), wrap.getCategoryModel().getMovies());
        }
    }

}
