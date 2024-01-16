package pmc.gui.components.categories;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;

public class CategoriesController implements IViewController {
    private final Builder<Region> viewBuilder;

    public CategoriesController(ObservableList<CategoriesModel> model) {
        this.viewBuilder = new CategoriesViewBuilder();
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
