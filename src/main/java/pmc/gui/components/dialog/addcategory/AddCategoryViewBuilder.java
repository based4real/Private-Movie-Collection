package pmc.gui.components.dialog.addcategory;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.util.Builder;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import pmc.be.Category;
import pmc.gui.widgets.TextFieldWidgets;
import pmc.gui.widgets.icons.IconWidgets;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AddCategoryViewBuilder implements Builder<Region> {
    private final AddCategoryModel model;
    private final Consumer<String> handleAddCategory;
    private final Consumer<Category> handleRemoveCategory;
    private final BiConsumer<Category, String> handleEditCategory;

    public AddCategoryViewBuilder(AddCategoryModel model,
                                  CategoryActions actions) {
        this.model = model;
        this.handleAddCategory = actions.add();
        this.handleRemoveCategory = actions.delete();
        this.handleEditCategory = actions.update();
    }

    @Override
    public Region build() {
        GridPane results = new GridPane();
        results.setVgap(10);
        results.setHgap(10);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(90);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(10);

        results.getColumnConstraints().addAll(col1, col2);

        int row = 0;

        TextField searchField = new TextField();
        searchField.setPromptText("Søg efter kategori");
        searchField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setColumnSpan(searchField, 1);
        results.add(searchField, 0, row++, 1, 1);

        FilteredList<Category> filteredCategories = new FilteredList<>(model.getCategories(), p -> true);
        searchField.textProperty().addListener((obs, ov, nv) -> {
            filteredCategories.setPredicate(category -> {
                System.out.println(model.getCategories().size());
                if (nv == null || nv.isEmpty()) return true;

                if (category.getName().toLowerCase().contains(nv.toLowerCase())) return true;
                return false;
            });
        });


        ListView<Category> categoryListView = new ListView<>(filteredCategories);
        categoryListView.setCellFactory(lv -> createCategoryCell());

        results.add(categoryListView, 0, row++, 1, 1);

        Button btn = new Button("Opret");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(event -> handleAddCategory.accept(model.nameProperty().get()));

        TextField addCategoryTextField = TextFieldWidgets.boundTextField(model.nameProperty());
        addCategoryTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleAddCategory.accept(model.nameProperty().get());
                event.consume(); // konsumere handlingen så den ikke går videre
                // da Dialog boksen også modtager en ENTER key event til at lukke dialog boksen
            }
        });
        results.add(addCategoryTextField, 0, row);
        results.add(btn, 1, row);

        return results;
    }

    private ListCell<Category> createCategoryCell() {
        return new ListCell<>() {
            private final Label nameLabel = new Label();
            private final TextField editTextField = new TextField();
            private final Region spacer = new Region();
            private final FontIcon deleteIcon = IconWidgets.styledIcon(Material2OutlinedAL.DELETE, "delete-icon");
            private final HBox layout = new HBox(10);

            {
                layout.getChildren().addAll(nameLabel, spacer, deleteIcon);
                layout.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(spacer, Priority.ALWAYS);

                nameLabel.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !isEmpty()) {
                        Category category = getItem();
                        if (category != null) {
                            enableEditMode(category);
                        }
                    }
                });

                deleteIcon.setOnMouseClicked(event -> {
                    Category category = getItem();
                    if (category != null) {
                        handleRemoveCategory.accept(category);
                    }
                });

                editTextField.setOnAction(event -> finishEdit());
                editTextField.focusedProperty().addListener((obs, ov, nv) -> {
                    if (!nv) finishEdit();
                });
            }

            private void enableEditMode(Category category) {
                editTextField.setText(category.getName());
                setGraphic(editTextField);
                editTextField.requestFocus();
            }

            private void finishEdit() {
                String newName = editTextField.getText();
                Category category = getItem();
                if (category != null && !newName.isEmpty()) {
                    handleEditCategory.accept(category, newName);
                }

                setGraphic(layout);
                nameLabel.setText(newName);
            }

            @Override
            public void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (!empty && category != null) {
                    nameLabel.setText(category.getName());
                    setGraphic(layout);
                } else {
                    setGraphic(null);
                }
            }
        };
    }
}