package pmc.gui.components.info;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.util.Builder;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.gui.common.MovieModel;
import pmc.gui.components.playback.PlaybackHandler;
import pmc.gui.components.playback.PlaybackModel;
import pmc.gui.components.pmc.ViewType;
import pmc.gui.widgets.ImageWidgets;
import pmc.gui.widgets.TextWidgets;
import pmc.gui.widgets.buttons.ButtonWidgets;


import java.awt.*;
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
        HBox results = new HBox();
        results.setPadding(new Insets(5, 0, 0, PADDING));

        ImageView poster = ImageWidgets.boundRoundedImage(model.posterPathProperty(), 220, 340, 10);

        results.getChildren().addAll(poster, createInfoBox());

        return results;
    }

    private Region createInfoBox() {
        VBox results = new VBox(0);
        results.setPadding(new Insets(0, PADDING, 0, PADDING * 2));

        Text title = TextWidgets.styledText(model.titleProperty(), "info-header");
        title.setWrappingWidth(800);

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

        Text description = TextWidgets.styledText(model.descriptionProperty(), "info-description");
        description.setWrappingWidth(450);

        // Mellemrum mellem knapper og beskrivelse.
        VBox.setMargin(description, new Insets(15, 0, 0, 0));

        results.getChildren().addAll(title, director, other, genres, createMenuButtons(), description);

        return results;
    }

    private HBox createMenuButtons() {
        HBox options = new HBox(PADDING);

        Button play = ButtonWidgets.actionButtonStyle("Play", "info-play", e -> buttonPlay());

        play.setOnMouseClicked(e -> {
            playMovieHandler.accept(model.getMovieModel());
            e.consume();
        });

        options.getChildren().addAll(play);
        return options;
    }

    private void buttonGenre(TMDBGenreEntity genre) {
        System.out.println(genre.getName());
    }

    private void buttonPlay() {
       // model.playMovieHandler().
    }
}
