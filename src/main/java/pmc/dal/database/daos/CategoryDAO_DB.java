package pmc.dal.database.daos;

import pmc.be.Category;
import pmc.dal.database.common.ADAO;
import pmc.dal.database.common.IDAO;
import pmc.dal.exception.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAO_DB extends ADAO<Category> {
    public CategoryDAO_DB() throws DataAccessException {
        super();
        addDependency(new CatMovieDAO_DB());
    }

    @Override
    protected String getTableName() {
        return "dbo.Category";
    }

    @Override
    protected String insertSql() {
        return "INSERT INTO dbo.Category (name) VALUES (?)";
    }

    @Override
    protected String updateSql() {
        return "UPDATE dbo.Category SET name = ? WHERE id = ?;";
    }

    @Override
    protected String deleteSql() {
        return "DELETE FROM dbo.Category WHERE id = ?;";
    }

    @Override
    protected Category toEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new Category(id, name);
    }

    @Override
    protected Category setId(Category entity, int id) {
        return new Category(id, entity.getName());
    }

    @Override
    protected void setPrimaryKey(PreparedStatement stmt, Category entity) throws SQLException {
        stmt.setInt(1, entity.getId());
    }

    @Override
    protected void setStatement(PreparedStatement stmt, Category entity) throws SQLException {
        stmt.setString(1, entity.getName());
    }

    @Override
    protected void setUpdateStatementId(PreparedStatement stmt, Category original) throws SQLException {
        stmt.setInt(2, original.getId());
    }

    public static void main(String[] args) throws DataAccessException {
        IDAO<Category> dao = new CategoryDAO_DB();

        for (Category c : dao.getAll()) {
            System.out.println(c.getName());
        }

//        dao.delete(new Category(2, "test slet"));

//        dao.update(new Category(2, "Engelsk"), new Category(2, "ENGLISH"));

        for (Category c : dao.getAll()) {
            System.out.println(c.getName());
        }

    }
}
