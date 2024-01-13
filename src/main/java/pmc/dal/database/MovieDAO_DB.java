package pmc.dal.database;

import pmc.be.Movie;
import pmc.dal.exception.DataAccessException;

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
        String sql = "INSERT INTO dbo.Movie (tmdbId, imdbId, title, imdbRating, personalRating, filePath, posterPath, lastSeen) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, movie.getTmdbId());
            stmt.setString(2, movie.getImdbId());
            stmt.setString(3, movie.getTitle());
            stmt.setFloat(4, movie.getImdbRating());
            stmt.setFloat(5, movie.getPersonalRating());
            stmt.setString(6, movie.getFilePath());
            stmt.setString(7, movie.getPosterPath());
            stmt.setTimestamp(8, movie.getLastSeen() != null ? Timestamp.valueOf(movie.getLastSeen()) : null);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            int id = 0;
            if (rs.next()) id = rs.getInt(1);

            return new Movie(id,
                    movie.getTmdbId(),
                    movie.getImdbId(),
                    movie.getTitle(),
                    movie.getImdbRating(),
                    movie.getPersonalRating(),
                    movie.getFilePath(),
                    movie.getPosterPath(),
                    movie.getLastSeen()
            );

        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke tilføje Movie entitet: " + movie + "til DB.\n Fejlbesked: " + e.getMessage());
        }
    }

    @Override
    public boolean update(Movie original, Movie updatedData) throws DataAccessException {
        return false;
    }

    @Override
    public boolean delete(Movie movie) throws DataAccessException {
        return false;
    }

    public static void main(String[] args) throws DataAccessException {
        IDAO<Movie> dao = new MovieDAO_DB();

        Movie m1 = new Movie(99999, "imdbIDVeryLongStringExceedingNormalLength",
                "A very long movie title that exceeds the expected length for a movie title, potentially causing issues with database insertion or retrieval",
                10.0f, 10.0f, "/path/to/file", "/path/to/poster", LocalDateTime.now());

        Movie m2 = new Movie(12345, "tt10360916", "Negative Ratings Movie", -5.0f, -3.0f, "/path/negative", "/poster/negative", LocalDateTime.now());

        // M3  fejler pga null titel
        Movie m3 = new Movie(54321, "IMDB54321", null, 5.0f, 4.0f, "/path/nulltitle", "/poster/nulltitle", LocalDateTime.now());
        Movie m4 = new Movie(67890, "1", "Future Movie", 6.5f, 7.0f, "/path/future", "/poster/future", LocalDateTime.now().plusYears(1));
        Movie m5 = new Movie(13579, "IMDB13579", "Empty Paths Movie", 3.0f, 2.5f, "", "", LocalDateTime.now());

        // M6 fejler pga ratinger over 10.0f
        Movie m6 = new Movie(24680, "IMDB24680", "Highly Rated Movie", 100.0f, 100.0f, "/path/high", "/poster/high", LocalDateTime.now());

        Movie m7 = new Movie(11223, "IMDB11223", "Null Last Seen Movie", 1.0f, 1.5f, "/path/nulllastseen", "/poster/nulllastseen", null);

        // M8 fejler ikke, kun titel må ikke være null -
        Movie m8 = new Movie(44556, null, "test m8", 0.0f, 0.0f, null, null, null);
        Movie m9 = new Movie(90876, "IMDB90876", "Long Path Movie", 8.0f, 7.5f,
                "/this/is/a/very/long/file/path/that/might/exceed/the/database/field/limit/and/cause/an/error/when/trying/to/insert/the/movie/into/the/database/movies.mp4",
                "/this/is/a/very/long/poster/path/that/might/exceed/the/database/field/limit/and/cause/an/error/when/trying/to/insert/the/movie/into/the/database/poster.jpg",
                LocalDateTime.now());

        List<Movie> movies = List.of(m1, m2, m4, m5, m7, m8, m9);
        for (Movie m : movies) {
            System.out.println(m.getTitle());
            Movie addedMovie = dao.add(m);
            System.out.println(addedMovie);
        }
    }
}
