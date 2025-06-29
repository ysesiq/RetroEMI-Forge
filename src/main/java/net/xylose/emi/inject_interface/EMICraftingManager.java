package net.xylose.emi.inject_interface;

import java.util.List;

public interface EMICraftingManager {
    default List getRecipes() {
        return null;
    }
}
