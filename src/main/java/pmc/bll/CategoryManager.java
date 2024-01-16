package pmc.bll;

import pmc.be.Category;
import pmc.dal.database.common.IDAO;
import pmc.dal.database.daos.CategoryDAO_DB;
import pmc.dal.exception.DataAccessException;
import pmc.utils.MovieException;

import java.util.List;
import java.util.Optional;

public class CategoryManager {
    private IDAO<Category> dao;

    public CategoryManager() throws MovieException {
        try {
            this.dao = new CategoryDAO_DB();
        } catch (DataAccessException e) {
            throw new MovieException(e.getMessage());
        }
    }

    public Optional<Category> getCategory(int id) throws MovieException {
        try {
            return dao.get(id);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke hente kategory fra ID\n" + e.getMessage());
        }
    }

    public List<Category> getAllCategories() throws MovieException {
        try {
            return dao.getAll();
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke hente alle kategorier\n" + e.getMessage());
        }
    }

    public Category addCategory(Category category) throws MovieException {
        try {
            return dao.add(category);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke oprette ny kategori\n" + e.getMessage());
        }
    }

    public boolean updateCategory(Category orginal, Category updatedData) throws MovieException {
        try {
            return dao.update(orginal, updatedData);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke opdatere kategori\n" + e.getMessage());
        }
    }

    public boolean deleteCategory(Category category) throws MovieException {
        try {
            return dao.delete(category);
        } catch (DataAccessException e) {
            throw new MovieException("Kunne ikke slette kategori\n" + e.getMessage());
        }
    }
}
