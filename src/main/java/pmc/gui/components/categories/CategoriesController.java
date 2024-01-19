package pmc.gui.components.categories;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MoviesData;

import java.util.function.Consumer;

public class CategoriesController implements IViewController {
    private final Builder<Region> viewBuilder;

    private ObservableList<CategoriesModel> model;

    public CategoriesController(ObservableList<CategoriesModel> model, Consumer<MoviesData> viewChangeHandler) {
        this.viewBuilder = new CategoriesViewBuilder(model, viewChangeHandler);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
