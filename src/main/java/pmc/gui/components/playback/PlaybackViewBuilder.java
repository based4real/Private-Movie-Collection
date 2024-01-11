package pmc.gui.components.playback;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.widgets.ButtonWidgets;
import pmc.gui.widgets.IconWidgets;
import pmc.gui.widgets.MediaViewWidget;

import java.util.function.Consumer;

public class PlaybackViewBuilder implements Builder<Region> {
    private final PlaybackModel model;
    private final Runnable onBackClicked;

    public PlaybackViewBuilder(PlaybackModel model, Runnable onBackClicked) {
        this.model = model;
        this.onBackClicked = onBackClicked;
    }

    @Override
    public Region build() {
        BorderPane results = new BorderPane();

        results.setTop(createTop());
        results.setCenter(createCenter());
        results.setBottom(createBottom());

        return results;
    }

    private Region createTop() {
        HBox results = new HBox();
        results.getStyleClass().add("topbar");

        Button backIcon = ButtonWidgets.actionIconButton(Material2AL.BACKSPACE, "icon", e -> onBackClicked.run());

        results.getChildren().add(backIcon);

        return results;
    }

    private Region createCenter() {
        MediaViewWidget results = new MediaViewWidget("C:\\Plex\\Film\\The.Shawshank.Redemption.1994.1080p.x264.YIFY.mp4");

//        results.getMediaPlayer().play();

        return results;
    }

    private Region createBottom() {
        BorderPane results = new BorderPane();

        results.setStyle("-fx-background-color: #fff");

        results.setTop(createPlaybackSlider());
        results.setCenter(createPlaybackControls());
        results.setRight(createExtraControls());

        results.setLeft(createMovieDetails());

        return results;
    }

    private Region createPlaybackSlider() {
        VBox results = new VBox(5);

        Slider playbackSlider = new Slider();

        results.getChildren().add(playbackSlider);

        return results;
    }

    private Region createMovieDetails() {
        VBox results = new VBox(5);

        Label title = new Label("Oppenheimer");
        Label year = new Label("2023");
        Label duration = new Label("1:54:47 / 3:00:22");

        results.getChildren().addAll(title, year, duration);
        results.setAlignment(Pos.CENTER_LEFT);
        results.setPadding(new Insets(5));

        return results;
    }

    private Region createPlaybackControls() {
        VBox results = new VBox(5);
        results.setAlignment(Pos.CENTER);

        HBox playbackControls = new HBox(10);
        FontIcon rewind = new FontIcon(Material2MZ.SKIP_PREVIOUS);
        FontIcon play = new FontIcon(Material2MZ.PLAY_ARROW);
        FontIcon forward = new FontIcon(Material2MZ.SKIP_NEXT);

        rewind.setIconSize(24);
        play.setIconSize(24);
        forward.setIconSize(24);

        playbackControls.getChildren().addAll(rewind, play, forward);
        playbackControls.setAlignment(Pos.CENTER);

        results.getChildren().add(playbackControls);

        return results;
    }

    private Region createExtraControls() {
        HBox results = new HBox(5);

        FontIcon volume = new FontIcon(Material2MZ.VOLUME_UP);
        volume.setIconSize(24);

        Slider volumeSlider = new Slider();
        volumeSlider.setPrefWidth(100);

        FontIcon fullScreenIcon = new FontIcon(Material2MZ.OPEN_IN_FULL);
        fullScreenIcon.setIconSize(24);
        Button fullScreenBtn = new Button("", fullScreenIcon);

        results.getChildren().addAll(volume, volumeSlider, fullScreenBtn);
        results.setAlignment(Pos.CENTER_RIGHT);
        results.setPadding(new Insets(5));

        return results;
    }
}
