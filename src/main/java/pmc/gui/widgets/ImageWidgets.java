package pmc.gui.widgets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class ImageWidgets {
    public static ImageView scaledRoundedImage(String url, double fitWidth, double fitHeight, double rounding) {
        ImageView results = scaledImage(url, fitWidth, fitHeight);

        Rectangle clip = new Rectangle(fitWidth, fitHeight);
        clip.setArcWidth(rounding);
        clip.setArcHeight(rounding);
        results.setClip(clip);

        return results;
    }

    public static ImageView scaledImage(String url, double fitWidth, double fitHeight) {
        ImageView results = new ImageView(new Image(url, true));
        results.setFitWidth(fitWidth);
        results.setFitHeight(fitHeight);
        return results;
    }
}