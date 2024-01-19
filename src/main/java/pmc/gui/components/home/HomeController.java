package pmc.gui.components.home;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviePosterActions;
import pmc.gui.components.categories.CategoriesModel;

public class HomeController implements IViewController {
    private final Builder<Region> viewBuilder;

    public HomeController(ObservableList<MovieModel> model,
                          ObservableList<CategoriesModel> categoriesModels,
                          MoviePosterActions moviePosterActions) {
        this.viewBuilder = new HomeViewBuilder(model, categoriesModels, moviePosterActions);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
