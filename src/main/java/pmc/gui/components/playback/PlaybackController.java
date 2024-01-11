package pmc.gui.components.playback;

import javafx.scene.layout.Region;
import javafx.util.Builder;
import pmc.gui.common.IViewController;

public class PlaybackController implements IViewController {
    private final PlaybackModel model;
    private final Builder<Region> viewBuilder;

    public PlaybackController() {
        this.model = new PlaybackModel();
        this.viewBuilder = new PlaybackViewBuilder(model);

    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }
}