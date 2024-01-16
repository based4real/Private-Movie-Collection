package pmc.gui.components.home;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesModel;
import pmc.gui.widgets.controls.HorizontalPaginator;
import pmc.gui.widgets.MoviePoster;
import pmc.gui.widgets.ScrollPaneWidgets;

import java.util.function.Consumer;

public class HomeViewBuilder implements Builder<Region> {
    private final ObservableList<MovieModel> model;
    private final ObservableList<CategoriesModel> categoriesModels;
    private final Consumer<MovieModel> moviePosterClickHandler;
    private final Consumer<MovieModel> playButtonClickHandler;

    public HomeViewBuilder(ObservableList<MovieModel> model,
                           ObservableList<CategoriesModel> categoriesModels,
                           Consumer<MovieModel> moviePosterClickHandler,
                           Consumer<MovieModel> playButtonClickHandler) {
        this.categoriesModels = categoriesModels;
        this.model = model;
        this.moviePosterClickHandler = moviePosterClickHandler;
        this.playButtonClickHandler = playButtonClickHandler;
    }

    @Override
    public Region build() {
        VBox results = new VBox();

        createRecentlyAdded(results);
        createCategories(results);

        results.setStyle("-fx-background-color: #323232");

        return ScrollPaneWidgets.defaultPageScrollPane(results);
    }

    private void createRecentlyAdded(VBox vBox) {
        vBox.getChildren().add(new HorizontalPaginator<>(
                model,
                this::createMoviePoster,
                "Recently Added"
        ));
    }

    private void createCategories(VBox vBox) {
        categoriesModels.addListener((ListChangeListener.Change<? extends CategoriesModel> change) -> {
            for (CategoriesModel categoriesModel : categoriesModels) {
                HorizontalPaginator category = new HorizontalPaginator<>(
                        categoriesModel.getMovies(),
                        this::createMoviePoster,
                        categoriesModel.nameProperty().get()
                );

                vBox.getChildren().add(category);
            }
        });
    }

    private MoviePoster createMoviePoster(MovieModel model) {
        return new MoviePoster(model, 150, 224, 10,
                moviePosterClickHandler,
                playButtonClickHandler);
    }
}
