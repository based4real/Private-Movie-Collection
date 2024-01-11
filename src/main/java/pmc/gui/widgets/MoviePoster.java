package pmc.gui.widgets;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import pmc.gui.common.MovieModel;

import java.util.function.Consumer;

import static pmc.gui.widgets.ImageWidgets.scaledRoundedImage;

// todo: refactor
public class MoviePoster extends StackPane {
    public MoviePoster(MovieModel model, double fitWidth, double fitHeight, double rounding,
                       Consumer<MovieModel> posterClickAction,
                       Consumer<MovieModel> playButtonClickAction) {
        super();

        ImageView imageView = scaledRoundedImage("file:" + model.posterPathProperty().get(), fitWidth, fitHeight, rounding);

        Rectangle border = new Rectangle(fitWidth, fitHeight);
        border.setArcWidth(rounding);
        border.setArcHeight(rounding);
        border.setFill(new Color(0, 0, 0, 0.5));
        border.setStroke(new Color(0.85, 0.57, 0.05, 1.0));
        border.setStrokeWidth(1);
        border.setVisible(false);

        Node playButton = new PosterPlayButton(50);
        playButton.setVisible(false);

        this.setPrefSize(fitWidth, fitHeight);
        this.setMaxSize(fitWidth, fitHeight);
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(imageView, border, playButton);
        this.getStyleClass().add("movie-poster");

        this.setOnMouseEntered(e -> {
            playButton.visibleProperty().set(true);
            border.visibleProperty().set(true);
        });

        this.setOnMouseExited(e -> {
            playButton.visibleProperty().set(false);
            border.visibleProperty().set(false);
        });

        this.setOnMouseClicked(e -> posterClickAction.accept(model));

        playButton.setOnMouseClicked(e -> {
            playButtonClickAction.accept(model);
            e.consume(); // gør sådan at når man klikker på play knap at man ikke "klikker gennem" til poster
        });
    }
}
