package pmc.dal.database.common;

import pmc.dal.exception.DataAccessException;

import java.util.List;

public interface IJunctionDAO<T, U> {
    void addRelation(T t, U u) throws DataAccessException;
    void removeRelation(T t, U u) throws DataAccessException;
    List<U> getUForT(T t) throws DataAccessException;
    List<T> getTForU(U u) throws DataAccessException;
    void deleteAssociationsForEntity(Object entity) throws DataAccessException;
}