package pmc.gui.components.playback;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;

import java.util.function.Consumer;

public class PlaybackController implements IViewController {
    private final PlaybackModel model;
    private final Builder<Region> viewBuilder;

    public PlaybackController(Runnable goBackHandler) {
        this.model = new PlaybackModel();
        this.viewBuilder = new PlaybackViewBuilder(model, goBackHandler);
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}