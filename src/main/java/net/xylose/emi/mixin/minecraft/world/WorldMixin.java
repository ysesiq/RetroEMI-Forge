package net.xylose.emi.mixin.minecraft.world;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.rewindmc.retroemi.RetroEMI;

@Mixin(World.class)
public class WorldMixin {
    @Inject(method = "tick", at = @At("RETURN"))
    public void tickEMI(CallbackInfo ci) {
        RetroEMI.tick();
    }
}
