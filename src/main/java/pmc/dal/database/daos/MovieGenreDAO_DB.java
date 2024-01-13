package pmc.dal.database.daos;

import pmc.be.Genre;
import pmc.be.Movie;
import pmc.dal.database.common.DBConnector;
import pmc.dal.database.common.IJunctionDAO;
import pmc.dal.exception.DataAccessException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MovieGenreDAO_DB implements IJunctionDAO<Movie, Genre> {
    DBConnector connector;

    public MovieGenreDAO_DB() throws DataAccessException {
        try {
            this.connector = new DBConnector();
        } catch (IOException e) {
            throw new DataAccessException("Kunne ikke etablere forbindelse til databasen.");
        }
    }

    @Override
    public void addRelation(Movie movie, Genre genre) throws DataAccessException {

    }

    @Override
    public void removeRelation(Movie movie, Genre genre) throws DataAccessException {

    }

    @Override
    public List<Genre> getUForT(Movie movie) throws DataAccessException {
        return null;
    }

    @Override
    public List<Movie> getTForU(Genre genre) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAssociationsForEntity(Object entity) throws DataAccessException {
        String sql = "";
        int entityId = 0;

        if (entity instanceof Movie) {
            sql = "DELETE FROM dbo.MovieGenre WHERE MovieId = ?";
            entityId = ((Movie) entity).getId();
        } else if (entity instanceof Genre) {
            sql = "DELETE FROM dbo.MovieGenre WHERE GenreId = ?";
            entityId = ((Genre) entity).getTmdbId();
        } else {
            throw new IllegalArgumentException("Forkert entitet");
        }

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entityId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at slette Movie-Genre association: " + e.getMessage());
        }
    }
}
