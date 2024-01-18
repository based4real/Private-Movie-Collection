package pmc.gui.components.playback;

import javafx.beans.property.*;
import javafx.scene.media.MediaPlayer;

public class PlaybackModel {
    private final ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<>();
    private final StringProperty filePath = new SimpleStringProperty("");
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final BooleanProperty isLoading = new SimpleBooleanProperty(false);

    public void reset() {
        if (mediaPlayer.get() != null) {
            mediaPlayer.get().stop();
            mediaPlayer.get().dispose();
            mediaPlayer.set(null);
        }

        filePath.set("");
        isPlaying.set(false);
    }

    public ObjectProperty<MediaPlayer> mediaPlayerProperty() { return mediaPlayer; }
    public StringProperty filePathProperty() { return filePath; }
    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public BooleanProperty isLoadingProperty() {
        return isLoading;
    }
}
