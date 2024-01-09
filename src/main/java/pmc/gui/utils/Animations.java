package pmc.gui.utils;

import javafx.animation.FadeTransition;
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
}
