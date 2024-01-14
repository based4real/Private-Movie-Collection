package pmc.gui.components.info;

import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import pmc.gui.widgets.ImageWidgets;

public class InfoViewBuilder implements Builder<Region> {
    private final InfoModel model;

    public InfoViewBuilder(InfoModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        HBox results = new HBox();

        ImageView poster = ImageWidgets.boundRoundedImage(model.posterPathProperty(), 220, 340, 10);

        results.getChildren().addAll(poster, createInfoBox());

        return results;
    }

    private Region createInfoBox() {
        VBox results = new VBox(10);

        Text title = new Text();
        title.textProperty().bind(model.titleProperty());

        Text description = new Text();
        description.textProperty().bind(model.descriptionProperty());
        description.setWrappingWidth(400);

        results.getChildren().addAll(title, description);

        return results;
    }
}
