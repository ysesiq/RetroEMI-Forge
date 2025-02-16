package net.xylose.emi.mixin.minecraft.client;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBlocks.class)
public class RenderBlockMixin {
    @Shadow @Final public Minecraft minecraftRB;

    @Inject(method = "setRenderBoundsFromBlock", at = @At("HEAD"))
    public void pause(Block block, CallbackInfo ci) {
        if (this.minecraftRB == null) {
            int x = 0;
            x ++;
        }
    }

}
