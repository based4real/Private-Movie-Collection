package pmc.bll;

import pmc.be.Category;
import pmc.dal.database.common.IDAO;
import pmc.dal.database.daos.CategoryDAO_DB;
import pmc.dal.exception.DataAccessException;
import pmc.utils.PMCException;

import java.util.List;
import java.util.Optional;

public class CategoryManager {
    private IDAO<Category> dao;

    public CategoryManager() throws PMCException {
        try {
            this.dao = new CategoryDAO_DB();
        } catch (DataAccessException e) {
            throw new PMCException(e.getMessage());
        }
    }

    public Optional<Category> getCategory(int id) throws PMCException {
        try {
            return dao.get(id);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke hente kategory fra ID\n" + e.getMessage());
        }
    }

    public List<Category> getAllCategories() throws PMCException {
        try {
            return dao.getAll();
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke hente alle kategorier\n" + e.getMessage());
        }
    }

    public boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty() || name.trim().matches(" "))
            return false;

        return true;
    }

    private Category trimName(Category category) {
        category.setName(category.getName().trim());
        return category;
    }

    public Category addCategory(Category category) throws PMCException {
        try {
            return dao.add(trimName(category));
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke oprette ny kategori\n" + e.getMessage());
        }
    }

    public boolean updateCategory(Category orginal, Category updatedData) throws PMCException {
        try {
            return dao.update(orginal, trimName(updatedData));
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke opdatere kategori\n" + e.getMessage());
        }
    }

    public boolean deleteCategory(Category category) throws PMCException {
        try {
            return dao.delete(category);
        } catch (DataAccessException e) {
            throw new PMCException("Kunne ikke slette kategori\n" + e.getMessage());
        }
    }
}
