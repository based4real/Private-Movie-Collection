package pmc.dal.database.daos;

import pmc.be.Movie;
import pmc.dal.database.common.AbstractDAO;
import pmc.dal.database.common.IDAO;
import pmc.dal.exception.DataAccessException;

import java.sql.*;
import java.time.LocalDateTime;

public class MovieDAO_DB extends AbstractDAO<Movie> {
    public MovieDAO_DB() throws DataAccessException {
        super();
        addDependency(new MovieGenreDAO_DB());
        addDependency(new CatMovieDAO_DB());
    }

    @Override
    protected String getTableName() {
        return "dbo.Movie";
    }

    @Override
    protected String getPrimaryKeyName() {
        return "id";
    }


    @Override
    protected String insertSql() {
        return "INSERT INTO dbo.Movie (tmdbId, imdbId, title, imdbRating, personalRating, filePath, posterPath, lastSeen) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    }

    @Override
    protected String updateSql() {
        return "UPDATE dbo.Movie SET tmdbId = ?, imdbId = ?, title = ?, imdbRating = ?, personalRating = ?, filePath = ?, posterPath = ?, lastSeen = ? WHERE id = ?";
    }

    @Override
    protected String deleteSql() {
        return "DELETE FROM dbo.Movie WHERE id = ?;";
    }

    @Override
    protected Movie toEntity(ResultSet rs) throws SQLException {
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

        return new Movie(id, tmdbId, imdbId, title, imdbRating, personalRating, filePath, posterPath, lastSeen);
    }

    @Override
    protected Movie setId(Movie entity, int id) {
        return new Movie(id, entity.getTmdbId(), entity.getImdbId(), entity.getTitle(), entity.getImdbRating(), entity.getPersonalRating(), entity.getFilePath(), entity.getPosterPath(), entity.getLastSeen());
    }

    @Override
    protected void setPrimaryKey(PreparedStatement stmt, Movie entity) throws SQLException {
        stmt.setInt(1, entity.getId());
    }

    @Override
    protected void setStatement(PreparedStatement stmt, Movie entity) throws SQLException {
        stmt.setInt(1, entity.getTmdbId());
        stmt.setString(2, entity.getImdbId());
        stmt.setString(3, entity.getTitle());
        stmt.setFloat(4, entity.getImdbRating());
        stmt.setFloat(5, entity.getPersonalRating());
        stmt.setString(6, entity.getFilePath());
        stmt.setString(7, entity.getPosterPath());

        if (entity.getLastSeen() != null) {
            stmt.setTimestamp(8, Timestamp.valueOf(entity.getLastSeen()));
        } else {
            stmt.setNull(8, Types.TIMESTAMP);
        }
    }

    @Override
    protected void setUpdateStatementId(PreparedStatement stmt, Movie original) throws SQLException {
        stmt.setInt(9, original.getId());
    }

    public static void main(String[] args) throws DataAccessException {
        IDAO<Movie> dao = new MovieDAO_DB();


        Movie m = new Movie(11, 580175, "tt10288566", "Druk", 7.7F, 10.0F, "Another.Round.mp4", "aDcIt4NHURLKnAEu7gow51Yd00Q.jpg", null);
        dao.delete(m);

/*        Optional<Movie> movie = dao.get(4);
        movie.ifPresent(System.out::println);


        movie.ifPresent(m -> {
            try {
                dao.update(m, new Movie(11324, "tt1130884", "KARTOFFELd", 8.2F, 7.4F, "shutter island 4k.mp4", "4GDy0PHYX3VRXUtwK5ysFbg3kEx.jpg", LocalDateTime.now()));
            } catch (DataAccessException e) {
                throw new RuntimeException(e); // test
            }
        });

        movie.ifPresent(System.out::println);*/


/*        movie.ifPresent(m -> {
            try {
                dao.delete(m);
            } catch (DataAccessException e) {
                throw new RuntimeException(e); // test
            }
        });*/

/*        Movie m = new Movie(6, 580175, "tt10288566", "Druk", 7.7F, 10.0F, "Another.Round.mp4", "aDcIt4NHURLKnAEu7gow51Yd00Q.jpg", null);
        dao.delete(m);*/
/*        Movie m1 = new Movie(99999, "imdbIDVeryLongStringExceedingNormalLength",
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
        }*/
    }
}