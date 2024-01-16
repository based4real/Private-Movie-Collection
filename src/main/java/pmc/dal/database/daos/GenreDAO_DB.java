package pmc.dal.database.daos;

import pmc.be.Genre;
import pmc.dal.database.common.AbstractDAO;
import pmc.dal.database.common.IDAO;
import pmc.dal.exception.DataAccessException;

import java.sql.*;

public class GenreDAO_DB extends AbstractDAO<Genre> {
    public GenreDAO_DB() throws DataAccessException {
        super();
        addDependency(new MovieGenreDAO_DB());
    }

    @Override
    protected String getTableName() {
        return "dbo.Genre";
    }

    @Override
    protected String getPrimaryKeyName() {
        return "tmdbId";
    }

    @Override
    protected String insertSql() {
        return "INSERT INTO dbo.Genre (tmdbId) VALUES (?);";
    }

    @Override
    protected String updateSql() {
        return "UPDATE dbo.Genre SET tmdbId = ? WHERE tmdbId = ?;"; // todo: tmdbId eneste felt... tænk over løsning her
    }

    @Override
    protected String deleteSql() {
        return "DELETE FROM dbo.Genre WHERE tmdbId = ?;";
    }

    @Override
    protected Genre toEntity(ResultSet rs) throws SQLException {
        int tmdbId = rs.getInt("tmdbId");
        return new Genre(tmdbId);
    }

    @Override
    protected Genre setId(Genre entity, int id) {
        return entity; // todo: tmdbId eneste felt... tænk over løsning her
    }

    @Override
    protected void setPrimaryKey(PreparedStatement stmt, Genre entity) throws SQLException {
        stmt.setInt(1, entity.getTmdbId());
    }

    @Override
    protected void setStatement(PreparedStatement stmt, Genre entity) throws SQLException {
        stmt.setInt(1, entity.getTmdbId());
    }

    @Override
    protected void setUpdateStatementId(PreparedStatement stmt, Genre original) throws SQLException {
        stmt.setInt(2, original.getTmdbId()); // todo: pga kun tmdbid fungerer ikke
    }

    public static void main(String[] args) throws DataAccessException {
        IDAO<Genre> dao = new GenreDAO_DB();

        for (Genre g : dao.getAll()) {
            System.out.println(g.getTmdbId());
        }

        dao.delete(new Genre(18));


        for (Genre g : dao.getAll()) {
            System.out.println(g.getTmdbId());
        }
    }
}
