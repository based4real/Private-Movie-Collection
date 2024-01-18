package pmc.gui.components.playback;

import javafx.beans.property.*;
import javafx.scene.media.MediaPlayer;

public class PlaybackModel {
    private final ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<>();
    private final StringProperty filePath = new SimpleStringProperty("");
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);
    private SimpleDoubleProperty currenTime = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty totalDuration = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty currentPosition = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty volume = new SimpleDoubleProperty(0.0);
    private SimpleBooleanProperty isMuted = new SimpleBooleanProperty(false);

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
    public StringProperty filePathProperty() { return filePath; }
    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public BooleanProperty isLoadingProperty() {
        return isLoading;
    }

    public SimpleDoubleProperty currenTimeProperty() {
        return currenTime;
    }

    public SimpleDoubleProperty totalDurationProperty() {
        return totalDuration;
    }

    public SimpleDoubleProperty currentPositionProperty() {
        return currentPosition;
    }

    public SimpleDoubleProperty volumeProperty() {
        return volume;
    }

    public SimpleBooleanProperty isMutedProperty() {
        return isMuted;
    }
}
