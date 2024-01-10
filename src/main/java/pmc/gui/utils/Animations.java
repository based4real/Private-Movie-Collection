package pmc.gui.utils;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
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

    public static void animate(WritableValue<Number> property, Number targetVal, Duration duration) {
        KeyValue value = new KeyValue(property, targetVal);
        KeyFrame frame = new KeyFrame(duration, value);
        new Timeline(frame).play();
    }
}
