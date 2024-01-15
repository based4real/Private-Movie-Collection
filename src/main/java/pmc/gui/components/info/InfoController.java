package pmc.gui.components.info;

import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.dal.rest.tmdb.movie.TMDBGenre;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieDetailsModel;
import pmc.gui.common.MovieModel;
import pmc.gui.components.playback.PlaybackController;

import java.util.function.Consumer;

public class InfoController implements IViewController {
    private final InfoModel model;
    private final Builder<Region> viewBuilder;

    public InfoController() {
        model = new InfoModel();
        this.viewBuilder = new InfoViewBuilder(model);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    public void setModel(MovieModel model) {
        this.model.posterPathProperty().set(model.posterPathProperty().get());
    }

    public void setDetailsModel(MovieDetailsModel detailsModel) {
        this.model.titleProperty().set(detailsModel.titleProperty().get());
        this.model.directorProperty().set(detailsModel.directorProperty().get());
        this.model.releaseProperty().set(detailsModel.releaseProperty().get());
        this.model.runtimeProperty().set(detailsModel.runtimeProperty().get());
        this.model.ratedProperty().set(detailsModel.ratedProperty().get());
        this.model.genresProperty().setAll(detailsModel.genresProperty());

        this.model.descriptionProperty().set(detailsModel.descriptionProperty().get());
    }
}