package pmc.gui.components.Genres;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.be.Genre;
import pmc.gui.common.GenreModel;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;

public class GenresController implements IViewController {
    private final Builder<Region> viewBuilder;

    public GenresController(ObservableList<GenreModel> model, ObservableList<MovieModel> movieModel) {
        this.viewBuilder = new GenresViewBuilder(model, movieModel);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}
