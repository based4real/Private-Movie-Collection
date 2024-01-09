package pmc.gui.widgets;

import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class NavigationGroup {
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private final VBox container = new VBox();

    public NavigationGroup(String style) {
        container.getStyleClass().add(style);
    }

    public void add(NavigationButton button, BooleanProperty selectedProperty) {
        toggleGroup.getToggles().add(button);
        selectedProperty.bind(button.selectedProperty());

        // Bruges til at konsumere MOUSE_PRESSED event på ToggleButtons så en knap ikke kan blive deselected.
        EventHandler<MouseEvent> consumeMouseEvent = event -> {
            if (((ToggleButton) event.getSource()).isSelected()) {
                event.consume();
            }
        };

        // Bruges til at konsumere KEY_PRESSED event på ToggleButtons så en kan knap ikke kan
        // blive reselected/deselected ved at trykke space
        EventHandler<KeyEvent> consumeKeyPressEvent = event -> {
            if (((ToggleButton) event.getSource()).isSelected() && event.getCode() == KeyCode.SPACE) {
                event.consume();
            }
        };

        button.addEventFilter(MouseEvent.MOUSE_PRESSED, consumeMouseEvent);
        button.addEventFilter(KeyEvent.KEY_PRESSED, consumeKeyPressEvent);

        container.getChildren().add(button);
    }

    public Region getView() {
        return container;
    }
}
