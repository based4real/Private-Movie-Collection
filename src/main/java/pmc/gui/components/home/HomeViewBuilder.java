package pmc.gui.components.home;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.util.Builder;
import pmc.gui.common.GenreModel;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.controls.HorizontalPaginator;
import pmc.gui.widgets.MoviePoster;

import java.awt.*;
import java.util.function.Consumer;

public class HomeViewBuilder implements Builder<Region> {
    private final ObservableList<MovieModel> model;
    private final ObservableList<GenreModel> genres;
    private final Consumer<MovieModel> moviePosterClickHandler;
    private final Consumer<MovieModel> playButtonClickHandler;

    public HomeViewBuilder(ObservableList<MovieModel> model,
                           ObservableList<GenreModel> genres,
                           Consumer<MovieModel> moviePosterClickHandler,
                           Consumer<MovieModel> playButtonClickHandler) {
        this.genres = genres;
        this.model = model;
        this.moviePosterClickHandler = moviePosterClickHandler;
        this.playButtonClickHandler = playButtonClickHandler;
    }

    @Override
    public Region build() {
        VBox results = new VBox();

        createRecentlyAdded(results);
        createGenres(results);

        ScrollPane scrollPane = new ScrollPane(results);
        scrollPane.setContent(results);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent");

        results.setStyle("-fx-background-color: #323232");

        return scrollPane;
    }

    private void createRecentlyAdded(VBox vBox) {
        vBox.getChildren().add(new HorizontalPaginator<>(
                model,
                this::createMoviePoster,
                "Recently Added"
        ));
    }

    private void createGenres(VBox vBox) {
        genres.addListener((ListChangeListener.Change<? extends GenreModel> change) -> {
            for (GenreModel genreModel : genres) {
                HorizontalPaginator hello = new HorizontalPaginator<>(
                        genreModel.getMovies(),
                        this::createMoviePoster,
                        genreModel.nameProperty().get()
                );
                vBox.getChildren().add(hello);
            }
        });
    }

    private MoviePoster createMoviePoster(MovieModel model) {
        return new MoviePoster(model, 150, 224, 10,
                moviePosterClickHandler,
                playButtonClickHandler);
    }
}
