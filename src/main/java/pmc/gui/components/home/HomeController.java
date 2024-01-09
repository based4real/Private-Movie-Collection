package pmc.gui.components.home;

import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;

public class HomeController implements IViewController {
    private final Builder<Region> viewBuilder;

    public HomeController() {
        this.viewBuilder = new HomeViewBuilder();
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
