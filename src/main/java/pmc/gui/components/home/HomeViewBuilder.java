package pmc.gui.components.home;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviePosterActions;
import pmc.gui.components.categories.CategoriesModel;
import pmc.gui.widgets.LabelWidgets;
import pmc.gui.widgets.OtherWidgets;
import pmc.gui.widgets.controls.HorizontalPaginator;
import pmc.gui.widgets.MoviePoster;
import pmc.gui.widgets.ScrollPaneWidgets;
import pmc.gui.widgets.icons.IconWidgets;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class HomeViewBuilder implements Builder<Region> {
    private final ObservableList<MovieModel> model;
    private final ObservableList<CategoriesModel> categoriesModels;
    private final MoviePosterActions moviePosterActions;

    private Map<CategoriesModel, HorizontalPaginator<MovieModel>> paginatorMap = new HashMap<>();


    public HomeViewBuilder(ObservableList<MovieModel> model,
                           ObservableList<CategoriesModel> categoriesModels,
                           MoviePosterActions moviePosterActions) {
        this.categoriesModels = categoriesModels;
        this.model = model;
        this.moviePosterActions = moviePosterActions;
    }

    @Override
    public Region build() {
        VBox results = new VBox();

        createDeletionReminder(results);
        createRecentlyAdded(results);
        createCategories(results);

        results.setStyle("-fx-background-color: #323232");

        return ScrollPaneWidgets.defaultPageScrollPane(results);
    }

    private void createDeletionReminder(VBox vBox) {
        FilteredList<MovieModel> filteredMovies = new FilteredList<>(model, this::deletionReminderPredicate);

        HorizontalPaginator<MovieModel> deletionReminderPaginator = new HorizontalPaginator<>(
                filteredMovies,
                movie -> OtherWidgets.moviePosterWithText(movie, 150, 224, 10, moviePosterActions,
                        LabelWidgets.styledLabel(movie.titleProperty(), "movie-poster-title")),
                "Husk at slette"
        );

        vBox.getChildren().add(deletionReminderPaginator);
    }

    private boolean deletionReminderPredicate(MovieModel movie) {
        LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
        LocalDateTime lastSeen = movie.lastSeenProperty().get();

        boolean isLastSeenBeforeTwoYears = (lastSeen != null) && lastSeen.isBefore(twoYearsAgo); // håndter null på ny tilføjet film

        return isLastSeenBeforeTwoYears && movie.personalRatingProperty().get() < 3;
    }

    private void createRecentlyAdded(VBox vBox) {
        vBox.getChildren().add(new HorizontalPaginator<>(
                model,
                movie -> OtherWidgets.moviePosterWithText(movie, 150, 224, 10, moviePosterActions,
                        LabelWidgets.styledLabel(movie.titleProperty(), "movie-poster-title")),
                "Senest tilføjet",
                true
        ));
    }

    private void createCategories(VBox vBox) {
        categoriesModels.addListener((ListChangeListener.Change<? extends CategoriesModel> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (CategoriesModel addedCategory : change.getAddedSubList()) {
                        HorizontalPaginator<MovieModel> category = new HorizontalPaginator<>(
                                addedCategory.getMovies(),
                                this::createMoviePoster,
                                addedCategory.nameProperty().get()
                        );

                        addedCategory.nameProperty().addListener((obs, ov, nv) -> {
                            category.setTitle(nv); // bruges til at opdatere titel på
                        });

                        vBox.getChildren().add(category);
                        paginatorMap.put(addedCategory, category);
                    }
                }
                if (change.wasRemoved()) {
                    for (CategoriesModel removedCategory : change.getRemoved()) {
                        HorizontalPaginator<MovieModel> paginatorToRemove = paginatorMap.get(removedCategory);
                        if (paginatorToRemove != null) {
                            vBox.getChildren().remove(paginatorToRemove);
                            paginatorMap.remove(removedCategory);
                        }
                    }
                }
            }
        });
    }

    private MoviePoster createMoviePoster(MovieModel model) {
        return new MoviePoster(model, 150, 224, 10, moviePosterActions);
    }
}
