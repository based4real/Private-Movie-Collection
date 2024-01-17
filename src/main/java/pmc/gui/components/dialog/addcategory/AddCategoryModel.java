package pmc.gui.components.dialog.addcategory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import pmc.be.Category;

public class AddCategoryModel {
    private ObservableList<Category> categories;
    private final StringProperty name = new SimpleStringProperty("");

    public AddCategoryModel() {}


    public void setCategories(ObservableList<Category> categories) {
        this.categories = categories;
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public StringProperty nameProperty() {
        return name;
    }
}