package dev.tuxebro.create_horse_decimation.mixin;

import com.simibubi.create.content.kinetics.crusher.CrushingWheelControllerBlockEntity;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import dev.tuxebro.create_horse_decimation.ModBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;

@Mixin(MechanicalPressBlockEntity.class)
public class MechanicalPressBlockEntityMixin {
    @Inject(method = "onItemPressed", at = @At("HEAD"))
    public void onItemPressed(ItemStack result, CallbackInfo ci) {
        if (!result.is(ModBlocks.HORSE_BLOCK.asItem())) return;

        var self = (MechanicalPressBlockEntity) (Object) this;

        var level = self.getLevel();
        var pos = self.getBlockPos();

        if (!(level instanceof ServerLevel serverLevel)) return;

        new ArrayList<>(Collections.nCopies(4, null)).forEach((item) -> {
            serverLevel.playSound(
                    null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    SoundEvents.HORSE_HURT,
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f);
        });


    }
}
