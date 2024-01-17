package pmc.gui.components.movies;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviePosterActions;
import pmc.gui.widgets.LabelWidgets;
import pmc.gui.widgets.MoviePoster;
import pmc.gui.widgets.ScrollPaneWidgets;
import pmc.gui.widgets.TextWidgets;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class MoviesViewBuilder implements Builder<Region> {
    private MoviesModel model;
    private final MoviePosterActions moviePosterActions;
    private FilteredList<MovieModel> filteredMovies;
    private TilePane tilePane;

    public MoviesViewBuilder(MoviesModel model, MoviePosterActions moviePosterActions) {
        this.model = model;
        this.moviePosterActions = moviePosterActions;
        this.tilePane = new TilePane();
        tilePane.setVgap(15);
        tilePane.setHgap(15);
        this.filteredMovies = new FilteredList<>(model.moviesProperty());
    }

    @Override
    public Region build() {
        VBox vBox = new VBox(20);
        Label title = LabelWidgets.styledLabel(model.titleProperty(), "hpage-title");

        addPosters();
        vBox.getChildren().addAll(title, tilePane);

        return createScrollPane(vBox);
    }

    public void setMoviesFilter(Predicate<MovieModel> filter) {
        this.filteredMovies.setPredicate(filter);
        updatePosters();
    }

    private ScrollPane createScrollPane(Node value) {
        ScrollPane scrollPane = ScrollPaneWidgets.defaultPageScrollPane(value);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(10, 5, 10, 10));

        return scrollPane;
    }

    private void addPosters() {
        updatePosters();
        model.moviesProperty().addListener((ListChangeListener.Change<? extends MovieModel> change) -> {
            updatePosters();
        });
    }

    private void updatePosters() {
        tilePane.getChildren().clear();

        for (MovieModel movieModel : filteredMovies) {
            MoviePoster moviePoster = new MoviePoster(movieModel, 150, 224, 10, moviePosterActions);
            tilePane.getChildren().add(moviePoster);
        }
    }
}
