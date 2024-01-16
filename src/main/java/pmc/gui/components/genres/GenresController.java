package pmc.gui.components.genres;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MoviesData;

import java.util.function.Consumer;

public class GenresController implements IViewController {
    private final Builder<Region> viewBuilder;

    public GenresController(ObservableList<GenresModel> model, Consumer<MoviesData> viewChangeHandler) {
        this.viewBuilder = new GenresViewBuilder(model, viewChangeHandler);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
