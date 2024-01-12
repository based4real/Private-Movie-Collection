package pmc.gui.components.playback;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.function.Consumer;

public class PlaybackHandler {
    private final PlaybackModel model;
    private MediaPlayer mediaPlayer;

    public PlaybackHandler(PlaybackModel model) {
        this.model = model;
    }

    public void initializeMediaPlayerAsync(String filePath, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                Media media = new Media(new File(filePath).toURI().toString());
                MediaPlayer newMediaPlayer = new MediaPlayer(media);

                Platform.runLater(() -> {
                    if (mediaPlayer != null) mediaPlayer.dispose();

                    mediaPlayer = newMediaPlayer;
                    model.mediaPlayerProperty().set(mediaPlayer);
                });
            } catch (Exception e) {
                Platform.runLater(() -> onError.accept(e));
            }
        }).start();
    }

    public void play() {
        mediaPlayer.play();
        model.isPlayingProperty().set(true);
    }

    public void pause() {
        mediaPlayer.pause();
        model.isPlayingProperty().set(false);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
