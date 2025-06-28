package dev.emi.emi.mixin.minecraft.client;

import net.xylose.emi.api.EMICraftingManager;
import net.minecraft.item.crafting.CraftingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(CraftingManager.class)
public class CraftingManagerMixin implements EMICraftingManager {
    @Shadow private List recipes;

    @Override
    public List getRecipes() {
        return recipes;
    }
}
