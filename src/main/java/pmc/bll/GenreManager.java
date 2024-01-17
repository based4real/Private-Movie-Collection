package pmc.bll;

import pmc.be.Genre;
import pmc.dal.database.common.IDAO;
import pmc.dal.database.daos.GenreDAO_DB;
import pmc.dal.exception.DataAccessException;
import pmc.utils.PMCException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreManager {
    private final IDAO<Genre> dao;

    public GenreManager() throws PMCException {
        try {
            this.dao = new GenreDAO_DB();
        } catch (DataAccessException e) {
            throw new PMCException(e.getMessage());
        }
    }

    public Optional<Genre> getGenre(int id) throws PMCException {
        try {
            return dao.get(id);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke hente genre fra id\n" + e.getMessage());
        }
    }

    public List<Genre> getAllGenres() throws PMCException {
        try {
            return dao.getAll();
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke hente alle genre.\n" + e.getMessage());
        }
    }

    public Genre addGenre(Genre genre) throws PMCException {
        try {
            return dao.add(genre);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke oprette genre.\n" + e.getMessage());
        }
    }

    public List<Genre> addGenresById(List<Integer> genreIds) throws PMCException {
        List<Genre> genres = new ArrayList<>();
        for (Integer id : genreIds) {
            Optional<Genre> genre = getGenre(id);
            // todo: kan refactores så eksisterende genre tjek er i addGenre
            if (genre.isPresent()) {
                genres.add(genre.get()); // eksister allerede så tilføj til List<Genre>
            } else {
                Genre newGenre = addGenre(new Genre(id)); // tilføj til db da den ikke eksister
                genres.add(newGenre);
            }
        }
        return genres;
    }

    public boolean updateGenre(Genre original, Genre updatedData) throws PMCException {
        try {
            return dao.update(original, updatedData);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke opdatere genre.\n" + e.getMessage());
        }
    }

    public boolean deleteGenre(Genre genre) throws PMCException {
        try {
            return dao.delete(genre);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke slette genre.\n" + e.getMessage());
        }
    }
}
