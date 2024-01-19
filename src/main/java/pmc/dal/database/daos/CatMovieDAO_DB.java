package pmc.dal.database.daos;

import pmc.be.Category;
import pmc.be.Movie;
import pmc.dal.database.common.DBConnector;
import pmc.dal.exception.DataAccessException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CatMovieDAO_DB implements ICatMovieDAO {
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
        String sql = "INSERT INTO dbo.CatMovie (CategoryId, MovieId) VALUES (?, ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, category.getId());
            stmt.setInt(2, movie.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at tilføje relation mellem Category og Movie: " + e.getMessage());
        }
    }

    @Override
    public void removeRelation(Category category, Movie movie) throws DataAccessException {
        String sql = "DELETE FROM dbo.CatMovie WHERE CategoryId = ? AND MovieId = ?;";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, category.getId());
            stmt.setInt(2, movie.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at fjerne relation mellem Category og Movie: " + e.getMessage());
        }
    }

    @Override
    public List<Movie> getUForT(Category category) throws DataAccessException {
        List<Movie> movies = new ArrayList<>();
        String sql = """
                SELECT m.id, m.tmdbId, m.imdbId, m.title, m.imdbRating, m.personalRating, m.filePath, m.posterPath, m.lastSeen
                FROM dbo.CatMovie cm
                JOIN dbo.Movie m ON cm.MovieId = m.id
                WHERE cm.CategoryId = ?
                """;

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, category.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int tmdbId = rs.getInt("tmdbId");
                String imdbId = rs.getString("imdbId");
                String title = rs.getString("title");
                float imdbRating = rs.getFloat("imdbRating");
                int personalRating = rs.getInt("personalRating");
                String filePath = rs.getString("filePath");
                String posterPath = rs.getString("posterPath");
                Timestamp lastSeenTimestamp = rs.getTimestamp("lastSeen");
                LocalDateTime lastSeen = lastSeenTimestamp != null ? lastSeenTimestamp.toLocalDateTime() : null;

                movies.add(new Movie(id, tmdbId, imdbId, title, imdbRating, personalRating, filePath, posterPath, lastSeen));
            }

            return movies;
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at hente Movies for Category: " + e.getMessage());
        }
    }

    @Override
    public List<Category> getTForU(Movie movie) throws DataAccessException {
        List<Category> categories = new ArrayList<>();
        String sql = """
                SELECT c.id, c.name
                FROM dbo.CatMovie cm
                JOIN dbo.Category c ON cm.CategoryId = c.id
                WHERE cm.MovieId = ?;
                """;

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movie.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                categories.add(new Category(id, name));
            }

            return categories;
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at hente Categories for Movie: " + e.getMessage());
        }
    }

    @Override
    public void deleteAssociationsForEntity(Object entity) throws DataAccessException {
        switch (entity) {
            case Category category -> executeDelete("DELETE FROM dbo.CatMovie WHERE CategoryId = ?", category.getId());
            case Movie movie -> executeDelete("DELETE FROM dbo.CatMovie WHERE MovieId = ?", movie.getId());
            default -> throw new IllegalArgumentException("Ukendt entitet");
        }
    }

    private void executeDelete(String sql, int entityId) throws DataAccessException {
        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, entityId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at slette Category-Movie association: " + e.getMessage());
        }
    }

    @Override
    public List<Movie> getMoviesForCategory(Category category) throws DataAccessException {
        return getUForT(category);
    }

    @Override
    public List<Category> getCategoriesForMovie(Movie movie) throws DataAccessException {
        return getTForU(movie);
    }
}
