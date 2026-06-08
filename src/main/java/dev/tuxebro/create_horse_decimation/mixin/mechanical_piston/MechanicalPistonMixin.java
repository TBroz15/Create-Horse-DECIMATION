package dev.tuxebro.create_horse_decimation.mixin.mechanical_piston;

import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import dev.tuxebro.create_horse_decimation.interfaces.IMixinMechanicalPistonEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: remove soon when video is done

@Mixin(MechanicalPistonBlock.class)
public class MechanicalPistonMixin {
    @Inject(method = "useItemOn", at= @At("HEAD"))
    protected void useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<ItemInteractionResult> cir) {
        if (!player.mayBuild()) return;
        if (player.isShiftKeyDown()) return;
        if (!stack.isEmpty()) return;

        var self = (MechanicalPistonBlock) (Object) this;

        self.withBlockEntityDo(level, pos, be -> {
            if (!(be instanceof IMixinMechanicalPistonEntity pistonEntity)) return;

            pistonEntity.updateSpeedScale(player);
        });
    }
}
