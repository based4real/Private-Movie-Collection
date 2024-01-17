package pmc.gui.utils;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Webbrowser {

    public static void openURL(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException | IOException e) {
            ErrorHandler.showErrorDialog("Fejl", "Kunne ikke åbne webbrowser");
        }
    }

    public static void openURL(URI uri) {
        try {
            Desktop.getDesktop().browse(uri);
        } catch (IOException e) {
            ErrorHandler.showErrorDialog("Fejl", "Kunne ikke åbne webbrowser");
        }
    }

    public static void openTMDBCredits(int id) {
        openURL("https://www.themoviedb.org/person/" + id);
    }

    public static void openTMDBInfo(int id) {
        openURL("https://www.themoviedb.org/movie/" + id);
    }

    public static WebView loadYoutubeEmbed(String url) {
        WebView webview = new WebView();
        WebEngine webEngine = webview.getEngine();
        String embedCode = "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/" + "JCZcFFKS-Qk" + "\" frameborder=\"0\" allowfullscreen></iframe>";

        webEngine.loadContent(embedCode);
        webview.setPrefHeight(315);
        webview.setPrefWidth(560);
        webview.setVisible(true);

        return webview;
    }
}
