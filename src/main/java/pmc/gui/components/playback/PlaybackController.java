package pmc.gui.components.playback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;
import pmc.gui.common.MovieModel;
import pmc.gui.utils.ErrorHandler;

import java.util.function.Consumer;

public class PlaybackController implements IViewController {
    private final PlaybackModel model;
    private PlaybackHandler playbackHandler;
    private final Builder<Region> viewBuilder;

    public PlaybackController(Runnable goBackHandler) {
        this.model = new PlaybackModel();
        this.playbackHandler = new PlaybackHandler(model);
        this.viewBuilder = new PlaybackViewBuilder(model, playbackHandler.getMediaPlayer(), this::handlePlay, goBackHandler);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    public void setModel(MovieModel model) {
        this.model.filePathProperty().set(model.filePathProperty().get());

        playbackHandler.initializeMediaPlayerAsync(
                this.model.filePathProperty().get(),
                this::handleMediaPlayerError
        );
    }

    private void handleMediaPlayerError(Exception error) {
        ErrorHandler.showErrorDialog("Fejl", "Fejl ved at indlæse film: " + error.getMessage());
    }

    public void handlePlay() {
        if (model.isPlayingProperty().get()) playbackHandler.pause();
        else playbackHandler.play();
    }
}