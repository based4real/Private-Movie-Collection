package pmc.gui.widgets.icons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

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

    public static ToggleableIcon posterPlayButton(float radius) {
        BooleanProperty hoverProperty = new SimpleBooleanProperty(false);

        ToggleableIcon results = new ToggleableIcon(
                Material2MZ.PLAY_CIRCLE_OUTLINE,
                Material2MZ.PLAY_CIRCLE_FILLED,
                "poster-play-button",
                hoverProperty
        );

        results.setOnMouseEntered(e -> {
            hoverProperty.set(true);
        });

        results.setOnMouseExited(e -> {
            hoverProperty.set(false);
        });

        return results;
    }
}
