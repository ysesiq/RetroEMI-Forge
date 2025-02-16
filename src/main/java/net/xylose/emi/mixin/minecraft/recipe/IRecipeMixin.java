package net.xylose.emi.mixin.minecraft.recipe;

import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IRecipe.class)
public interface IRecipeMixin {
//    default IRecipe hideFromEMI() {
//        EmiData.hideRecipe(new SyntheticIdentifier((IRecipe) this));
//        return (IRecipe) this;
//    }
}
