package pmc.gui.components.playback;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.util.Builder;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.utils.Animations;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.MediaViewWidget;
import pmc.gui.widgets.controls.ProgressSlider;

public class PlaybackViewBuilder implements Builder<Region> {
    private final PlaybackModel model;
    private final Runnable onPlay;
    private final Runnable onBackClicked;

    private double topHeight = 0;
    private double bottomHeight = 0;

    public PlaybackViewBuilder(PlaybackModel model, Runnable onPlay, Runnable onBackClicked) {
        this.model = model;
        this.onPlay = onPlay;
        this.onBackClicked = onBackClicked;
    }

    @Override
    public Region build() {
        StackPane results = new StackPane();
        results.getStyleClass().add("playback");

        BorderPane topAndBottom = new BorderPane();
        Region top = createTop();
        Region bottom = createBottom();

        topAndBottom.setTop(top);
        topAndBottom.setBottom(bottom);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.visibleProperty().bind(model.isLoadingProperty());

        results.getChildren().addAll(createCenter(), topAndBottom, progressIndicator);

        top.boundsInParentProperty().addListener((obs, ov, nv) -> topHeight = nv.getHeight());
        bottom.boundsInParentProperty().addListener((obs, ov, nv) -> bottomHeight = nv.getHeight());

        results.setOnMouseEntered(e -> {
            Animations.slideIn(top, Duration.millis(100), -topHeight, 0);
            Animations.slideIn(bottom, Duration.millis(100), bottomHeight, 0);
        });

        results.setOnMouseExited(e -> {
            Animations.slideOut(top, Duration.millis(100), 0, -topHeight);
            Animations.slideOut(bottom, Duration.millis(100), 0, bottomHeight);
        });

        return results;
    }

    private Region createTop() {
        HBox results = new HBox();
        results.getStyleClass().add("playback-top");

        Button backIcon = ButtonWidgets.actionIconButton(Material2AL.BACKSPACE, "icon", e -> {
            model.reset();
            onBackClicked.run();
        });

        results.getChildren().add(backIcon);

        return results;
    }

    private Region createCenter() {
        MediaViewWidget results = new MediaViewWidget(model.mediaPlayerProperty().get());
        results.getStyleClass().add("playback-mediaview");

        model.mediaPlayerProperty().addListener((obs, ov, nv) -> {
            results.setMediaPlayer(nv);
        });

        return results;
    }

    private Region createBottom() {
        BorderPane results = new BorderPane();
        results.getStyleClass().add("playback-bottom");

        results.setTop(createPlaybackSlider());
        results.setCenter(createPlaybackControls());
        results.setRight(createExtraControls());

        results.setLeft(createMovieDetails());

        return results;
    }

    private Region createPlaybackSlider() {
        VBox results = new VBox(5);

        ProgressSlider playbackSlider = new ProgressSlider();

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

        ProgressSlider volumeSlider = new ProgressSlider();
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
