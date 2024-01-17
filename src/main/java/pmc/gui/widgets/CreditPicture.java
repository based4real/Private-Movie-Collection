package pmc.gui.widgets;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import pmc.be.rest.tmdb.TMDBCreditEntity;
import pmc.gui.utils.StringHandler;

public class CreditPicture {

    public static Node render(TMDBCreditEntity credit, int radius) {
        return credit.getImage() == null ? renderCircleWithInitials(credit, radius) : renderImage(credit, radius);
    }

    public static StackPane renderCircleWithInitials(TMDBCreditEntity credit, int radius) {
        Circle circle = new Circle(radius * .5);
        circle.getStyleClass().add("info-circle");

        String name = credit.getName();
        String initials = StringHandler.getFirst(name) + StringHandler.getFirstAfterSpace(name);
        Text text = new Text(initials.toUpperCase());

        text.getStyleClass().add("info-circle-txt");
        return new StackPane(circle, text);
    }

    public static ImageView renderImage(TMDBCreditEntity credit, int radius) {
        String img = credit.getImage();
        ImageView imageView = ImageWidgets.scaledRoundedImage(img, radius, radius, radius);
        imageView.getStyleClass().add("info-circle");
        return imageView;
    }
}
