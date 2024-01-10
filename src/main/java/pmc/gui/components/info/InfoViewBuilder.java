package pmc.gui.components.info;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.ImageWidgets;
import pmc.gui.widgets.MoviePoster;

public class InfoViewBuilder implements Builder<Region> {
    private InfoModel model;

    public InfoViewBuilder(InfoModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        VBox results = new VBox();

        results.getChildren().add(ImageWidgets.boundRoundedImage(model.posterPathProperty(), 150, 224, 10));

        return results;
    }
}
