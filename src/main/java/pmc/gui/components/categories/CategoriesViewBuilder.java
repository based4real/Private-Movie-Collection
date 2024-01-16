package pmc.gui.components.categories;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.util.Builder;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviesData;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.ScrollPaneWidgets;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CategoriesViewBuilder implements Builder<Region> {
    private ObservableList<CategoriesModel> model;
    private Consumer<MoviesData> viewChangehandler;

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
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(5, 20, 0, 20));
        tilePane.setHgap(15);
        tilePane.setVgap(15);

        /*
            Lidt dumt tjek.. men vi lytter først om modellen opdaterer sig, hvis ja så loop igennem dem.
            Derefter tjekker vi om .getMovies() ændrer sig, hvis ja så tjek om den er tom og hvis den ikke
            er så tilføj til tilepane..
            Bruges så der ikke vises en tom kategori.
         */
        this.model.addListener((ListChangeListener.Change<? extends CategoriesModel> change) -> {
            for (CategoriesModel categoryModel : model) {
                categoryModel.getMovies().addListener((ListChangeListener.Change<? extends MovieModel> moviesUpdate) -> {
                    if (!categoryModel.getMovies().isEmpty()) {
                        Button btn = ButtonWidgets.actionButtonStyle(categoryModel.nameProperty().get(), "genre-category-button", event -> categoryClick(categoryModel));

                        tilePane.getChildren().add(btn);
                    }
                });
            }
        });
        return tilePane;
    }

    private void categoryClick(CategoriesModel categoryModel) {
       // if (!categoryModel.getMovies().isEmpty())
        this.viewChangehandler.accept(new MoviesData(categoryModel.nameProperty().get(), categoryModel.getMovies()));
    }
}
