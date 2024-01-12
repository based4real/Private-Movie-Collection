package pmc.gui.components.playback;

import javafx.beans.property.*;
import javafx.scene.media.MediaPlayer;

public class PlaybackModel {
    private final ObjectProperty<MediaPlayer> mediaPlayer = new SimpleObjectProperty<>();
    private final StringProperty filePath = new SimpleStringProperty("");
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);

    public ObjectProperty mediaPlayerProperty() { return mediaPlayer; }
    public StringProperty filePathProperty() { return filePath; }
    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }
}
