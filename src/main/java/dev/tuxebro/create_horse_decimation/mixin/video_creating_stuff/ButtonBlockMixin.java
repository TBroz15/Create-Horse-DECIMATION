package dev.tuxebro.create_horse_decimation.mixin.video_creating_stuff;

import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.world.level.block.ButtonBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ButtonBlock.class)
public class ButtonBlockMixin {
    @Final
    @Shadow
    @Mutable
    private int ticksToStayPressed;

    @Inject(method = "press", at = @At("HEAD"))
    private void onPress(CallbackInfo ci) {
        this.ticksToStayPressed = Config.server.ticksToStayPressed.get();
    }
}
