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
import javafx.scene.layout.*;
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
        results.setFillWidth(true);

        fileNameTextField.setPromptText("Filnavn");
        Button fileChooser = ButtonWidgets.actionButton("Vælg fil...", chooseFileHandler);
        fileChooser.getStyleClass().add("add-back-button");

        HBox inputContainer = new HBox(5);
        inputContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(fileNameTextField, Priority.ALWAYS);
        inputContainer.getChildren().addAll(fileNameTextField, fileChooser);

        HBox chosenFileBox = new HBox();
        Label chosenFileLabel1 = LabelWidgets.styledLabel("Fil valgt: ", "info-others");
        chosenFileLabel1.visibleProperty().bind(model.chosenFileProperty());
        chosenFileLabel1.managedProperty().bind(model.chosenFileProperty());
        Label chosenFileLabel2 = LabelWidgets.styledLabel(model.filePathProperty(), "info-others");
        chosenFileBox.getChildren().addAll(chosenFileLabel1, chosenFileLabel2);

        Button nextButton = new Button("Næste");
        nextButton.getStyleClass().add("add-back-button");
        nextButton.disableProperty().bind(model.chosenFileProperty().not());
        nextButton.setOnAction(e -> {
            String enteredFileName = fileNameTextField.getText();
            if (enteredFileName != null && !enteredFileName.isEmpty()) {
                searchFileHandler.accept(enteredFileName);
                model.chooseFileViewProperty().set(false);
                model.fileChosenViewProperty().set(true);
            }
        });

        HBox buttonContainer = new HBox(nextButton);
        buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);

        results.getChildren().addAll(inputContainer, chosenFileBox, new Region(), buttonContainer);
        VBox.setVgrow(buttonContainer, Priority.ALWAYS);

        return results;
    }


    public Region fileChosenView() {
        HBox results = new HBox(10);

        System.out.println(model.posterPathProperty().get());
        VBox posterBox = new VBox(10);
        ImageView poster = ImageWidgets.observableBoundRoundedImage(model.posterUrlProperty(), 150, 244, 10);
        poster.getStyleClass().add("add-poster");

        Button backButton = new Button("Tilbage");
        backButton.setOnAction(e -> {
            model.chooseFileViewProperty().set(true);
            model.fileChosenViewProperty().set(false);
            model.resetFetchedData();
        });
        backButton.getStyleClass().add("add-back-button");
        posterBox.getChildren().addAll(poster, backButton);

        VBox info = new VBox(10);

        Label title = LabelWidgets.styledLabel(model.titleProperty(), "add-title");

        HBox originalTitleBox = new HBox();
        Label originalTitleText = LabelWidgets.styledLabel("Original title: ", "info-others");

        Label originalTitleProp = LabelWidgets.styledLabel(model.originalTitleProperty(), "info-others");
        originalTitleBox.getChildren().addAll(originalTitleText, originalTitleProp);

        HBox releaseTime = new HBox(10);
        Label releaseYear = LabelWidgets.styledLabel(model.releaseYearProperty(), "info-others");
        Label runtime = LabelWidgets.styledLabel(model.runtimeProperty(), "info-others");
        releaseTime.getChildren().addAll(releaseYear, runtime);

        HBox imdb = new HBox(10);
        FontIcon imdbIcon = IconWidgets.styledIcon(FontAwesomeBrands.IMDB, "add-imdb-icon");

        Label imdbRating = LabelWidgets.styledLabel(model.imdbRatingProperty(), "info-imdb-rating");
        imdb.setAlignment(Pos.CENTER_LEFT);
        imdb.getChildren().addAll(imdbIcon, imdbRating);

        Label description = LabelWidgets.styledLabel(model.descriptionProperty(), "info-description");
        description.setWrapText(true);
        description.maxWidth(400);


        info.getChildren().addAll(title, originalTitleBox, releaseTime, imdb, description);


        results.getChildren().addAll(posterBox, info);

        return results;
    }
}