package pmc.gui.components.movies;

import javafx.collections.ListChangeListener;
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
import pmc.gui.widgets.LabelWidgets;
import pmc.gui.widgets.MoviePoster;
import pmc.gui.widgets.ScrollPaneWidgets;
import pmc.gui.widgets.TextWidgets;

import java.util.function.Consumer;

public class MoviesViewBuilder implements Builder<Region> {

    private MoviesModel model;
    private final Consumer<MovieModel> moviePosterClickHandler;
    private final Consumer<MovieModel> playButtonClickHandler;

    public MoviesViewBuilder(MoviesModel model,
                             Consumer<MovieModel> moviePosterClickHandler,
                             Consumer<MovieModel> playButtonClickHandler) {

        this.moviePosterClickHandler = moviePosterClickHandler;
        this.playButtonClickHandler = playButtonClickHandler;
        this.model = model;
    }

    @Override
    public Region build() {
        VBox vBox = new VBox(20);

        Label title = LabelWidgets.styledLabel(model.titleProperty(), "hpage-title");
        vBox.getChildren().addAll(title, createTilePane());

        return createScrollPane(vBox);
    }

    private ScrollPane createScrollPane(Node value) {
        ScrollPane scrollPane = ScrollPaneWidgets.defaultPageScrollPane(value);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(10, 5, 10, 10));

        return scrollPane;
    }

    private TilePane createTilePane() {
        TilePane tilePane = new TilePane();
        tilePane.setVgap(15);
        tilePane.setHgap(15);

        addPoster(tilePane);

        return tilePane;
    }

    private void addPoster(TilePane tilePane) {
        model.moviesProperty().addListener((ListChangeListener.Change<? extends MovieModel> change) -> {
            tilePane.getChildren().clear();
            for (MovieModel movieModel : model.moviesProperty()) {
                MoviePoster moviePoster = new MoviePoster(movieModel, 150, 224, 10,
                        moviePosterClickHandler,
                        playButtonClickHandler);

                tilePane.getChildren().add(moviePoster);
            }
        });
    }
}
