package pmc.dal.database.daos;

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
        String sql = "INSERT INTO dbo.MovieGenre (MovieId, GenreId) VALUES (?, ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movie.getId());
            stmt.setInt(2, genre.getTmdbId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at tilføje relation mellem Movie og Genre: " + e.getMessage());
        }
    }

    @Override
    public void removeRelation(Movie movie, Genre genre) throws DataAccessException {
        String sql = "DELETE FROM dbo.MovieGenre WHERE MovieId = ? AND GenreId = ?;";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movie.getId());
            stmt.setInt(2, genre.getTmdbId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at fjerne relation mellem Movie og Genre: " + e.getMessage());
        }
    }

    @Override
    public List<Genre> getUForT(Movie movie) throws DataAccessException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.tmdbId FROM dbo.MovieGenre mg JOIN dbo.Genre g ON mg.GenreId = g.tmdbId WHERE mg.MovieId = ?;";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movie.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int tmdbId = rs.getInt("tmdbId");
                genres.add(new Genre(tmdbId));
            }

            return genres;
        } catch (SQLException e) {
            throw new DataAccessException("Fejl ved at hente Genres for Movie: " + e.getMessage());
        }
    }

    @Override
    public List<Movie> getTForU(Genre genre) throws DataAccessException {
        List<Movie> movies = new ArrayList<>();
        String sql = """
                SELECT m.id, m.tmdbId, m.imdbId, m.title, m.imdbRating, m.personalRating, m.filePath, m.posterPath, m.lastSeen
                FROM dbo.MovieGenre mg
                JOIN dbo.Movie m ON mg.MovieId = m.id
                WHERE mg.GenreId = ?
                """;

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genre.getTmdbId());

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
            throw new DataAccessException("Fejl ved at hente Movies for Genres: " + e.getMessage());
        }
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

    public static void main(String[] args) throws DataAccessException {
        IJunctionDAO<Movie, Genre> dao = new MovieGenreDAO_DB();
        Genre genre = new Genre(36);
        Movie movie = new Movie(9, -1, "", "test", 0.0F, 0.0F, "", "", null);

//        dao.removeRelation(movie, genre);
/*        List<Movie> movies = dao.getTForU(genre);
        for (Movie m : movies) {
            System.out.println(m);
        }

        List<Genre> genres = dao.getUForT(movie);
        for (Genre g : genres) {
            System.out.println(g);
        }*/
    }
}
