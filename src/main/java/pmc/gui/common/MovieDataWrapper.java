package pmc.gui.common;

import pmc.gui.components.categories.CategoriesModel;
import pmc.gui.components.genres.GenresModel;

public class MovieDataWrapper {
    public enum Type {
        GENRE,
        CATEGORY
    }

    private Type dataType;
    private GenresModel genreModel;
    private CategoriesModel categoryModel;

    public MovieDataWrapper(GenresModel genresModel) {
        this.dataType = Type.GENRE;
        this.genreModel = genresModel;
    }

    public MovieDataWrapper(CategoriesModel categoryModel) {
        this.dataType = Type.CATEGORY;
        this.categoryModel = categoryModel;
    }

    public Type getDataType() {
        return dataType;
    }

    public GenresModel getGenreModel() {
        return genreModel;
    }

    public CategoriesModel getCategoryModel() {
        return categoryModel;
    }
}