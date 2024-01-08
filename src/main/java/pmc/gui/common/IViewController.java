package pmc.gui.common;

import javafx.scene.layout.Region;

/**
 * Interface for Controllere i en MVC arkitektur.
 * Er ansvarlig for at levere View komponenten.
 */
public interface IViewController {

    /**
     * Henter View komponenten.
     * Metoden forventes at returnere et konstrueret View, typisk ved at påkalde en ViewBuilder.
     *
     * @return Region objektet, der repræsenterer MVC komponentens View.
     */
    Region getView();
}