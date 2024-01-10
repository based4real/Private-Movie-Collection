package pmc.gui.components.dialog;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * En generisk builder-klasse til at oprette og konfigurere instanser af Dialog<T>.<br>
 * Den giver mulighed for tilpasning af dialogens egenskaber såsom titel, button types, og indhold.<br>
 * <br>
 * Denne klasse bruger en controller, der implementerer IDialogController<T> interfacet
 * til at styre dialogens adfærd og data.

 * @param <T>
 */
public class DialogBuilder<T> {
    private final Region view;
    private final IDialogController<T> controller;
    private String title;
    private final List<ButtonType> buttonTypes = new ArrayList<>();

    /**
     * Initialiserer en DialogBuilder ved at bruge den specificerede controller.<br>
     * Initialiserer builderen med det view leveret af controlleren.
     *
     * @param controller den IDialogController<T> som styrer dialogen
     */
    public DialogBuilder(IDialogController<T> controller) {
        this.controller = controller;
        this.view = controller.getView();
    }

    /**
     * Sætter titlen af dialogen
     *
     * @param title titlen, der skal sættes på dialogen
     * @return DialogBuilder instansen for sammenkædning
     */
    public DialogBuilder<T> withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Tilføjer button types til dialogen.
     *
     * @param types de button button types der skal tilføjes til dialogen
     * @return DialogBuilder instansen for sammenkædning
     */
    public DialogBuilder<T> addButtonTypes(ButtonType... types) {
        this.buttonTypes.addAll(Arrays.asList(types));
        return this;
    }

    /**
     * Bygger og returnerer en Dialog<T> instans.
     * Opsætter dialogen med den specificerede titel, button types, og indhold leveret af IDialogController<T>.<br>
     * Initialiserer dialogen ved at bruge controlleren.
     *
     * @return den konfigurerede Dialog<T> instans
     */
    public Dialog<T> build() {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().setContent(view);

        if (!buttonTypes.isEmpty()) {
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypes);
        }

        controller.initializeDialog(dialog);

        return dialog;
    }
}