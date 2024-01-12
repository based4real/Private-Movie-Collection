package pmc.gui.components.playback;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class PlaybackHandler {
    private final PlaybackModel model;
    private MediaPlayer mediaPlayer;

    public PlaybackHandler(PlaybackModel model) {
        this.model = model;
        initializeMediaPlayer(model.filePathProperty().get());

        model.filePathProperty().addListener((obs, ov, nv) -> {
            if (nv != null && !nv.isEmpty()) initializeMediaPlayer(nv);
        });
    }

    private void initializeMediaPlayer(String filePath) {
        if (mediaPlayer != null) mediaPlayer.dispose();

        if (filePath == null || filePath.isEmpty()) {
            model.mediaPlayerProperty().set(null);
            return;
        }

        try {
            Media media = new Media(new File(filePath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            model.mediaPlayerProperty().set(mediaPlayer);
        } catch (Exception e) {
            System.out.println("Fejl i PlaybackHandler.initializeMediaPlayer(): " + e.getMessage());
            mediaPlayer = null;
            model.mediaPlayerProperty().set(null);
            // todo: håndter exception ordentligt
        }
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
