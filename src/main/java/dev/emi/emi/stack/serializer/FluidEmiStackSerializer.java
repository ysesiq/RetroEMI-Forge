package dev.emi.emi.stack.serializer;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.stack.FluidEmiStack;
import dev.emi.emi.api.stack.serializer.EmiStackSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class FluidEmiStackSerializer implements EmiStackSerializer<FluidEmiStack> {

	@Override
	public String getType() {
		return "fluid";
	}

	@Override
	public EmiStack create(ResourceLocation id, NBTTagCompound componentChanges, long amount, int subtype) {
		return EmiStack.of(EmiPort.getFluidRegistry().get(id), componentChanges, amount);
	}
}
