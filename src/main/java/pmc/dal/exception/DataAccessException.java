package pmc.dal.exception;

/**
 * Repræsenterer en checked exception, der kastes, når en dataadgangshandling støder på en unormal tilstand.<br>
 * <br>
 * Denne exception bliver typisk brugt til at signalere at et problem er opstået under dataadgangsoperationer,
 * såsom fejl ved at lave forespørgsler til en database eller der ikke kan forbindes.<br>
 * Formålet er, at indkapsle exceptions på et lavere niveau og give en ensartet måde at håndtere dataadgangsfejl gennem applikationen.<br>
 * <br>
 * Hvis en metode kaster denne exception indikerer det, at metoden kan støde på en situation,
 * hvor der ikke kan fås adgang til data som forventet.
 * Det betyder, at disse tilfælde skal håndteres passende af den, der kalder metoden.
 */
public class DataAccessException extends Exception {
    /**
     * Konstruer en ny DataAccessException med den angivne besked.
     *
     * @param message den besked, der forklarer årsagen til at exceptionen opstod.
     */
    public DataAccessException(String message) {
        super(message);
    }
}