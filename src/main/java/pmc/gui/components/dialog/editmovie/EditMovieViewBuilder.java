package pmc.gui.components.dialog.editmovie;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import pmc.gui.common.MovieModel;
import pmc.gui.components.categories.CategoriesModel;
import pmc.gui.widgets.icons.IconWidgets;

import java.util.function.Consumer;

public class EditMovieViewBuilder implements Builder<Region> {
    private final MovieModel movieToEdit;
    private final ObservableList<CategoriesModel> categories;
    private final Consumer<EditMovieData> updateHandler;

    public EditMovieViewBuilder(MovieModel movieToEdit, ObservableList<CategoriesModel> categories, Consumer<EditMovieData> updateHandler) {
        this.movieToEdit = movieToEdit;
        this.categories = categories;
        this.updateHandler = updateHandler;
    }

    @Override
    public Region build() {
        VBox results = new VBox(10);

        Label title = new Label(movieToEdit.titleProperty().get());
        title.setStyle("-fx-text-fill: white");

        ListView<CategoriesModel> categoryListView = new ListView<>(categories);
        categoryListView.setCellFactory(lv -> createCategoryCell());

        results.getChildren().addAll(title, categoryListView);

        return results;
    }

    private ListCell<CategoriesModel> createCategoryCell() {
        return new ListCell<>() {
            private final Label nameLabel = new Label();
            private final Region spacer = new Region();
            private final FontIcon categoryIcon = IconWidgets.styledIcon(Material2OutlinedAL.CHECK, "delete-icon");
            private final HBox layout = new HBox(10);

            {
                layout.getChildren().addAll(nameLabel, spacer, categoryIcon);
                layout.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(spacer, Priority.ALWAYS);
            }

            @Override
            protected void updateItem(CategoriesModel categoryModel, boolean empty) {
                super.updateItem(categoryModel, empty);
                if (empty || categoryModel == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(categoryModel.nameProperty().get());
                    boolean isCategoryAssigned = movieToEdit.categoryObservableList().stream()
                            .anyMatch(category -> category.getId() == categoryModel.idProperty().get());
                    categoryIcon.setVisible(isCategoryAssigned);
                    categoryIcon.setManaged(isCategoryAssigned);

                    setOnMouseClicked(event -> {
                        int categoryId = categoryModel.idProperty().get();
                        updateHandler.accept(new EditMovieData(movieToEdit, categoryId));

                        boolean newAssignmentStatus = movieToEdit.categoryObservableList().stream()
                                .anyMatch(category -> category.getId() == categoryId);
                        categoryIcon.setVisible(newAssignmentStatus);
                        categoryIcon.setManaged(newAssignmentStatus);
                    });

                    setGraphic(layout);
                }
            }
        };
    }
}
