package pmc.gui.components.playback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;

import java.util.function.Consumer;

public class PlaybackController implements IViewController {
    private final PlaybackModel model;
    private PlaybackHandler playbackHandler;
    private final Builder<Region> viewBuilder;

    public PlaybackController(Runnable goBackHandler) {
        this.model = new PlaybackModel();
        this.playbackHandler = new PlaybackHandler(model, "C:\\Plex\\Film\\The.Shawshank.Redemption.1994.1080p.x264.YIFY.mp4");
        this.viewBuilder = new PlaybackViewBuilder(model, playbackHandler.getMediaPlayer(), this::handlePlay, goBackHandler);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    public void handlePlay() {
        playbackHandler.play();
    }
}