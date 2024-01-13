package pmc.dal.database.daos;

import pmc.be.Category;
import pmc.be.Movie;
import pmc.dal.database.common.DBConnector;
import pmc.dal.database.common.IJunctionDAO;
import pmc.dal.exception.DataAccessException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CatMovieDAO_DB implements IJunctionDAO<Category, Movie> {
    DBConnector connector;

    public CatMovieDAO_DB() throws DataAccessException {
        try {
            this.connector = new DBConnector();
        } catch (IOException e) {
            throw new DataAccessException("Kunne ikke etablere forbindelse til databasen.");
        }
    }

    @Override
    public void addRelation(Category category, Movie movie) throws DataAccessException {

    }

    @Override
    public void removeRelation(Category category, Movie movie) throws DataAccessException {

    }

    @Override
    public List<Movie> getUForT(Category category) throws DataAccessException {
        return null;
    }

    @Override
    public List<Category> getTForU(Movie movie) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAssociationsForEntity(Object entity) throws DataAccessException {
        String sql = "";
        int entityId = 0;

        if (entity instanceof Category) {
            sql = "DELETE FROM dbo.CatMovie WHERE CategoryId = ?";
            entityId = ((Category) entity).getId();
        } else if (entity instanceof Movie) {
            sql = "DELETE FROM dbo.CatMovie WHERE MovieId = ?";
            entityId = ((Movie) entity).getId();
        } else {
            throw new IllegalArgumentException("Forkert entitet");
        }

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entityId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at slette Category-Movie association: " + e.getMessage());
        }
    }
}
