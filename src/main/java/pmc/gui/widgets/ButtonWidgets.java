package pmc.gui.widgets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class ButtonWidgets {
    public static Button actionButton(String text, EventHandler<ActionEvent> actionEventHandler) {
        Button results = new Button(text);
        results.setOnAction(actionEventHandler);
        return results;
    }

    public static Button actionIconButton(Ikon iconCode, String style, EventHandler<ActionEvent> actionEventHandler) {
        Button results = actionButton("", actionEventHandler);
        FontIcon icon = IconWidgets.styledIcon(iconCode, style);

        results.setGraphic(icon);
        results.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border: none;");
        results.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        results.setFocusTraversable(false);

        return results;
    }
}
