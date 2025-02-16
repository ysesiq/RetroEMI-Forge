package net.xylose.emi.mixin.minecraft.block;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.data.EmiRemoveFromIndex;
import net.xylose.emi.api.EMIBlock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin implements EMIBlock {

//    @Override
//    public Blocks hideFromEMI() {
//        if (MixinEnvironment.getCurrentEnvironment().equals((MixinEnvironment.Side.CLIENT))) {
//            for (int i = 0; i < 16; i++) {
//                EmiRemoveFromIndex.removed.add(EmiStack.of(new ItemStack((Block) (Object) this, 1, i)));
//            }
//        }
//        return (Blocks) (Object) this;
//    }
//
//    @Override
//    public Blocks hideFromEMI(int metadata) {
//        if (MixinEnvironment.getCurrentEnvironment().equals((MixinEnvironment.Side.CLIENT))) {
//            EmiRemoveFromIndex.removed.add(EmiStack.of(new ItemStack((Block) (Object) this, 1, metadata)));
//        }
//        return (Blocks) (Object) this;
//    }
//
//    @Inject(method = "registerBlocks", at = @At("TAIL"))
//    private static void addBlockHideFromEMI(CallbackInfo callback) {
//        ((EMIBlock) Blocks.flowing_water).hideFromEMI();
//        ((EMIBlock) Blocks.water).hideFromEMI();
//        ((EMIBlock) Blocks.flowing_lava).hideFromEMI();
//        ((EMIBlock) Blocks.lava).hideFromEMI();
//        ((EMIBlock) Blocks.piston_extension).hideFromEMI();
//        ((EMIBlock) Blocks.piston_head).hideFromEMI();
//        ((EMIBlock) Blocks.double_stone_slab).hideFromEMI();
//        ((EMIBlock) Blocks.bed).hideFromEMI();
//        ((EMIBlock) Blocks.brown_mushroom_block).hideFromEMI();
//        ((EMIBlock) Blocks.red_mushroom_block).hideFromEMI();
//        ((EMIBlock) Blocks.redstone_wire).hideFromEMI();
//        ((EMIBlock) Blocks.wheat).hideFromEMI();
//        ((EMIBlock) Blocks.farmland).hideFromEMI();
//        ((EMIBlock) Blocks.lit_furnace).hideFromEMI();
//        ((EMIBlock) Blocks.standing_sign).hideFromEMI();
//        ((EMIBlock) Blocks.wooden_door).hideFromEMI();
//        ((EMIBlock) Blocks.wall_sign).hideFromEMI();
//        ((EMIBlock) Blocks.iron_door).hideFromEMI();
//        ((EMIBlock) Blocks.lit_redstone_ore).hideFromEMI();
//        ((EMIBlock) Blocks.unlit_redstone_torch).hideFromEMI();
//        ((EMIBlock) Blocks.reeds).hideFromEMI();
//        ((EMIBlock) Blocks.cake).hideFromEMI();
//        ((EMIBlock) Blocks.powered_repeater).hideFromEMI();
//        ((EMIBlock) Blocks.unpowered_repeater).hideFromEMI();
//        ((EMIBlock) Blocks.pumpkin_stem).hideFromEMI();
//        ((EMIBlock) Blocks.melon_stem).hideFromEMI();
//        ((EMIBlock) Blocks.red_mushroom_block).hideFromEMI();
//        ((EMIBlock) Blocks.brown_mushroom_block).hideFromEMI();
//        ((EMIBlock) Blocks.end_portal).hideFromEMI();
//        ((EMIBlock) Blocks.brewing_stand).hideFromEMI();
//        ((EMIBlock) Blocks.cauldron).hideFromEMI();
//        ((EMIBlock) Blocks.lit_redstone_lamp).hideFromEMI();
//        ((EMIBlock) Blocks.redstone_lamp).hideFromEMI();
//        ((EMIBlock) Blocks.double_wooden_slab).hideFromEMI();
//        ((EMIBlock) Blocks.carrots).hideFromEMI();
//        ((EMIBlock) Blocks.potatoes).hideFromEMI();
//        ((EMIBlock) Blocks.skull).hideFromEMI();
//        ((EMIBlock) Blocks.flower_pot).hideFromEMI();
//        ((EMIBlock) Blocks.deadbush).hideFromEMI();
//        ((EMIBlock) Blocks.nether_wart).hideFromEMI();
//        ((EMIBlock) Blocks.tripwire).hideFromEMI();
//        ((EMIBlock) Blocks.cocoa).hideFromEMI();
//        ((EMIBlock) Blocks.unpowered_comparator).hideFromEMI();
//        ((EMIBlock) Blocks.portal).hideFromEMI();
//    }
}
