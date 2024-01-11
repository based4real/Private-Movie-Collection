package pmc.dal.database;

import pmc.be.Movie;
import pmc.dal.exception.DataAccessException;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAO_DB implements IDAO<Movie> {
    private final DBConnector connector;

    public MovieDAO_DB() throws DataAccessException {
        try {
            connector = new DBConnector();
        } catch (IOException e) {
            throw new DataAccessException("Kunne ikke forbinde til database");
        }
    }

    @Override
    public Optional<Movie> get(long id) throws DataAccessException {
        return Optional.empty();
    }

    @Override
    public List<Movie> getAll() throws DataAccessException {
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM dbo.Movie";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                int tmdbId = rs.getInt("tmdbId");
                String imdbId = rs.getString("imdbId");
                String title = rs.getString("title");
                float imdbRating = rs.getFloat("imdbRating");
                float personalRating = rs.getFloat("personalRating");
                String filePath = rs.getString("filePath");
                String posterPath = rs.getString("posterPath");

                Timestamp timestamp = rs.getTimestamp("lastSeen");
                Optional<LocalDateTime> lastSeen = Optional.ofNullable(timestamp != null ? timestamp.toLocalDateTime() : null);

                Movie movie = new Movie(id, tmdbId, imdbId, title, imdbRating, personalRating, filePath, posterPath, lastSeen.orElse(null));
                movies.add(movie);
            }

            return movies;
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke hente sange fra databasen");
        }
    }

    @Override
    public Movie add(Movie movie) throws DataAccessException {
        return null;
    }

    @Override
    public boolean update(Movie original, Movie updatedData) throws DataAccessException {
        return false;
    }

    @Override
    public boolean delete(Movie movie) throws DataAccessException {
        return false;
    }
}
