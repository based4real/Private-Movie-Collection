package pmc.util;

/**
 * Repræsenterer en checked exception specifikt for fejl, der opstår i forbindelse med håndtering af Movie entiteter.<br>
 * Anvendes for at give en klar og relevant fejlbesked relateret til Movie entiteter.<br>
 * <br>
 * Brugen af denne exception indikerer potentielle fejl i forbindelse med Movie entiteter.
 */
public class MovieException extends Exception {
    /**
     * Konsturerer en ny MovieException med den angivne besked.
     *
     * @param message den besked, der forklarer årsagen til at exceptionen opstod.
     */
    public MovieException(String message) {
        super(message);
    }
}