package pmc.gui.widgets;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

// todo: refactor
public class PosterPlayButton extends StackPane {
    public PosterPlayButton(int radius) {
        super();
        FontIcon icon = new FontIcon(Material2MZ.PLAY_CIRCLE_OUTLINE);
        icon.setIconColor(Color.WHITE);
        icon.setIconSize(radius);

        FontIcon iconHovered = new FontIcon(Material2MZ.PLAY_CIRCLE_FILLED);
        iconHovered.setIconColor(new Color(0.85, 0.57, 0.05, 1.0));
        iconHovered.setIconSize(radius);
        iconHovered.setVisible(false);

        Circle clip = new Circle(radius / 2.0, radius / 2.0, radius / 2.0);
        this.setClip(clip);

        this.setPrefSize(radius, radius);
        this.setMaxSize(radius, radius);
        this.getChildren().addAll(icon, iconHovered);

        this.setOnMouseEntered(e -> {
            icon.visibleProperty().set(false);
            iconHovered.visibleProperty().set(true);
        });

        this.setOnMouseExited(e -> {
            icon.visibleProperty().set(true);
            iconHovered.visibleProperty().set(false);
        });
    }
}
