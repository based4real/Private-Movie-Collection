package pmc.gui.widgets;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class IconWidgets {
    public static FontIcon styledIcon(Ikon iconCode, String style) {
        FontIcon results = new FontIcon(iconCode);
        results.getStyleClass().add(style);
        return results;
    }

    public static FontIcon styledIcon(Ikon iconCode, String... styles) {
        FontIcon results = new FontIcon(iconCode);
        results.getStyleClass().addAll(styles);
        return results;
    }
}
