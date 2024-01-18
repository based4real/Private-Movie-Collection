package pmc.gui.utils;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class RatingFilter implements UnaryOperator<TextFormatter.Change> {
    private static final Pattern RATING_PATTERN = Pattern.compile(
            "(10|([0-9](\\.\\d{0,1})?)|\\.)?"
    );

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        String newText = change.getControlNewText();

        if (!RATING_PATTERN.matcher(newText).matches()) {
            return null;
        }

        if (!newText.isEmpty() && !newText.equals(".")) {
            try {
                double rating = Double.parseDouble(newText);
                if (rating < 0.0 || rating > 10.0) return null;
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return change;
    }
}