package pmc.gui.components.info;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.widgets.ImageWidgets;
import pmc.gui.widgets.LabelWidgets;
import pmc.gui.widgets.TextWidgets;
import pmc.gui.widgets.icons.IconWidgets;

import java.awt.*;

public class InfoViewBuilder implements Builder<Region> {
    private final InfoModel model;

    public InfoViewBuilder(InfoModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        HBox results = new HBox();
        results.setPadding(new Insets(5, 0, 0, 20));

        ImageView poster = ImageWidgets.boundRoundedImage(model.posterPathProperty(), 220, 340, 10);

        results.getChildren().addAll(poster, createInfoBox());

        return results;
    }

    private Region createInfoBox() {
        VBox results = new VBox(0);
        results.setPadding(new Insets(0, 20, 0, 40));

       // FontIcon menuIcon = IconWidgets.styledIcon(Material2MZ.MENU, "icon");
      //  Label pmc = LabelWidgets.styledLabel("PMC", "logo");

        Text title = TextWidgets.styledText("title", "info-header");
        title.textProperty().bind(model.titleProperty());
        title.setWrappingWidth(800);

        Text director = TextWidgets.styledText("director", "info-director");
        director.textProperty().bind(model.directorProperty());

        Text release = TextWidgets.styledText("release", "info-release");
        release.textProperty().bind(model.releaseProperty());

       // Text description = new Text();
       // description.textProperty().bind(model.descriptionProperty());
       // description.setWrappingWidth(400);

        results.getChildren().addAll(title, director, release);

        return results;
    }
}
