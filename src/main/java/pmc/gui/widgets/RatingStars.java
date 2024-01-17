package pmc.gui.widgets;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import pmc.gui.widgets.icons.IconWidgets;

public class RatingStars {

    private int selectedStars = 0;
    private int maxStars = 0;

    private final Ikon STAR_OUTLINE = FontAwesomeRegular.STAR;
    private final Ikon STAR_SOLID = FontAwesomeSolid.STAR;

    private HBox ratingBox;

    public RatingStars(int stars) {
        this.maxStars = stars;
    }

    public HBox createRatingBox() {
        ratingBox = new HBox(5);

        for (int i = 1; i <= maxStars; i++) {
            FontIcon star = createStar(i);

            ratingBox.getChildren().add(star);
        }

        return ratingBox;
    }

    private FontIcon createStar(int i) {
        FontIcon star = IconWidgets.styledIcon(STAR_OUTLINE, "info-rating-star");

        star.setOnMouseEntered(event -> highlightStars(event, i));
        star.setOnMouseExited(event -> highlightStars(event, selectedStars));
        star.setOnMouseClicked(event -> selectStars(event, i));

        return star;
    }

    private void selectStars(MouseEvent event, int i) {
        if (i == selectedStars) {
            selectedStars = 0;
            return;
        }

        selectedStars = i;
    }

    private void setStarHover(FontIcon star) {
        star.getStyleClass().clear();
        star.getStyleClass().add("info-rating-star-hover");
        star.setIconCode(STAR_SOLID);
    }

    private void setStarExit(FontIcon star) {
        star.getStyleClass().clear();
        star.getStyleClass().add("info-rating-star");
        star.setIconCode(STAR_OUTLINE);
    }
    private void highlightStars(MouseEvent event, int numStars) {
        ObservableList<Node> children = ratingBox.getChildren();
        for (int i = 1; i <= maxStars; i++) {
            FontIcon star = (FontIcon) children.get(i - 1);
            if (i <= numStars) {
                setStarHover(star);
            } else {
                setStarExit(star);
            }
        }
    }







}
