package pmc.gui.components.dialog.addcategory;

import pmc.be.Category;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public record CategoryActions(
        Consumer<String> add,
        Consumer<Category> delete,
        BiConsumer<Category, String> update)
{ }