package dev.tuxebro.create_horse_decimation.mixin;

import dev.tuxebro.create_horse_decimation.utils.HorseCensorInator9000;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Component.class)
public interface GoofyAhhMixinThatSpecificallyCensorsTheWordHorse {
    @Inject(method = "literal", at= @At("HEAD"), cancellable = true)
    private static void censorHorse(String text, CallbackInfoReturnable<MutableComponent> cir) {
        cir.setReturnValue(HorseCensorInator9000.CensorLiteral(text));
    }
}
