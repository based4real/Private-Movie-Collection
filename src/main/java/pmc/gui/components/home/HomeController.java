package pmc.gui.components.home;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.GenreModel;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesModel;

import java.util.function.Consumer;

public class HomeController implements IViewController {
    private final Builder<Region> viewBuilder;

    public HomeController(ObservableList<MovieModel> model, ObservableList<CategoriesModel> categoriesModels, Consumer<MovieModel> viewChangeHandler, Consumer<MovieModel> playMovieHandler) {
        this.viewBuilder = new HomeViewBuilder(model, categoriesModels, viewChangeHandler, playMovieHandler);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
