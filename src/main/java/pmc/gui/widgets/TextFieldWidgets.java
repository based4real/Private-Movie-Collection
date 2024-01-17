package pmc.gui.widgets;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

public class TextFieldWidgets {
    public static TextField boundTextField(StringProperty boundProperty) {
        return boundTextField(boundProperty, true);
    }

    public static TextField boundTextField(StringProperty boundProperty, boolean isEditable) {
        TextField textField = new TextField();
        textField.textProperty().bindBidirectional(boundProperty);
        textField.setEditable(isEditable);
        return textField;
    }
}
