package pmc.gui.components.info;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.kordamp.ikonli.javafx.FontIcon;
import pmc.be.rest.tmdb.TMDBCreditEntity;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.gui.common.MovieModel;
import pmc.gui.widgets.ImageWidgets;
import pmc.gui.widgets.LabelWidgets;
import pmc.gui.widgets.TextWidgets;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.controls.HorizontalPaginator;
import pmc.gui.widgets.icons.IconWidgets;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class InfoViewBuilder implements Builder<Region> {
    private final InfoModel model;
    private final static int PADDING = 20;
    private Consumer<MovieModel> playMovieHandler;

    public InfoViewBuilder(InfoModel model, Consumer<MovieModel> playMovieHandler) {
        this.model = model;
        this.playMovieHandler = playMovieHandler;
    }

    @Override
    public Region build() {
        VBox results = new VBox();

        results.getChildren().addAll(infoHbox(), createCreditPaginator());
        return results;
    }

    private HBox infoHbox() {
        HBox results = new HBox();
        results.setPadding(new Insets(5, 0, 0, PADDING));

        ImageView poster = ImageWidgets.boundRoundedImage(model.posterPathProperty(), 220, 340, 10);

        results.getChildren().addAll(poster, createInfoBox());
        return results;
    }

    private Region createInfoBox() {
        VBox results = new VBox(0);
        results.setPadding(new Insets(0, PADDING, 0, PADDING * 2));

        Label title = LabelWidgets.styledLabel(model.titleProperty(), "info-header");

        Text director = TextWidgets.styledText(model.directorProperty(), "info-director");

        // Mellemrum mellem release og director.
        VBox.setMargin(director, new Insets(0, 0, PADDING, 0));

        HBox other = new HBox(PADDING);

        Text release = TextWidgets.styledText(model.releaseProperty(), "info-others");
        Text runtime = TextWidgets.styledText(model.runtimeProperty(), "info-others");
        Text rated = TextWidgets.styledText(model.ratedProperty(), "info-others");

        other.getChildren().addAll(release, runtime, rated);

        HBox genres = new HBox(5);

        // Mellemrum mellem genre og others.
        VBox.setMargin(genres, new Insets(10, 0, PADDING * 2, 0));

        model.genresProperty().addListener((ListChangeListener.Change<? extends TMDBGenreEntity> change) -> {
            genres.getChildren().clear();
            for (TMDBGenreEntity genre : model.genresProperty()) {
                Text genreText = TextWidgets.styledText(genre.getName(), "info-genre");
                genreText.setOnMouseClicked(event -> buttonGenre(genre));
                genres.getChildren().add(genreText);
            }
        });

        Label description = LabelWidgets.styledLabel(model.descriptionProperty(), "info-description");
        description.setWrapText(true);
        description.setMaxWidth(450);
        description.setMaxHeight(115);

        // Mellemrum mellem knapper og beskrivelse.
        VBox.setMargin(description, new Insets(15, 0, 0, 0));

        results.getChildren().addAll(title, director, other, genres, createMenuButtons(), description);

        return results;
    }

    private HBox createMenuButtons() {
        HBox options = new HBox(PADDING);

        Button play = ButtonWidgets.actionButtonStyle("Play", "info-play", e -> playVideo());

        options.getChildren().addAll(play);
        return options;
    }

    private void playVideo() {
        playMovieHandler.accept(model.getMovieModel());
    }

    private HorizontalPaginator<TMDBCreditEntity> createCreditPaginator() {
        HorizontalPaginator credits = new HorizontalPaginator<>(
                model.creditsPropertyMaxResults(20),
                credit -> {
                        VBox vBox = new VBox();

                        String img = credit.getImage() == null ? "https://image.tmdb.org/t/p/original/vgjhFQ3cixrl0VbChoB8V29ozlc.jpg" : credit.getImage();
                        ImageView imageView = ImageWidgets.scaledRoundedImage(img, 125, 125, 125);

                        Text characterName = TextWidgets.styledText(credit.getCharacterName(), "info-cast-name");
                        Text name = TextWidgets.styledText(credit.getName(), "info-cast-name");

                        vBox.alignmentProperty().set(Pos.BASELINE_CENTER);
                        vBox.getChildren().addAll(imageView, characterName, name);
                        return vBox;
                },
                "Cast & Crew"
        );

        credits.setPadding(new Insets(10, 0, 0, 10));
        return credits;
    }

    private void buttonGenre(TMDBGenreEntity genre) {

    }

}
