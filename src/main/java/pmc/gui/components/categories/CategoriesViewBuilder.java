package pmc.gui.components.categories;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.util.Builder;
import pmc.gui.common.MovieDataWrapper;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviesData;
import pmc.gui.utils.StringHandler;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.ScrollPaneWidgets;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CategoriesViewBuilder implements Builder<Region> {
    private ObservableList<CategoriesModel> model;
    private Consumer<MoviesData> viewChangehandler;
    private TilePane tilePane;

    public CategoriesViewBuilder(ObservableList<CategoriesModel> model, Consumer<MoviesData> viewChangeHandler) {
        this.model = model;
        this.viewChangehandler = viewChangeHandler;
    }
    @Override
    public Region build() {
        ScrollPane scrollPane = ScrollPaneWidgets.defaultPageScrollPane(addCategories());
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    private TilePane addCategories() {
        tilePane = new TilePane();
        tilePane.setPadding(new Insets(5, 20, 0, 20));
        tilePane.setHgap(15);
        tilePane.setVgap(15);

        this.model.addListener((ListChangeListener.Change<? extends CategoriesModel> change) -> {
            updateCategoryButtons();
        });
        return tilePane;
    }

    private void updateCategoryButtons() {
        tilePane.getChildren().clear();

        for (CategoriesModel categoryModel : model) {
            categoryModel.getMovies().addListener((ListChangeListener.Change<? extends MovieModel> moviesChange) -> {
                handleCategoryMoviesChange(categoryModel);
            });

            if (!categoryModel.getMovies().isEmpty()) {
                addButtonForCategory(categoryModel);
            }
        }
    }

    private void handleCategoryMoviesChange(CategoriesModel categoryModel) {
        boolean hasButton = tilePane.getChildren().stream()
                .anyMatch(node -> node instanceof Button && ((Button) node).getText().equals(categoryModel.nameProperty().get()));

        if (!categoryModel.getMovies().isEmpty() && !hasButton) {
            addButtonForCategory(categoryModel);
        } else if (categoryModel.getMovies().isEmpty() && hasButton) {
            removeButtonForCategory(categoryModel);
        }
    }


    private void addButtonForCategory(CategoriesModel categoryModel) {
        Button btn = ButtonWidgets.actionButtonStyle(StringHandler.trimName(categoryModel.nameProperty().get(), 15), "genre-category-button", event -> categoryClick(categoryModel));
        tilePane.getChildren().add(btn);
    }

    private void removeButtonForCategory(CategoriesModel categoryModel) {
        tilePane.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getText().equals(categoryModel.nameProperty().get()));
    }

    private void categoryClick(CategoriesModel categoryModel) {
        this.viewChangehandler.accept(new MoviesData(new MovieDataWrapper(categoryModel)));
    }
}
