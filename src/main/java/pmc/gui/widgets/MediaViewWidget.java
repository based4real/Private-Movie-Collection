package pmc.gui.widgets;

import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class MediaViewWidget extends Region {
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    public MediaViewWidget(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        this.mediaView = new MediaView(mediaPlayer);

        mediaView.setPreserveRatio(true);

        this.getChildren().add(mediaView);
    }

    public void setMediaPlayer(MediaPlayer newMediaPlayer) {
        if (this.mediaPlayer != null) {
            this.mediaPlayer.dispose();
        }

        this.mediaPlayer = newMediaPlayer;

        // Remove the old MediaView and create a new one
        this.getChildren().clear();
        this.mediaView = new MediaView(newMediaPlayer);
        mediaView.setPreserveRatio(true);
        this.getChildren().add(mediaView);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        if (mediaPlayer == null || mediaPlayer.getMedia() == null) return;

        // max størrelse
        double width = this.getWidth();
        double height = this.getHeight();
        double aspectRatio = mediaPlayer.getMedia().getWidth() / (double) mediaPlayer.getMedia().getHeight();

        double fitHeight = Math.min(height, width / aspectRatio);
        double fitWidth = fitHeight * aspectRatio;

        mediaView.setFitWidth(fitWidth);
        mediaView.setFitHeight(fitHeight);
        mediaView.relocate((width - fitWidth) / 2, (height - fitHeight) / 2); // centrer
    }
}
