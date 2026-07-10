package dev.tuxebro.create_horse_decimation.mixin.easter_eggs;

import dev.tuxebro.create_horse_decimation.ModBlocks;
import com.llamalad7.mixinextras.sugar.Local;
import dev.tuxebro.create_horse_decimation.config.Config;
import dev.tuxebro.create_horse_decimation.event.HorseBlockCrasherHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class DetectHorseBlockDestroyMixin {
    @Inject(method = "destroyBlock", at= @At("TAIL"))
    public void destroyBlock(BlockPos pos, boolean dropBlock, Entity entity, int recursionLeft, CallbackInfoReturnable<Boolean> cir, @Local BlockState blockstate) {
        if (Config.server.survivalFriendlyEnabled.get()) return;
        if (blockstate.getBlock() != ModBlocks.HORSE_BLOCK.get()) return;
        HorseBlockCrasherHandler.blocksBrokenThisTick++;
    }
}
