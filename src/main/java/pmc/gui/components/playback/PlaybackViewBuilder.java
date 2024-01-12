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
import javafx.scene.media.MediaPlayer;
import javafx.util.Builder;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.MediaViewWidget;

public class PlaybackViewBuilder implements Builder<Region> {
    private final PlaybackModel model;
    private final MediaPlayer mediaPlayer;
    private final Runnable onPlay;
    private final Runnable onBackClicked;

    public PlaybackViewBuilder(PlaybackModel model, MediaPlayer mediaPlayer, Runnable onPlay, Runnable onBackClicked) {
        this.model = model;
        this.mediaPlayer = mediaPlayer;
        this.onPlay = onPlay;
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
        return new MediaViewWidget(mediaPlayer);
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
//        Button play = ButtonWidgets.actionIconButton(Material2MZ.PLAY_ARROW, "icon", e -> onPlay.run());

        Button play = ButtonWidgets.toggleableActionIconButton(
                Material2MZ.PLAY_CIRCLE_OUTLINE,
                Material2MZ.PAUSE_CIRCLE_OUTLINE,
                "icon",
                model.isPlayingProperty(),
                e -> onPlay.run());

        FontIcon forward = new FontIcon(Material2MZ.SKIP_NEXT);

        rewind.setIconSize(24);
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
