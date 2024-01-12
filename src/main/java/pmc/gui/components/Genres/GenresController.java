package pmc.gui.components.Genres;

import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;

public class GenresController implements IViewController {
    private final Builder<Region> viewBuilder;

    public GenresController() {
        this.viewBuilder = new GenresViewBuilder();
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
