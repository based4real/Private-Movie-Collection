package pmc.gui.components.playback;

import javafx.beans.property.*;
import javafx.scene.media.MediaPlayer;

public class PlaybackModel {
    private final ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<>();
    private final StringProperty movieTitle = new SimpleStringProperty("");
    private final StringProperty filePath = new SimpleStringProperty("");
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);
    private final BooleanProperty isFullscreen = new SimpleBooleanProperty(false);
    private final DoubleProperty currenTime = new SimpleDoubleProperty(0.0);
    private final DoubleProperty totalDuration = new SimpleDoubleProperty(0.0);
    private final DoubleProperty currentPosition = new SimpleDoubleProperty(0.0);
    private final DoubleProperty volume = new SimpleDoubleProperty(0.0);
    private final BooleanProperty isMuted = new SimpleBooleanProperty(false);

    public void reset() {
        if (mediaPlayer.get() != null) {
            mediaPlayer.get().stop();
            mediaPlayer.get().dispose();
            mediaPlayer.set(null);
        }

        filePath.set("");
        isPlaying.set(false);
        currenTime.set(0.0);
        totalDuration.set(0.0);
        currentPosition.set(0.0);
    }

    public ObjectProperty<MediaPlayer> mediaPlayerProperty() { return mediaPlayer; }

    public StringProperty movieTitleProperty() {
        return movieTitle;
    }

    public StringProperty filePathProperty() { return filePath; }
    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public BooleanProperty isLoadingProperty() {
        return isLoading;
    }

    public BooleanProperty isFullscreenProperty() {
        return isFullscreen;
    }

    public DoubleProperty currenTimeProperty() {
        return currenTime;
    }

    public DoubleProperty totalDurationProperty() {
        return totalDuration;
    }

    public DoubleProperty currentPositionProperty() {
        return currentPosition;
    }

    public DoubleProperty volumeProperty() {
        return volume;
    }

    public BooleanProperty isMutedProperty() {
        return isMuted;
    }
}
