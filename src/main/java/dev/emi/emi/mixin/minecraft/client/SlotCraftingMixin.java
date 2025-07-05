package dev.emi.emi.mixin.minecraft.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.rewindmc.retroemi.REMIMixinHooks;

@Mixin(SlotCrafting.class)
public class SlotCraftingMixin {
    @Final @Shadow private IInventory craftMatrix;
    @Shadow private EntityPlayer thePlayer;

    @Inject(method = "onCrafting(Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"))
    private void onCraftRenderEMI(ItemStack par1ItemStack, CallbackInfo ci) {
        REMIMixinHooks.onCrafting(this.thePlayer, this.craftMatrix);
    }
}
