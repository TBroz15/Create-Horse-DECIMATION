package dev.tuxebro.create_horse_decimation.mixin;

import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// one hell of a way to detect horse sounds
@Mixin(PressingBehaviour.class)
public class MechanicalPressBlockEntityMixin {
    @Inject(method = "applyOnBasin", at = @At(value = "HEAD"))
    public void makeHorseSoundOnPress(CallbackInfo ci) {
        var self = (PressingBehaviour) (Object) this;
        var level = self.blockEntity.getLevel();
        var pos = self.getPos();

        if (level == null) return;

        BlockEntity blockEntity = level.getBlockEntity(pos.below(2));
        if (blockEntity == null) return;
        if (!(blockEntity instanceof BasinBlockEntity basin)) return;

        var basinInv = basin.getInputInventory();
        var hasHorseJPG = basinInv.getItem(0).is(ModItems.HORSE_JPG);

        if (!hasHorseJPG) return;

        if (!(level instanceof ServerLevel serverLevel)) return;

        serverLevel.playSound(
                null,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                SoundEvents.HORSE_HURT,
                SoundSource.BLOCKS,
                0.5f,
                1.0f);
    }
}


