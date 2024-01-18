package pmc.gui.widgets;

import javafx.beans.value.ObservableStringValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviePosterActions;

public class OtherWidgets {
    public static VBox moviePosterWithText(MovieModel model, double fitWidth, double fitHeight, double rounding,
                                           MoviePosterActions actions, Label title) {
        VBox results = new VBox(10);
        title.setMaxWidth(fitWidth);
        results.getChildren().addAll(new MoviePoster(model, fitWidth, fitHeight, rounding, actions), title);
        return results;
    }

    public static VBox moviePosterWithTitleAndSubtitle(MovieModel model, double fitWidth, double fitHeight, double rounding,
                                                       MoviePosterActions actions, Label title, Label subtitle) {
        VBox results = new VBox(10);
        title.setMaxWidth(fitWidth);
        subtitle.setMaxWidth(fitWidth);
        results.getChildren().addAll(new MoviePoster(model, fitWidth, fitHeight, rounding, actions), title, subtitle);
        return results;
    }

    public static VBox moviePosterWithTitleAndSubtitle(MovieModel model, double fitWidth, double fitHeight, double rounding,
                                                       MoviePosterActions actions, Label title, Label subtitle, FontIcon subIcon) {
        VBox results = new VBox(10);
        HBox subtitleContainer = new HBox(10);
        title.setMaxWidth(fitWidth);
        subtitleContainer.getChildren().addAll(subIcon, subtitle);
        subtitleContainer.setMaxWidth(fitWidth);
        subtitleContainer.setAlignment(Pos.CENTER_LEFT);
        results.getChildren().addAll(new MoviePoster(model, fitWidth, fitHeight, rounding, actions), title, subtitleContainer);
        return results;
    }
}
