package pmc.dal.database.common;

import pmc.dal.exception.DataAccessException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class ADAO<T> implements IDAO<T> {
    protected DBConnector connector;
    private List<IJunctionDAO<?, ?>> dependencies = new ArrayList<>();

    public ADAO() throws DataAccessException {
        try {
            connector = new DBConnector();
        } catch (IOException e) {
            throw new DataAccessException("Kunne ikke etablere forbindelse til databasen.");
        }
    }

    public Optional<T> get(int id) throws DataAccessException {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(toEntity(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke finde entitet med id: " + id + ".\n Fejlbesked: " + e.getMessage());
        }
    }

    @Override
    public List<T> getAll() throws DataAccessException {
        List<T> entities = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();

        try (Connection conn = connector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                entities.add(toEntity(rs));
            }
            return entities;
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke hente alle entiteter.\n Fejlbesked: " + e.getMessage());
        }
    }

    @Override
    public T add(T entity) throws DataAccessException {
        String sql = insertSql();

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            setStatement(stmt, entity);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }

            return setId(entity, id);
        } catch (SQLException e) {
            throw new DataAccessException("Kunne ikke tilføje entitet: " + entity + ".\n Fejlbesked: " + e.getMessage());
        }
    }

    @Override
    public boolean update(T original, T updatedData) throws DataAccessException {
        String sql = updateSql();

        try (Connection conn = connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setStatement(stmt, updatedData);
            setUpdateStatementId(stmt, original);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Kunne opdatere entitet: " + original + " i DB.\n Fejlbesked: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(T entity) throws DataAccessException {
        String sql = deleteSql();

        try (Connection conn = connector.getConnection()) {
            conn.setAutoCommit(false); // start transaction

            for (IJunctionDAO<?, ?> dependency : dependencies) {
                dependency.deleteAssociationsForEntity(entity);
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                setPrimaryKey(stmt, entity);
                int rowsAffected = stmt.executeUpdate();

                conn.commit(); // commit transaction
                return rowsAffected > 0;
            } catch (SQLException e) {
                conn.rollback(); // Rollback hvis der sker en exception
                throw new DataAccessException("Fejl under sletning: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Databaseadgangsfejl: " + e.getMessage());
        }
    }

    protected void addDependency(IJunctionDAO<?, ?> dependency) {
        dependencies.add(dependency);
    }

    /**
     * @return Navnet på den tabel som er associeret ved DAO'en
     */
    protected abstract String getTableName();

    /*
     * SQL udtryk skabeloner
     */
    /**
     * @return SQL udtryk for at indsætte en ny entitet.
     */
    protected abstract String insertSql();

    /**
     * @return SQL udtryk for at opdatere en eksisterende entitet.
     */
    protected abstract String updateSql();

    /**
     * @return SQL udtryk for at slette en entitet.
     */
    protected abstract String deleteSql();

    /*
     * ResultSet til Entity mapping
     */
    /**
     * Konverter et ResultSet row til et entitet objekt.
     *
     * @param rs ResultS
     * @return
     * @throws SQLException
     */
    protected abstract T toEntity(ResultSet rs) throws SQLException;

    /*
     * Entity til PreparedStatement mapping
     */
    protected abstract T setId(T entity, int id);
    protected abstract void setPrimaryKey(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract void setStatement(PreparedStatement stmt, T entity) throws SQLException;
    protected abstract void setUpdateStatementId(PreparedStatement stmt, T original) throws SQLException;

}
