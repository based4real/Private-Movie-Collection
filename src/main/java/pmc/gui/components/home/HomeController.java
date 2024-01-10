package pmc.gui.components.home;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;

import java.util.function.Consumer;

public class HomeController implements IViewController {
    private final Builder<Region> viewBuilder;

    public HomeController(ObservableList<MovieModel> model, Consumer<MovieModel> viewChangeHandler) {
        this.viewBuilder = new HomeViewBuilder(model, viewChangeHandler);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
