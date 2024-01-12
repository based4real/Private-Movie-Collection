package pmc.gui.widgets.icons;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class ToggleableIcon extends StackPane {
    private final FontIcon icon1;
    private final FontIcon icon2;

    public ToggleableIcon(Ikon iconCode1, Ikon iconCode2, String style, BooleanProperty toggleProperty) {
        icon1 = IconWidgets.styledIcon(iconCode1, style);
        icon2 = IconWidgets.styledIcon(iconCode2, style);
        icon2.visibleProperty().bind(toggleProperty);
        icon1.visibleProperty().bind(toggleProperty.not());

        this.getChildren().addAll(icon1, icon2);

        this.sceneProperty().addListener((obs, ov, nv) -> {
            if (nv != null) adjustSizeBasedOnIcons();
        });
    }

    private void adjustSizeBasedOnIcons() {
        Bounds bounds1 = icon1.getLayoutBounds();
        Bounds bounds2 = icon2.getLayoutBounds();
        double maxWidth = Math.max(bounds1.getWidth(), bounds2.getWidth());
        double maxHeight = Math.max(bounds1.getHeight(), bounds2.getHeight());

        double size = Math.max(maxWidth, maxHeight);
        this.setPrefSize(size, size);
        this.setMaxSize(size, size);

        Circle clip = new Circle();
        clip.radiusProperty().bind(Bindings.max(this.widthProperty(), this.heightProperty()).divide(2));
        clip.centerXProperty().bind(this.widthProperty().divide(2));
        clip.centerYProperty().bind(this.heightProperty().divide(2));
        this.setClip(clip);
    }
}
