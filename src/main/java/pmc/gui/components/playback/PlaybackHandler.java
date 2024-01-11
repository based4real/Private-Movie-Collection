package pmc.gui.components.playback;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class PlaybackHandler {
    private final PlaybackModel model;
    private MediaPlayer mediaPlayer;

    public PlaybackHandler(PlaybackModel model, String mediaPath) {
        this.model = model;
        Media media = new Media(new File(mediaPath).toURI().toString());
        this.mediaPlayer = new MediaPlayer(media);
    }

    public void play() {
        mediaPlayer.play();
        model.isPlayingProperty().set(true);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
