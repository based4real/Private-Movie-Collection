package pmc.gui.components.dialog.addmovie;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.kordamp.ikonli.fontawesome5.FontAwesomeBrands;
import org.kordamp.ikonli.javafx.FontIcon;
import pmc.gui.widgets.ImageWidgets;
import pmc.gui.widgets.LabelWidgets;
import pmc.gui.widgets.buttons.ButtonWidgets;
import pmc.gui.widgets.icons.IconWidgets;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class AddMovieViewBuilder implements Builder<Region> {
    private final AddMovieModel model;
    private final Consumer<String> searchFileHandler;
    private final EventHandler<ActionEvent> chooseFileHandler;
    private final TextField fileNameTextField;

    public AddMovieViewBuilder(AddMovieModel model, Consumer<String> searchFileHandler, EventHandler<ActionEvent> chooseFileHandler) {
        this.model = model;
        this.searchFileHandler = searchFileHandler;
        this.chooseFileHandler = chooseFileHandler;
        this.fileNameTextField = new TextField();
    }

    @Override
    public Region build() {
        StackPane results = new StackPane();

        Region chooseFileView = chooseFileView();
        Region fileChosenView = fileChosenView();

        chooseFileView.visibleProperty().bind(model.chooseFileViewProperty());
        fileChosenView.visibleProperty().bind(model.fileChosenViewProperty());

        results.getChildren().addAll(chooseFileView, fileChosenView);

        return results;
    }

    public TextField getFileNameTextField() {
        return fileNameTextField;
    }

    public Region chooseFileView() {
        VBox results = new VBox(10);


        Button fileChooser = ButtonWidgets.actionButton("Vælg fil...", chooseFileHandler);

        Button nextButton = new Button("Næste");
        nextButton.disableProperty().bind(model.chosenFileProperty().not());
        nextButton.setOnAction(e -> {
            String enteredFileName = fileNameTextField.getText();
            if (enteredFileName != null && !enteredFileName.isEmpty()) {
                searchFileHandler.accept(enteredFileName);
                model.chooseFileViewProperty().set(false);
                model.fileChosenViewProperty().set(true);
            }
        });

        results.getChildren().addAll(fileNameTextField, fileChooser, nextButton);
        results.setAlignment(Pos.CENTER);

        return results;
    }


    public Region fileChosenView() {
        HBox results = new HBox(10);

        System.out.println(model.posterPathProperty().get());
        VBox posterBox = new VBox(10);
        ImageView poster = ImageWidgets.observableBoundRoundedImage(model.posterUrlProperty(), 150, 244, 10);
        Button backButton = new Button("Tilbage");
        backButton.setOnAction(e -> {
            model.chooseFileViewProperty().set(true);
            model.fileChosenViewProperty().set(false);
            model.resetFetchedData();
        });
        posterBox.getChildren().addAll(poster, backButton);

        VBox info = new VBox(10);

        Label title = LabelWidgets.styledLabel(model.titleProperty(), "");
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold");

        HBox originalTitleBox = new HBox();
        Label originalTitleText = LabelWidgets.styledLabel("Original title: ", "");
        Label originalTitleProp = LabelWidgets.styledLabel(model.originalTitleProperty(), "");
        originalTitleText.setStyle("-fx-text-fill: white;");
        originalTitleProp.setStyle("-fx-text-fill: white;");
        originalTitleBox.getChildren().addAll(originalTitleText, originalTitleProp);

        HBox releaseTime = new HBox(10);
        Label releaseYear = LabelWidgets.styledLabel("1999", "");
        Label runtime = LabelWidgets.styledLabel("2h 22m", "");
        releaseYear.setStyle("-fx-text-fill: white");
        runtime.setStyle("-fx-text-fill: white");
        releaseTime.getChildren().addAll(releaseYear, runtime);

        HBox imdb = new HBox(10);
        FontIcon imdbIcon = IconWidgets.styledIcon(FontAwesomeBrands.IMDB, "add-imdb-icon");

        Label imdbRating = LabelWidgets.styledLabel(model.imdbRatingProperty(), "");
        imdbRating.setStyle("-fx-text-fill: white");
        imdb.setAlignment(Pos.CENTER_LEFT);
        imdb.getChildren().addAll(imdbIcon, imdbRating);


        info.getChildren().addAll(title, originalTitleBox, releaseTime, imdb);


        results.getChildren().addAll(posterBox, info);

        return results;
    }
}