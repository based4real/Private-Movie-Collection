package pmc.dal;

import pmc.dal.exception.DataAccessException;

import java.util.List;
import java.util.Optional;

/**
 * Interface for generiske Data Access Object (DAO) operationer.<br>
 * Er ansvarlig for at levere CRUD operationer for en bestemt type T.<br>
 * <br>
 * Alle metoder i dette interface kaster en DataAccessException hvis der opstår en fejl for at tilgå data.<br>
 * Hvilket er en checked exception, som indikerer en unormal tilstand relateret til DAO operationer.
 *
 * @param <T> typen af den entitet, som DAO'en vil styre.
 */
public interface IDAO<T> {
    /**
     * Henter en entitet ved dens id.
     *
     * @param id den unikke identifikator for entiteten.
     * @return en Optional som indeholder entiteten hvis fundet, ellers en empty Optional hvis ikke.
     */
    Optional<T> get(long id) throws DataAccessException;

    /**
     * Henter all entiteter af type T.
     *
     * @return en liste af alle entiteter fundet. Listen kan være tom hvis ingen entiteter er fundet.
     */
    List<T> getAll() throws DataAccessException;

    /**
     * Tilføjer en ny entitet.
     *
     * @param t entiteten der skal tilføjes.
     * @return den tilføjede entitet med eventuelle ændringer foretager under indsættelsen (som genereret id).
     */
    T add(T t) throws DataAccessException;

    /**
     * Opdater en eksisterende entitet til at matche de opdaterede data.
     *
     * @param original den originale entitet, som vil blive opdateret.
     * @param updatedData den entitet der indeholder de opdaterede felter,
     *                    som 'original' vil blive opdateret til at matche.
     * @return true hvis opdateringen var successful, ellers false.
     */
    boolean update(T original, T updatedData) throws DataAccessException;

    /**
     * Sletter en entitet.
     *
     * @param t entiteten der skal slettes.
     * @return true hvis sletningen var successful, ellers false.
     */
    boolean delete(T t) throws DataAccessException;
}