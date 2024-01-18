package pmc.gui.components.playback;

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

    public PlaybackController(Runnable goBackHandler, Runnable fullScreen, Consumer<String> openInSystem) {
        this.model = new PlaybackModel();
        this.playbackHandler = new PlaybackHandler(model);
        this.viewBuilder = new PlaybackViewBuilder(
                model,
                this::handlePlay,
                goBackHandler,
                this::handleMute,
                this::changeVolume,
                this::onSeek,
                fullScreen,
                openInSystem
        );
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    public void setModel(MovieModel model) {
        this.model.filePathProperty().set(model.filePathProperty().get());
        this.model.movieTitleProperty().set(model.titleProperty().get());

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

    public void handleMute() {
        if (model.isMutedProperty().get()) {
            playbackHandler.unmute();
        } else {
            playbackHandler.mute();
        }
    }

    public void changeVolume(double value) {
        playbackHandler.setVolume(value);
    }

    public void onSeek(double value) {
        playbackHandler.seek(value);
    }
}