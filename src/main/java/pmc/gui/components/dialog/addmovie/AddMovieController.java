package pmc.gui.components.dialog.addmovie;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import pmc.be.rest.tmdb.TMDBGenreEntity;
import pmc.gui.common.MovieDetailsModel;
import pmc.gui.common.MovieModel;
import pmc.gui.components.dialog.IDialogController;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AddMovieController implements IDialogController<AddMovieData> {
    private final AddMovieModel model;
    private final AddMovieViewBuilder viewBuilder;
    private Dialog<AddMovieData> dialog;

    public AddMovieController(Consumer<String> onFileChosen) {
        this.model = new AddMovieModel();
        this.viewBuilder = new AddMovieViewBuilder(model, onFileChosen, event -> chooseFile());
    }

    @Override
    public Region getView() {
        return viewBuilder.build();
    }

    @Override
    public void initializeDialog(Dialog<AddMovieData> dialog) {
        this.dialog = dialog;

        dialog.setResultConverter(dialogBtn -> {
            if (dialogBtn == ButtonType.OK) {
                // todo: håndter at sige OK når der ikke er noget data.
                List<Integer> genreIds = model.getGenres().stream()
                        .map(TMDBGenreEntity::getID)
                        .toList();

                // todo: opbevar imdb rating som string i DB? for at undgå N/A
                float imdbRating = 0.0F;
                if (!model.imdbRatingProperty().get().equals("N/A")) {
                    imdbRating = Float.parseFloat(model.imdbRatingProperty().get());
                }

                return new AddMovieData(
                        Integer.parseInt(model.tmdbRatingProperty().get()),
                        model.imdbIdProperty().get(),
                        model.originalTitleProperty().get(),
                        imdbRating, // håndter N/A
                        0,
                        model.filePathProperty().get(),
                        model.posterPathProperty().get(),
                        null,
                        model.absoluteFilePathProperty().get(),
                        model.posterUrlProperty().get(),
                        genreIds);
            }
            return null;
        });
    }

    public void setTMDBMovie(MovieDetailsModel model) {
        this.model.imdbIdProperty().set(model.imdbIdProperty().get());
        this.model.titleProperty().set(model.titleProperty().get());
        this.model.originalTitleProperty().set(model.originalTitleProperty().get());
        this.model.tmdbRatingProperty().set(model.tmdbRatingProperty().get());
        this.model.imdbRatingProperty().set(model.imdbRatingProperty().get());
        System.out.println(model.posterPathProperty().get());
        this.model.posterPathProperty().set(model.posterPathProperty().get());
        System.out.println(this.model.posterPathProperty().get());
        this.model.descriptionProperty().set(model.descriptionProperty().get());

        this.model.posterUrlProperty().set("https://image.tmdb.org/t/p/original" + model.posterPathProperty().get());

        this.model.setGenres(model.genresProperty());
    }

    private void chooseFile() {
        FileChooser chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Film", "*.mp4", "*.mpeg4");
        chooser.getExtensionFilters().add(filter);

        File selectedFile = chooser.showOpenDialog(null);
        if (selectedFile != null) {
            model.absoluteFilePathProperty().set(selectedFile.getAbsolutePath());
            model.filePathProperty().set(selectedFile.getName());
            String sanitizedTitle = sanitizeFileName(selectedFile.getName());
            model.fileNameProperty().set(sanitizedTitle);
            model.chosenFileProperty().set(true);
            viewBuilder.getFileNameTextField().setText(sanitizedTitle);
        }
    }

    private String sanitizeFileName(String fileName) {
        String noExtension = fileName.replaceAll("\\.\\w+$", "");

        String replacedSeparators = noExtension.replaceAll("[._-]", " ");

        String cleaned = replacedSeparators.replaceAll("1080p|720p|x264|YTS AM|WEBRip|YIFY", "")
                .replaceAll("\\s+", " ")
                .replaceAll("\\s\\d{4}\\s", " ")
                .trim();

        System.out.println(cleaned);
        return cleaned;
    }
}