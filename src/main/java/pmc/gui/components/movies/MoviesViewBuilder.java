package pmc.gui.components.movies;

import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviePosterActions;
import pmc.gui.utils.SortState;
import pmc.gui.widgets.*;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.icons.IconWidgets;

import java.util.Comparator;
import java.util.function.Predicate;

public class MoviesViewBuilder implements Builder<Region> {
    private MoviesModel model;
    private final MoviePosterActions moviePosterActions;
    private FilteredList<MovieModel> filteredMovies;
    private TilePane tilePane;

    private SortState currentSortState = SortState.NONE;
    private Button sortButton;

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

        sortButton = ButtonWidgets.actionButtonStyle("Sorter på IMDB rating", "button-filter-imdb", event -> sortByImdbRating());

        addPosters();
        vBox.getChildren().addAll(title, sortButton, tilePane);

        return createScrollPane(vBox);
    }

    private void sortByImdbRating() {

        Comparator<MovieModel> comparator = switch (currentSortState) {
            case NONE -> {
                currentSortState = SortState.ASCENDING;
                sortButton.setText("Stigende IMDB rating");
                yield Comparator.comparing(m -> (double) m.imdbRatingProperty().get());
            }
            case ASCENDING -> {
                currentSortState = SortState.DESCENDING;
                sortButton.setText("Faldende IMDB rating");
                yield Comparator.<MovieModel, Double>comparing(m -> (double) m.imdbRatingProperty().get()).reversed();
            }
            case DESCENDING -> {
                sortButton.setText("Sorter på IMDB rating");
                currentSortState = SortState.NONE;
                yield null;
            }
        };

        if (comparator != null) {
            SortedList<MovieModel> sortedList = new SortedList<>(filteredMovies, comparator);
            updatePosters(sortedList);
        } else {
            updatePosters(new SortedList<>(filteredMovies));
        }
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

    private void updatePosters(SortedList<MovieModel> sortedList) {
        tilePane.getChildren().clear();
        for (MovieModel movieModel : sortedList) {
            VBox moviePoster = OtherWidgets.moviePosterWithTitleAndSubtitle(movieModel, 150, 224, 10, moviePosterActions,
                    LabelWidgets.styledLabel(movieModel.titleProperty(), "movie-poster-title"),
                    LabelWidgets.styledLabel(String.valueOf(movieModel.imdbRatingProperty().get()), "movie-poster-subtitle"),
                    IconWidgets.styledIcon(FontAwesomeBrands.IMDB, "movie-poster-imdb-icon"));
            tilePane.getChildren().add(moviePoster);
        }
    }


    private void updatePosters() {
        updatePosters(new SortedList<>(filteredMovies));
    }
}
