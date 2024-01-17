package pmc.gui.widgets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MoviePosterActions;
import pmc.gui.widgets.icons.IconWidgets;

import java.util.function.Consumer;

import static pmc.gui.widgets.ImageWidgets.scaledRoundedImage;

// todo: refactor
public class MoviePoster extends StackPane {
    public MoviePoster(MovieModel model, double fitWidth, double fitHeight, double rounding,
                       MoviePosterActions actions) {
        super();

        ImageView imageView = scaledRoundedImage("file:" + model.posterPathProperty().get(), fitWidth, fitHeight, rounding);

        Rectangle border = new Rectangle(fitWidth, fitHeight);
        border.setArcWidth(rounding);
        border.setArcHeight(rounding);
        border.setFill(new Color(0, 0, 0, 0.5));
        border.setStroke(new Color(0.85, 0.57, 0.05, 1.0));
        border.setStrokeWidth(1);
        border.setVisible(false);

        Node playButton = IconWidgets.posterPlayButton(50);
        playButton.setVisible(false);

        FontIcon deleteIcon = IconWidgets.styledIcon(Material2OutlinedAL.DELETE, "movie-poster-delete-icon");
        deleteIcon.setVisible(false);
        StackPane.setAlignment(deleteIcon, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(deleteIcon, new Insets(0, 0, 2, 0));

        FontIcon editIcon = IconWidgets.styledIcon(Material2OutlinedAL.EDIT, "movie-poster-edit-icon");
        editIcon.setVisible(false);
        StackPane.setAlignment(editIcon, Pos.BOTTOM_LEFT);
        StackPane.setMargin(editIcon, new Insets(0, 0, 2, 2));

        this.setPrefSize(fitWidth, fitHeight);
        this.setMaxSize(fitWidth, fitHeight);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(imageView, border, playButton, deleteIcon, editIcon);
        this.getStyleClass().add("movie-poster");

        this.setOnMouseEntered(e -> {
            border.visibleProperty().set(true);
            playButton.visibleProperty().set(true);
            deleteIcon.visibleProperty().set(true);
            editIcon.visibleProperty().set(true);
        });

        this.setOnMouseExited(e -> {
            border.visibleProperty().set(false);
            playButton.visibleProperty().set(false);
            deleteIcon.visibleProperty().set(false);
            editIcon.visibleProperty().set(false);
        });

        this.setOnMouseClicked(e -> actions.info().accept(model));

        playButton.setOnMouseClicked(e -> {
            actions.play().accept(model);
            e.consume(); // gør sådan at når man klikker på play knap at man ikke "klikker gennem" til poster
        });

        deleteIcon.setOnMouseClicked(e -> {
            actions.delete().accept(model);
            e.consume();
        });

        editIcon.setOnMouseClicked(e -> {
            actions.edit().accept(model);
            e.consume();
        });
    }
}
