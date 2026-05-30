package dev.tuxebro.create_horse_decimation.mixin;

import dev.tuxebro.create_horse_decimation.utils.HorseCensorInator9000;
import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLanguage.class)
public class AnotherGoofyAhhMixinThatSpecificallyCensorsTheWordHorse {
    @Inject(method = "getOrDefault", at= @At("RETURN"), cancellable = true)
    private static void censorHorse(String p_118920_, String p_265273_, CallbackInfoReturnable<String> cir) {
        final var original = cir.getReturnValue();
        final var censoredText = HorseCensorInator9000.CensorText(original);

        cir.setReturnValue(censoredText);
    }
}
