package pmc.utils;

/**
 * Repræsenterer en checked exception specifikt for fejl, der opstår i forbindelse med håndtering af Movie entiteter.<br>
 * Anvendes for at give en klar og relevant fejlbesked relateret til Movie entiteter.<br>
 * <br>
 * Hvis en metode kaster denne exception indikerer det, at metoden kan støde på en situation,
 * hvor der opstås fejl i forbindelse med Movie entiteter.
 */
public class PMCException extends Exception {
    /**
     * Konsturerer en ny PMCException med den angivne besked.
     *
     * @param message den besked, der forklarer årsagen til at exceptionen opstod.
     */
    public PMCException(String message) {
        super(message);
    }
}