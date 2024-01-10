package pmc.gui.components.home;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;

public class HomeController implements IViewController {
    private final Builder<Region> viewBuilder;

    public HomeController(ObservableList<MovieModel> model) {
        this.viewBuilder = new HomeViewBuilder(model);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
