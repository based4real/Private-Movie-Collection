package pmc.bll;

import pmc.be.Genre;
import pmc.be.Movie;
import pmc.dal.database.common.IDAO;
import pmc.dal.database.daos.GenreDAO_DB;
import pmc.dal.database.daos.MovieDAO_DB;
import pmc.dal.exception.DataAccessException;
import pmc.utils.MovieException;

import java.util.List;

public class GenreManager {
    private final IDAO<Genre> dao;

    public GenreManager() throws MovieException {
        try {
            this.dao = new GenreDAO_DB();
        } catch (DataAccessException e) {
            throw new MovieException(e.getMessage());
        }
    }

    public List<Genre> getAllGenres() throws MovieException {
        try {
            return dao.getAll();
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke hente filmene.\n" + e.getMessage());
        }
    }


}
