package pmc.gui.components.home;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.controls.HorizontalPaginator;
import pmc.gui.widgets.MoviePoster;

import java.util.function.Consumer;

public class HomeViewBuilder implements Builder<Region> {
    private final ObservableList<MovieModel> model;
    private final Consumer<MovieModel> moviePosterClickHandler;
    private final Consumer<MovieModel> playButtonClickHandler;

    public HomeViewBuilder(ObservableList<MovieModel> model,
                           Consumer<MovieModel> moviePosterClickHandler,
                           Consumer<MovieModel> playButtonClickHandler) {
        this.model = model;
        this.moviePosterClickHandler = moviePosterClickHandler;
        this.playButtonClickHandler = playButtonClickHandler;
    }

    @Override
    public Region build() {
        return new HorizontalPaginator<>(
                model,
                this::createMoviePoster,
                "Recently Added"
        );
    }

    private MoviePoster createMoviePoster(MovieModel model) {
        return new MoviePoster(model, 150, 224, 10,
                moviePosterClickHandler,
                playButtonClickHandler);
    }
}
