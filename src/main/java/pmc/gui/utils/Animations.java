package pmc.gui.utils;

import javafx.animation.*;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.util.Duration;

public class Animations {
    public static void fadeIn(Node node, Duration duration, double fromOpacity, double toOpacity) {
        FadeTransition fadeIn = new FadeTransition(duration, node);
        fadeIn.setToValue(toOpacity);
        fadeIn.setFromValue(fromOpacity);
        fadeIn.play();
    }

    public static void fadeOut(Node node, Duration duration, double fromOpacity, double toOpacity) {
        FadeTransition fadeOut = new FadeTransition(duration, node);
        fadeOut.setToValue(toOpacity);
        fadeOut.setFromValue(fromOpacity);
        fadeOut.play();
    }

    public static void slideIn(Node node, Duration duration, double fromY, double toY) {
        TranslateTransition slideIn = new TranslateTransition(duration, node);
        slideIn.setFromY(fromY);
        slideIn.setToY(toY);
        slideIn.play();
    }

    public static void slideOut(Node node, Duration duration, double fromY, double toY) {
        TranslateTransition slideOut = new TranslateTransition(duration, node);
        slideOut.setFromY(fromY);
        slideOut.setToY(toY);
        slideOut.play();
    }

    public static void animate(WritableValue<Number> property, Number targetVal, Duration duration) {
        KeyValue value = new KeyValue(property, targetVal);
        KeyFrame frame = new KeyFrame(duration, value);
        new Timeline(frame).play();
    }
}
