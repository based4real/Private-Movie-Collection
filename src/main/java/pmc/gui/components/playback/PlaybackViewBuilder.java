package pmc.gui.components.playback;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import javafx.util.Builder;
import javafx.util.Duration;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import pmc.gui.utils.Animations;
import pmc.gui.utils.TimeStringConverter;
import pmc.gui.widgets.LabelWidgets;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.MediaViewWidget;
import pmc.gui.widgets.controls.ProgressSlider;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class PlaybackViewBuilder implements Builder<Region> {
    private final PlaybackModel model;
    private final Runnable onPlay;
    private final Runnable onBackClicked;
    private final Runnable onMute;
    private final Consumer<Double> changeVolume;
    private final Consumer<Double> onSeek;
    private final Runnable onFulScreen;
    private final Consumer<String> openInSystem;

    private Label curTimeLbl;
    private Label totalDurLbl;

    public PlaybackViewBuilder(PlaybackModel model,
                               Runnable onPlay,
                               Runnable onBackClicked,
                               Runnable onMute,
                               Consumer<Double> changeVolume,
                               Consumer<Double> onSeek,
                               Runnable onFullscreen,
                               Consumer<String> openInSystem) {
        this.model = model;
        this.onPlay = onPlay;
        this.onBackClicked = onBackClicked;
        this.onMute = onMute;
        this.changeVolume = changeVolume;
        this.onSeek = onSeek;
        this.onFulScreen = onFullscreen;
        this.openInSystem = openInSystem;
    }

    @Override
    public Region build() {
        StackPane results = new StackPane();
        results.getStyleClass().add("playback");

        BorderPane topAndBottom = new BorderPane();
        Region top = createTop();
        Region bottom = createBottom();
        top.setPadding(new Insets(20));
        bottom.setPadding(new Insets(20));

        top.setOpacity(0.0);
        bottom.setOpacity(0.0);

        topAndBottom.setTop(top);
        topAndBottom.setBottom(bottom);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.getStyleClass().add("playback-progress-indicator");
        progressIndicator.visibleProperty().bind(model.isLoadingProperty());

        results.getChildren().addAll(createCenter(), topAndBottom, progressIndicator);

        results.setOnMouseEntered(e -> {
            Animations.fadeIn(top, Duration.millis(100), 0, 1);
            Animations.fadeIn(bottom, Duration.millis(100), 0, 1);
        });

        results.setOnMouseExited(e -> {
            Animations.fadeOut(top, Duration.millis(100), 1, 0);
            Animations.fadeOut(bottom, Duration.millis(100), 1, 0);
        });

        return results;
    }

    private Region createTop() {
        HBox results = new HBox();
        results.getStyleClass().add("playback-top");

        Button backIcon = ButtonWidgets.actionIconButton(Material2AL.ARROW_BACK, "playback-back-icon", e -> {
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
        playbackSlider.getStyleClass().add("playback-slider");

        AtomicBoolean isDragging = new AtomicBoolean(false);
        playbackSlider.setOnMousePressed(evt -> isDragging.set(true));
        playbackSlider.setOnMouseReleased(evt -> {
            onSeek.accept(playbackSlider.valueProperty().get());
            isDragging.set(false);
        });

        playbackSlider.valueProperty().addListener((obs, ov, nv) -> {
            if (isDragging.get()) {
                double newPosition = nv.doubleValue();
                double totalDuration = model.totalDurationProperty().get();
                double newTime = newPosition / 100.0 * totalDuration;
                updateCurrentTimeLabel(newTime);
            } else {
                model.currentPositionProperty().set(nv.doubleValue());
            }
        });

        model.currentPositionProperty().addListener((obs, ov, nv) -> {
            if (!isDragging.get()) {
                playbackSlider.valueProperty().set(nv.doubleValue());
            }
        });

        model.currenTimeProperty().addListener((obs, ov, nv) -> {
            if (!isDragging.get()) {
                updateCurrentTimeLabel(nv.doubleValue());
            }
        });

        results.getChildren().add(playbackSlider);

        return results;
    }

    private void updateCurrentTimeLabel(double currentTimeInSeconds) {
        curTimeLbl.setText(new TimeStringConverter().toString((int) (currentTimeInSeconds * 1000)));
    }

    private Region createMovieDetails() {
        VBox results = new VBox(5);

        Label title = LabelWidgets.styledLabel(model.movieTitleProperty(), "playback-title");

        curTimeLbl = LabelWidgets.styledLabel("00:00", "playback-time");
        totalDurLbl = LabelWidgets.styledLabel("", "playback-time");

        HBox time = new HBox();
        time.getChildren().addAll(curTimeLbl, LabelWidgets.styledLabel("/", "playback-time"), totalDurLbl);

        totalDurLbl.textProperty().bind(Bindings.createStringBinding(() -> {
            double totalDur = model.totalDurationProperty().get();
            return new TimeStringConverter().toString((int) (totalDur * 1000));
        }, model.totalDurationProperty().asObject()));

        results.getChildren().addAll(title, time);
        results.setAlignment(Pos.CENTER_LEFT);
        results.setPadding(new Insets(5));

        return results;
    }

    private Region createPlaybackControls() {
        VBox results = new VBox(5);
        results.setAlignment(Pos.CENTER);

        HBox playbackControls = new HBox(10);

        Button play = ButtonWidgets.toggleableActionIconButton(
                Material2MZ.PLAY_CIRCLE_OUTLINE,
                Material2MZ.PAUSE_CIRCLE_OUTLINE,
                "playback-playpause-icon",
                model.isPlayingProperty(),
                e -> onPlay.run());

        Button openInSystemBtn = ButtonWidgets.actionIconButton(Material2AL.LOCAL_PLAY, "playback-local-icon", event -> {
            openInSystem.accept(model.filePathProperty().get());
        });

        playbackControls.getChildren().addAll(play, openInSystemBtn);
        playbackControls.setAlignment(Pos.CENTER);


        results.getChildren().add(playbackControls);

        return results;
    }

    private Region createExtraControls() {
        HBox results = new HBox(5);

        Button volume = ButtonWidgets.toggleableActionIconButton(
                Material2MZ.VOLUME_UP,
                Material2MZ.VOLUME_MUTE,
                "playback-volume-icon",
                model.isMutedProperty(),
                e -> onMute.run());

        volume.getStyleClass().add("playback-volume-icon");

        ProgressSlider volumeSlider = new ProgressSlider();
        volumeSlider.getStyleClass().add("volume-slider");
        volumeSlider.setPrefWidth(100);

        volumeSlider.valueProperty().bindBidirectional(model.volumeProperty());

        volumeSlider.valueProperty().addListener((obs, ov, nv) -> changeVolume.accept(nv.doubleValue()));

        Button fullScreenIcon = ButtonWidgets.toggleableActionIconButton(
                Material2MZ.OPEN_IN_FULL,
                Material2AL.CLOSE_FULLSCREEN,
                "playback-fullscreen-icon",
                model.isFullscreenProperty(),
                e -> onFulScreen.run()
        );


        results.getChildren().addAll(volume, volumeSlider, fullScreenIcon);
        results.setAlignment(Pos.CENTER_RIGHT);
        results.setPadding(new Insets(5));

        return results;
    }
}
