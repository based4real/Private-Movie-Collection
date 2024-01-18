package pmc.gui.components.playback;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.function.Consumer;

public class PlaybackHandler {
    private final PlaybackModel model;
    private MediaPlayer mediaPlayer;
    private double previousVolume = 0.0;

    public PlaybackHandler(PlaybackModel model) {
        this.model = model;
    }

    public void initializeMediaPlayerAsync(String filePath, Consumer<Exception> onError) {
        new Thread(() -> {
            try {
                model.isLoadingProperty().set(true);
                Media media = new Media(new File(filePath).toURI().toString());
                MediaPlayer newMediaPlayer = new MediaPlayer(media);

                Platform.runLater(() -> {
                    if (mediaPlayer != null) mediaPlayer.dispose();

                    mediaPlayer = newMediaPlayer;
                    model.mediaPlayerProperty().set(mediaPlayer);
                    model.isLoadingProperty().set(false);

                    mediaPlayer.setVolume(model.volumeProperty().get() / 100.0);
                    mediaPlayer.setMute(model.isMutedProperty().get());

                    syncMediaPlayerProperties();
                });
            } catch (Exception e) {
                Platform.runLater(() -> onError.accept(e));
                model.isLoadingProperty().set(false);
            }
        }).start();
    }

    private void syncMediaPlayerProperties() {
        mediaPlayer.currentTimeProperty().addListener((obs, ov, nv) -> {
            double currentTime = nv.toSeconds();
            double totalDuration = mediaPlayer.getTotalDuration().toSeconds();
            model.currenTimeProperty().set(currentTime);
            model.currentPositionProperty().set(currentTime / totalDuration);
        });

        model.volumeProperty().addListener((obs, ov, nv) -> mediaPlayer.setVolume(nv.doubleValue() / 100.0));
        model.isMutedProperty().addListener((obs, ov, nv) -> mediaPlayer.setMute(nv));
        mediaPlayer.setOnReady(() -> model.totalDurationProperty().set(mediaPlayer.getMedia().getDuration().toSeconds()));
    }

    public void play() {
        mediaPlayer.play();
        model.isPlayingProperty().set(true);
    }

    public void pause() {
        mediaPlayer.pause();
        model.isPlayingProperty().set(false);
    }

    /**
     * @throws IllegalArgumentException hvis position ikke er et tal mellem 0.0 og 100.0
     */
    public void seek(double position) {
        if (position < 0.0 || position > 100.0) {
            throw new IllegalArgumentException("Position skal være mellem 0.0 and 100.0");
        }

        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.seconds(position * mediaPlayer.getTotalDuration().toSeconds()));
        }
    }

    /**
     * @throws IllegalArgumentException hvis volumen ikke er et tal mellem 0.0 og 100.0
     */
    public void setVolume(double volume) {
        if (volume < 0.0 || volume > 100.0) {
            throw new IllegalArgumentException("Volume skal være en værdi mellem 0.0 and 100.0");
        }

        model.volumeProperty().set(volume);
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume / 100.0);
            model.isMutedProperty().set(volume == 0.0);
        }
    }

    public void mute() {
        if (!model.isMutedProperty().get()) {
            previousVolume = model.volumeProperty().get();
            model.volumeProperty().set(0);
            mediaPlayer.setMute(true);
        }
    }

    public void unmute() {
        if (model.isMutedProperty().get()) {
            model.volumeProperty().set(previousVolume);
            mediaPlayer.setMute(false);
        }
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
