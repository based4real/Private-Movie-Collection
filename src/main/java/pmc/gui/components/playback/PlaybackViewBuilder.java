package pmc.gui.components.playback;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PlaybackViewBuilder implements Builder<Region> {
    private final PlaybackModel model;

    public PlaybackViewBuilder(PlaybackModel model) {
        this.model = model;
    }

    @Override
    public Region build() {
        return new Button("hej fra playback");
    }
}
