package pmc.gui.components.info;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Builder;
import javafx.util.Duration;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.javafx.FontIcon;
import pmc.be.rest.tmdb.TMDBCreditEntity;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.be.rest.tmdb.TMDBVideoEntity;
import pmc.gui.common.MovieModel;
import pmc.gui.common.MovieUpdate;
import pmc.gui.common.MoviesData;
import pmc.gui.components.genres.GenresModel;
import pmc.gui.utils.ErrorHandler;
import pmc.gui.utils.StringHandler;
import pmc.gui.utils.Webbrowser;
import pmc.gui.widgets.*;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.controls.HorizontalPaginator;
import pmc.gui.widgets.icons.IconWidgets;
import pmc.gui.widgets.icons.ToggleableIcon;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class InfoViewBuilder implements Builder<Region> {
    private final InfoModel model;
    private final static int PADDING = 20;

    private Consumer<MovieModel> playMovieHandler;
    private Consumer<MoviesData> viewChangehandler;
    private Consumer<MovieUpdate> movieUpdate;

    public InfoViewBuilder(InfoModel model,
                           Consumer<MovieModel> playMovieHandler,
                           Consumer<MoviesData> viewChangehandler,
                           Consumer<MovieUpdate> movieUpdate) {
        this.model = model;
        this.playMovieHandler = playMovieHandler;
        this.viewChangehandler = viewChangehandler;
        this.movieUpdate = movieUpdate;
    }

    @Override
    public Region build() {
        VBox results = new VBox();
        results.getChildren().addAll(infoHbox(), createCreditPaginator(), createExternalLinks());

        ScrollPane scrollPane = ScrollPaneWidgets.defaultPageScrollPane(results);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    private HBox infoHbox() {
        HBox results = new HBox();
        results.setPadding(new Insets(5, 0, 0, PADDING));
        
        results.getChildren().addAll(createPoster(), createInfoBox());
        return results;
    }

    private StackPane createPoster() {
        StackPane results = new StackPane();
        results.getStyleClass().add("info-poster");

        ImageView poster = ImageWidgets.boundRoundedImage(model.posterPathProperty(), 220, 340, 10);

        Rectangle border = new Rectangle(220, 340);
        border.setArcWidth(10);
        border.setArcHeight(10);
        border.setFill(new Color(0, 0, 0, 0.5));
        border.setStroke(new Color(0.85, 0.57, 0.05, 1.0));
        border.setStrokeWidth(1);

        ToggleableIcon playButton = IconWidgets.posterPlayButton(50);

        border.setVisible(false);
        playButton.setVisible(false);

        results.setOnMouseEntered(e -> {
            border.visibleProperty().set(true);
            playButton.visibleProperty().set(true);
        });

        results.setOnMouseExited(e -> {
            border.visibleProperty().set(false);
            playButton.visibleProperty().set(false);
        });

        results.setOnMouseClicked(e -> playMovieHandler.accept(model.movieModelProperty().get()));

        results.getChildren().addAll(poster, border, playButton);
        return results;
    }

    private HBox genres;

    private Region createInfoBox() {
        VBox results = new VBox(0);
        results.setPadding(new Insets(0, PADDING, 0, PADDING * 2));

        HBox director = createDirectorText();

        // Mellemrum mellem release og director.
        VBox.setMargin(director, new Insets(0, 0, PADDING, 0));

        HBox other = new HBox(PADDING);

        Text release = TextWidgets.styledText(model.releaseProperty(), "info-others");
        Text runtime = TextWidgets.styledText(model.runtimeProperty(), "info-others");
        Text rated = TextWidgets.styledText(model.ratedProperty(), "info-others");

        other.getChildren().addAll(release, runtime, rated);

        genres = createGenreText(2);

        // Mellemrum mellem genre og IMDB.
        VBox.setMargin(other, new Insets(0, 0, 5, 0));

        HBox imdb = createIMDBText();
        imdb.getChildren().add(createRating());

        // Mellemrum mellem IMDB og knapper.
        VBox.setMargin(imdb, new Insets(PADDING, 0, PADDING * 2, 0));

        Label description = LabelWidgets.styledLabel(model.descriptionProperty(), "info-description");
        description.setWrapText(true);
        description.setMaxWidth(450);
        description.setMaxHeight(115);

        // Mellemrum mellem knapper og beskrivelse.
        VBox.setMargin(description, new Insets(15, 0, 0, 0));

        results.getChildren().addAll(createTitleText(), director, other, genres, imdb, createMenuButtons(), description);
        return results;
    }

    private HBox createRating() {
        RatingStars ratingStars = new RatingStars(6, movieUpdate);
        HBox rating = ratingStars.createRatingBox();
        rating.setPadding(new Insets(7, 0, 0, 10));

        model.movieModelProperty().addListener((observable, oldValue, newValue) -> {
            ratingStars.setMovieModel(newValue);
            ratingStars.setStars(model.movieModelProperty().get().personalRatingProperty().get());
        });

        return rating;
    }

    private HBox createGenreText(int max) {
        genres = new HBox(0);
        model.genresProperty().addListener((ListChangeListener.Change<? extends TMDBGenreEntity> change) -> {
            createGenre(max);
        });

        return genres;
    }

    private void createGenre(int maxGenresToshow) {
        genres.getChildren().clear();

        ObservableList<GenresModel> genreModels = model.getGenreModels();
        int genreModelsSize = genreModels.size();

        Label andMoreLabel = LabelWidgets.styledLabel("og flere", "info-genre");
        andMoreLabel.setOnMouseClicked(event -> createGenre(genreModelsSize));

        for (int i = 0; i < Math.min(maxGenresToshow, genreModelsSize); i++) {
            GenresModel genreModel = genreModels.get(i);
            Label genreText = LabelWidgets.styledLabel(genreModel.nameProperty(), "info-genre");
            genreText.setOnMouseClicked(event -> buttonGenre(genreModel));
            genres.getChildren().add(genreText);

            // Tilføjer komma
            if (i < Math.min(maxGenresToshow, genreModelsSize) - 1) {
                Label commaLabel = LabelWidgets.styledLabel(",", "info-genre");
                genres.getChildren().add(commaLabel);
                HBox.setMargin(commaLabel, new Insets(0, 5, 0, 0));
            }
        }

        // Hvis der er mere end 2 genre, så tilføj "og flere" label
        if (genreModelsSize > maxGenresToshow) {
            HBox.setMargin(andMoreLabel, new Insets(0, 0, 0, 5));
            genres.getChildren().add(andMoreLabel);
        }
    }

    private HBox createIMDBText() {
        HBox imdb = new HBox(10);
        FontIcon imdbIcon = IconWidgets.styledIcon(FontAwesomeBrands.IMDB, "add-imdb-icon");

        Label imdbRating = LabelWidgets.styledLabel("5.0", "info-imdb-rating");
        imdbRating.setPadding(new Insets(5,0,0,0));

        model.movieModelProperty().addListener((observable, oldValue, newValue) -> {
            imdbRating.setText(String.valueOf(newValue.imdbRatingProperty().get()));
        });

        imdb.getChildren().setAll(imdbIcon, imdbRating);

        return imdb;
    }

    private HBox createDirectorText() {
        HBox hBox = new HBox();

        Label directedBy = LabelWidgets.styledLabel("Instrueret af ", "info-directored");
        Label director = LabelWidgets.styledLabel("name", "info-director");
        model.creditsProperty().addListener((ListChangeListener.Change<? extends TMDBCreditEntity> change) -> {
            if (model.getDirector() != null)
                director.setText(model.getDirector().get(0).getName());
        });

        createToolTip(director, "Åben i TMDB");
        director.setOnMouseClicked(event -> personClick(model.getDirector().get(0)));

        hBox.getChildren().addAll(directedBy, director);
        return hBox;
    }

    private Label createTitleText() {
        Label title = LabelWidgets.styledLabel(model.titleProperty(), "info-header");
        title.setOnMouseClicked(event -> titleClick());
        createToolTip(title, "Åben i TMDB");
        return title;
    }

    private HBox createMenuButtons() {
        HBox options = new HBox(PADDING);

        Button play = ButtonWidgets.actionButtonStyle("Play", "info-play", e -> playVideo());

        options.getChildren().addAll(play);
        return options;
    }

    private void createToolTip(Node val, String txt) {
        Tooltip tooltip = new Tooltip(txt);
        tooltip.setShowDelay(new Duration(500));
        tooltip.getStyleClass().add("info-tooltip");
        Tooltip.install(val, tooltip);
    }

    private void playVideo() {
        playMovieHandler.accept(model.movieModelProperty().get());
    }

    private HorizontalPaginator<TMDBCreditEntity> createCreditPaginator() {
        HorizontalPaginator credits = new HorizontalPaginator<>(
                model.creditsPropertyMaxResults(20),
                credit -> {
                        VBox vBox = new VBox();
                        Node val = CreditPicture.render(credit, 125);

                        Text name = TextWidgets.styledText(StringHandler.trimName(credit.getName(), 15), "info-name");
                        Text characterName = TextWidgets.styledText(StringHandler.trimName(credit.getCharacterName(), 15), "info-cast-name");

                        createToolTip(name, "Åben i TMDB");

                        name.setOnMouseClicked(event -> personClick(credit));

                        vBox.alignmentProperty().set(Pos.BASELINE_CENTER);
                        vBox.getChildren().addAll(val, name, characterName);
                        return vBox;
                },
                "Cast & Crew"
        );

        credits.setPadding(new Insets(10, 0, 0, 15));
        credits.setMinHeight(250); //todo: bedre løsning
        return credits;
    }

    private HorizontalPaginator<TMDBVideoEntity> createExternalLinks() {
        HorizontalPaginator credits = new HorizontalPaginator<>(
                model.videosProperty(),
                video -> {
                    VBox results = new VBox(5);

                    HBox details = new HBox(5);
                    details.getStyleClass().add("info-external-links");

                    //Jaer...
                    details.setMinWidth(300);
                    details.setMaxWidth(300);

                    Label title = LabelWidgets.styledLabel(video.getName(), "info-youtube-title");

                    StackPane stackPane = new StackPane();
                    Rectangle square = new Rectangle(25, 25);
                    square.setFill(Color.WHITE);

                    FontIcon youtubeIcon = IconWidgets.styledIcon(FontAwesomeBrands.YOUTUBE, "info-youtube-icon");

                    stackPane.getChildren().addAll(square, youtubeIcon);

                    Label type = LabelWidgets.styledLabel(video.getType(), "info-youtube-type");
                    results.getChildren().addAll(type, title);
                    results.setAlignment(Pos.CENTER_LEFT);

                    details.getChildren().addAll(stackPane, results);
                    details.setAlignment(Pos.CENTER_LEFT);
                    details.setOnMouseClicked(event -> detailsClick(video));

                    createToolTip(details, "Se på youtube");

                    return details;
                },
                "Eksterne links"
        );
        credits.setPadding(new Insets(10, 0, 0, 15));
        credits.setMinHeight(250); //todo: bedre løsning
        return credits;

    }

    private void detailsClick(TMDBVideoEntity video) {
        Webbrowser.openURL(video.getYoutubeUrl());
    }

    private void titleClick() {
        Webbrowser.openTMDBInfo(model.movieModelProperty().get().tmdbIdProperty().get());
    }


    private void personClick(TMDBCreditEntity credit) {
        Webbrowser.openTMDBCredits(credit.getID());
    }

    private void buttonGenre(GenresModel genreModel) {
        this.viewChangehandler.accept(new MoviesData(genreModel.nameProperty().get(), genreModel.getMovies()));
    }

}
