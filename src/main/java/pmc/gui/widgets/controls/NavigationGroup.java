package pmc.gui.widgets.controls;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.Ikon;
import pmc.gui.components.pmc.ViewType;

import javax.swing.text.View;
import java.util.HashMap;
import java.util.Map;

public class NavigationGroup {
    private final VBox container = new VBox();
    private final ObjectProperty<ViewType> activeView;

    public NavigationGroup(String style, ObjectProperty<ViewType> activeView) {
        container.getStyleClass().add(style);
        this.activeView = activeView;
    }

    public void add(Ikon iconCode, String text, ViewType viewType) {
        NavigationButton button = new NavigationButton(iconCode, text, activeView, viewType);
        button.setOnAction(e -> activeView.set(viewType));
        container.getChildren().add(button);
    }

    public Region getView() {
        return container;
    }
}
