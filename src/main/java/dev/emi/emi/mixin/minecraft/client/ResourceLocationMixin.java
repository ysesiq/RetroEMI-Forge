package dev.emi.emi.mixin.minecraft.client;

import net.xylose.emi.api.EMIResourceLocation;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ResourceLocation.class)
public class ResourceLocationMixin implements EMIResourceLocation {
    @Mutable @Final @Shadow private final String resourceDomain;
    @Mutable @Final @Shadow private final String resourcePath;

    public ResourceLocationMixin(String resourceDomain, String resourcePath) {
        this.resourceDomain = resourceDomain;
        this.resourcePath = resourcePath;
    }

    @Override
    public int compareTo(ResourceLocation that) {
        int i = this.resourcePath.compareTo(that.getResourcePath());
        if (i != 0) return i;
        return this.resourceDomain.compareTo(that.getResourceDomain());
    }

//    @Inject(method = "exists", at = @At("RETURN"), cancellable = true)
//    public void exists(CallbackInfoReturnable<Boolean> cir) {
//        cir.setReturnValue(true);
//    }
}
