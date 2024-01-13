package pmc.dal.database.daos;

import pmc.be.Category;
import pmc.be.Genre;
import pmc.be.Movie;
import pmc.dal.database.common.DBConnector;
import pmc.dal.database.common.IJunctionDAO;
import pmc.dal.exception.DataAccessException;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
                float personalRating = rs.getFloat("personalRating");
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

    public static void main(String[] args) throws DataAccessException {
        IJunctionDAO<Category, Movie> dao = new CatMovieDAO_DB();

        Category category = new Category(3, "Engelsk");
        Movie movie = new Movie(9, -1, "", "test", 0.0F, 0.0F, "", "", null);
        dao.removeRelation(category, movie);



/*        List<Movie> movies = dao.getUForT(category);
        for (Movie m : movies) {
            System.out.println(m);
        }

        List<Category> categories = dao.getTForU(movie);
        for (Category c : categories) {
            System.out.println(c);
        }*/
    }
}
