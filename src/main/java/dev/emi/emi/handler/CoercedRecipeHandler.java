package dev.emi.emi.handler;

import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;
import net.xylose.emi.api.EMIInventoryCrafting;
import net.xylose.emi.api.EMISlot;
import net.xylose.emi.api.EMISlotCrafting;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.*;

import java.util.List;

public class CoercedRecipeHandler<T extends Container> implements StandardRecipeHandler<T> {
	private SlotCrafting output;
	private IInventory inv;

	public CoercedRecipeHandler(SlotCrafting output) {
		this.output = output;
		this.inv = ((EMISlotCrafting) output).getCraftMatrix();
	}

	@Override
	public Slot getOutputSlot(Container handler) {
		return output;
	}

	@Override
	public List<Slot> getInputSources(Container handler) {
		Minecraft client = Minecraft.getMinecraft();
		List<Slot> slots = Lists.newArrayList();
		if (output != null) {
			for (Slot slot : (List<Slot>) handler.inventorySlots) {
				if (slot.canTakeStack(client.thePlayer) && slot != output) {
					slots.add(slot);
				}
			}
		}
		return slots;
	}

	@Override
	public List<Slot> getCraftingSlots(Container handler) {
		List<Slot> slots = Lists.newArrayList();
		int width = inv instanceof InventoryCrafting ic ? ((EMIInventoryCrafting) ic).getInventoryWidth() : 3;
		int height = inv.getSizeInventory() / width;
		for (int i = 0; i < 9; i++) {
			slots.add(null);
		}
		for (Slot slot : (List<Slot>) handler.inventorySlots) { //Something about this is broken, not sure what
			if (slot.inventory == inv && ((EMISlot) slot).getSlotIndex() < width * height && ((EMISlot) slot).getSlotIndex() >= 0) {
				int index = ((EMISlot) slot).getSlotIndex();
				index = index * 3 / width;
				slots.set(index, slot);
			}
		}
		return slots;
	}

	@Override
	public boolean supportsRecipe(EmiRecipe recipe) {
		if (recipe.getCategory() == VanillaEmiRecipeCategories.CRAFTING && recipe.supportsRecipeTree()) {
			if (recipe instanceof EmiCraftingRecipe crafting) {
				int width = inv instanceof InventoryCrafting ic ? ((EMIInventoryCrafting) ic).getInventoryWidth() : 3;
				int height = inv.getSizeInventory() / width;
				return crafting.canFit(width, height);
			}
			return true;
		}
		return false;
	}
}
