package pmc.dal.database.genre;

import pmc.be.Genre;
import pmc.dal.database.DBConnector;
import pmc.dal.database.IDAO;
import pmc.dal.exception.DataAccessException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenreDAO_DB implements IDAO<Genre> {
    private final DBConnector connector;

    public GenreDAO_DB() throws DataAccessException {
        try {
            connector = new DBConnector();
        } catch (IOException e) {
            throw new DataAccessException("Kunne ikke forbinde til database");
        }
    }

    @Override
    public Optional<Genre> get(int id) throws DataAccessException {
        String sql = "SELECT * FROM dbo.Genre WHERE tmdbId = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Genre genre = new Genre(rs.getInt("tmdbId"));
                return Optional.of(genre);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke finde Genre entitet med tmdbId: " + id + ".\n Fejlbesked: " + e.getMessage());
        }
    }

    @Override
    public List<Genre> getAll() throws DataAccessException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM dbo.Genre";

        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Genre genre = new Genre(rs.getInt("tmdbId"));
                genres.add(genre);
            }

            return genres;
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke hente genres fra databasen.\n Fejlbesked: " + e.getMessage());
        }
    }

    @Override
    public Genre add(Genre genre) throws DataAccessException {
        String sql = "INSERT INTO dbo.Genre (tmdbId) VALUES (?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, genre.getTmdbId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return genre;
            } else {
                throw new DataAccessException("Failed to insert the genre into the database.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke tilføje Genre entitet: " + genre + "til DB.\n Fejlbesked: " + e.getMessage());
        }
    }

    @Override
    public boolean update(Genre original, Genre updatedData) throws DataAccessException {
        return false;
    }

    @Override
    public boolean delete(Genre genre) throws DataAccessException {
        String sqlDeleteMovieGenreRef = "DELETE FROM dbo.MovieGenre WHERE GenreId = ?";
        String sqlDeleteGenre = "DELETE FROM dbo.Genre WHERE tmdbId = ?";

        try (Connection conn = connector.getConnection()) {
            conn.setAutoCommit(false);             // Starter transaction

            try (PreparedStatement stmtDelMovieGenre = conn.prepareStatement(sqlDeleteMovieGenreRef);
                 PreparedStatement stmtDelGenre = conn.prepareStatement(sqlDeleteGenre)) {

                // Slet referencer i MovieGenre table
                stmtDelMovieGenre.setInt(1, genre.getTmdbId());
                stmtDelMovieGenre.executeUpdate();

                // Slet Genre
                stmtDelGenre.setInt(1, genre.getTmdbId());
                int rowsAffected = stmtDelGenre.executeUpdate();

                conn.commit(); // Commit transaction

                return rowsAffected > 0;
            } catch (SQLException e) {
                conn.rollback(); // Rollback hvis der sker en exception
                throw new DataAccessException("Kunne ikke slette Genre entitet: " + genre + ".\n Fejlbesked: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Der opstod en fejl ved at tilgå databasen for at slette en genre.\n Fejlbesked: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws DataAccessException {
        IDAO<Genre> dao = new GenreDAO_DB();

        for (Genre g : dao.getAll()) {
            System.out.println(g.getTmdbId());
        }

//        dao.add(new Genre(1337));

//        for (Genre g : dao.getAll()) {
//            System.out.println(g.getTmdbId());
//        }

        dao.delete(new Genre(99));

        for (Genre g : dao.getAll()) {
            System.out.println(g.getTmdbId());
        }
    }
}
