package pmc.gui.components.home;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.controls.HorizontalPaginator;
import pmc.gui.widgets.MoviePoster;

public class HomeViewBuilder implements Builder<Region> {
    private final ObservableList<MovieModel> model;

    public HomeViewBuilder(ObservableList<MovieModel> model) {
        this.model = model;
    }

    @Override
    public Region build() {
        return new HorizontalPaginator<>(
                model,
                movieModel -> new MoviePoster("file:" + movieModel.posterPathProperty().get(), 150, 224, 10),
                "Recently Added"
        );
    }
}
