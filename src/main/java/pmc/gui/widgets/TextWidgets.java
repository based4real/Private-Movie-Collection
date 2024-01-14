package pmc.gui.widgets;

import javafx.scene.text.Text;

public class TextWidgets {
    public static Text styledText(String text, String style) {
        Text results = new Text(text);
        results.getStyleClass().add(style);
        return results;
    }

    public static Text styledText(String text, String... styles) {
        Text results = new Text(text);
        results.getStyleClass().addAll(styles);
        return results;
    }

}
