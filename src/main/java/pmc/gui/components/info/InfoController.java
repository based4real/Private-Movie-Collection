package pmc.gui.components.info;

import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieDetailsModel;
import pmc.gui.common.MovieModel;

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

        this.model.descriptionProperty().set(detailsModel.descriptionProperty().get());
    }
}