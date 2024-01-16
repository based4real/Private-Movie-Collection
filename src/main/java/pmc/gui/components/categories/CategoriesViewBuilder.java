package pmc.gui.components.categories;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.icons.ScrollPaneWidgets;

public class CategoriesViewBuilder implements Builder<Region> {
    private ObservableList<CategoriesModel> model;

    public CategoriesViewBuilder(ObservableList<CategoriesModel> model) {
        this.model = model;
    }
    @Override
    public Region build() {
        ScrollPane scrollPane = ScrollPaneWidgets.defaultPageScrollPane(addCategories());
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    private TilePane addCategories() {
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(5, 20, 0, 20));
        tilePane.setHgap(15);
        tilePane.setVgap(15);

        this.model.addListener((ListChangeListener.Change<? extends CategoriesModel> change) -> {
            for (CategoriesModel categoryModel : model) {
                Button btn = ButtonWidgets.actionButtonStyle("name", "genre-category-button", event -> categoryClick(categoryModel));
                btn.textProperty().bind(categoryModel.nameProperty());

                tilePane.getChildren().add(btn);
            }
        });
        return tilePane;
    }

    private void categoryClick(CategoriesModel categoryModel) {
        if (!categoryModel.getMovies().isEmpty()) {
            for (MovieModel matchingMovie : categoryModel.getMovies()) {
                System.out.println(categoryModel.nameProperty().get() + " " + matchingMovie.tmdbIdProperty().get());
            }
        } else {
            System.out.println("moviemodels ikke fundet");
        }
    }
}
