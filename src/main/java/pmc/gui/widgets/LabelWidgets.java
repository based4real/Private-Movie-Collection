package pmc.gui.widgets;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.Label;

public class LabelWidgets {
    public static Label styledLabel(String text, String style) {
        Label results = new Label(text);
        results.getStyleClass().add(style);
        return results;
    }

    public static Label styledLabel(ObservableStringValue text, String style) {
        Label results = styledLabel("", style);
        results.textProperty().bind(text);
        return results;
    }

    public static Label styledLabel(String text, String... styles) {
        Label results = new Label(text);
        results.getStyleClass().addAll(styles);
        return results;
    }

    public static Label styledLabel(ObservableIntegerValue value, String style) {
        Label results = new Label();
        results.getStyleClass().add(style);

        results.textProperty().bind(Bindings.createStringBinding(
                () -> String.valueOf(value.get()),
                value
        ));

        return results;
    }
}
