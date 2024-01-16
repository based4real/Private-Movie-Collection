package pmc.gui.widgets;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
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

    public static ImageView boundRoundedImage(StringProperty urlProperty, double fitWidth, double fitHeight, double rounding) {
        ImageView results = scaledRoundedImage("file:" + urlProperty.get(), fitWidth, fitHeight, rounding);

        urlProperty.addListener((obs, ov, nv) -> results.setImage(new Image("file:" + nv, true)));

        return results;
    }

    public static ImageView observableBoundRoundedImage(ObservableStringValue urlProperty, double fitWidth, double fitHeight, double rounding) {
        ImageView results = scaledRoundedImage("file:" + urlProperty.get(), fitWidth, fitHeight, rounding);

        urlProperty.addListener((obs, ov, nv) -> {
            if (!nv.isEmpty() && nv != null) results.setImage(new Image(nv, true));
        });

        return results;
    }

    public static Background stretchedBackground(String url) {
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true);
        Image image = new Image(url, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, null, null, null, backgroundSize);
        return new Background(backgroundImage);
    }
}