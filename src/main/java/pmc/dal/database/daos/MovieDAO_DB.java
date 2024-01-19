package pmc.dal.database.daos;

import pmc.be.Movie;
import pmc.dal.database.common.AbstractDAO;
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
        int personalRating = rs.getInt("personalRating");
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
}