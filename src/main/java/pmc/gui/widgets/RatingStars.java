package pmc.gui.widgets;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MovieUpdate;
import pmc.gui.widgets.icons.IconWidgets;

import java.util.function.Consumer;

public class RatingStars {

    private static int maxStars = 0;

    private final static Ikon STAR_OUTLINE = FontAwesomeRegular.STAR;
    private final static Ikon STAR_SOLID = FontAwesomeSolid.STAR;

    private static HBox ratingBox;
    private static Consumer<MovieUpdate> movieUpdate;

    private MovieModel cachedModel;
    private MovieModel movieModel;

    public RatingStars(int stars, Consumer<MovieUpdate> movieUpdateHandle) {
        maxStars = stars;
        movieUpdate = movieUpdateHandle;
    }

    public HBox createRatingBox() {
        ratingBox = new HBox(5);

        for (int i = 1; i <= maxStars; i++) {
            FontIcon star = createStar(i);

            ratingBox.getChildren().add(star);
        }

        return ratingBox;
    }

    public void setMovieModel(MovieModel model) {
        movieModel = model;
        cachedModel = model;
    }

    public void setStars(int amount) {
        highlightStars(amount);
    }


    private FontIcon createStar(int i) {
        FontIcon star = IconWidgets.styledIcon(STAR_OUTLINE, "info-rating-star");

        star.setOnMouseEntered(event -> highlightStars(i));
        star.setOnMouseExited(event -> highlightStars(movieModel.personalRatingProperty().get()));
        star.setOnMouseClicked(event -> selectStars(i));

        return star;
    }

    private void setMovieUpdate(int stars) {
        cachedModel.personalRatingProperty().set(stars);
        movieUpdate.accept(new MovieUpdate(movieModel, cachedModel));

        movieModel.personalRatingProperty().set(stars);
    }

    private void selectStars(int i) {
        if (movieModel == null)
            return;

        if (i == movieModel.personalRatingProperty().get()) {
            setMovieUpdate(0);
            return;
        }

        setMovieUpdate(i);
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
    private void highlightStars(int numStars) {
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
