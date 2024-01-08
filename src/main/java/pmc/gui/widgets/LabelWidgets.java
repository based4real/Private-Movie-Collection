package pmc.gui.widgets;

import javafx.scene.control.Label;

public class LabelWidgets {
    public static Label styledLabel(String text, String style) {
        Label results = new Label(text);
        results.getStyleClass().add(style);
        return results;
    }
}
