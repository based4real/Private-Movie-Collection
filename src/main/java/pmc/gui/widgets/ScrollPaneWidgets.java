package pmc.gui.widgets;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public class ScrollPaneWidgets {

    /**
     * Bruges til at oprette scrollpane på sider hvor det er nødvendigt.
     * @param value - Den node som scrollpane skal wrappes omkring, f.eks. VBox eller HBox.
     * @return SrollPane
     */
    public static ScrollPane defaultPageScrollPane(Node value) {
        ScrollPane scrollPane = new ScrollPane(value);
        scrollPane.setContent(value);
        scrollPane.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        scrollPane.setMaxHeight(Region.USE_COMPUTED_SIZE); // Set the max height to USE_COMPUTED_SIZE
        scrollPane.getStyleClass().add("scroll-pane");
        value.setStyle("-fx-background-color: #323232");

        return scrollPane;
    }
}
