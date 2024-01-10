package pmc.gui.components.dialog;

import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;

/**
 * Interface for en dialog controller.
 *
 * @param <T> typen af det objekt, som denne controller vil bruge til at initialisere og styre dialogen.
 */
public interface IDialogController<T> {

    /**
     * @return det Region objekt der repræsenterer the brugergrænsefladen af dialogen.
     */
    Region getView();

    /**
     * Initializes the given dialog with necessary properties and event handlers.<br>
     * This method is expected to set up the dialog's behavior and interactions with the user.<br>
     * The method modifies the passed Dialog<T> object by setting its properties and event handlers,
     * facilitating the intended functionality.<br>
     * <br>
     * Initialiserer den givne dialog med nødvendige egenskaber og event handlers.<br>
     * Denne metode forventes at opsætte dialogens adfærd og interaktioner med brugeren.<br>
     * Denne metode ændrer det passerede Dialog<T> objekt ved at indstille dets egenskaber og event handlers,
     * for at facilitere den tilsigtede funktionalitet.
     *
     * @param dialog den Dialog<T> instans, der skal initialiseres
     */
    void initializeDialog(Dialog<T> dialog);
}
