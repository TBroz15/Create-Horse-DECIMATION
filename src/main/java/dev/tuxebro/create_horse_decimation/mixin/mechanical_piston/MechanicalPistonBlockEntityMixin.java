package dev.tuxebro.create_horse_decimation.mixin.mechanical_piston;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlockEntity;
import dev.tuxebro.create_horse_decimation.interfaces.IMixinMechanicalPistonEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// TODO: remove soon when video is done

@Mixin(MechanicalPistonBlockEntity.class)
public class MechanicalPistonBlockEntityMixin implements IMixinMechanicalPistonEntity {
    protected float movementSpeedScale = 1;

    @Override
    public void updateSpeedScale(Player player) {
        movementSpeedScale += 0.25f;

        if (movementSpeedScale >= 10.25f) {
            movementSpeedScale = 1f;
            player.playSound(SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 1.0F, 1.0F);
        }

        if (movementSpeedScale <= 0.75f) {
            movementSpeedScale = 10f;
            player.playSound(SoundEvents.NOTE_BLOCK_DIDGERIDOO.value(), 1.0F, 1.0F);
        }

        player.playSound(SoundEvents.NOTE_BLOCK_BELL.value(), 1.0F, 1.0F + (movementSpeedScale / 10f));

        player.displayClientMessage(Component.literal("Movement Speed Scale: " + movementSpeedScale), true);
    }

    @WrapOperation(method = "getMovementSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 0))
    public float scaleMovementSpeed(float value, float min, float max, Operation<Float> original) {
        return original.call(value, min, max) * movementSpeedScale;
    }
}
