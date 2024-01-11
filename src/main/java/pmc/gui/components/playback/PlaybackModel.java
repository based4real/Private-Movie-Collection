package pmc.gui.components.playback;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class PlaybackModel {
    private final BooleanProperty isPlaying = new SimpleBooleanProperty(false);

    public BooleanProperty isPlayingProperty() {
        return isPlaying;
    }
}
