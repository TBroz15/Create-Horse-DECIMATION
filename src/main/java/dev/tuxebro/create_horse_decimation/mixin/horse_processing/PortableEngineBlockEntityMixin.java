package dev.tuxebro.create_horse_decimation.mixin.horse_processing;

import dev.simulated_team.simulated.content.blocks.portable_engine.PortableEngineBlockEntity;
import dev.simulated_team.simulated.content.blocks.portable_engine.PortableEngineInventory;
import dev.tuxebro.create_horse_decimation.ModItems;
import dev.tuxebro.create_horse_decimation.ModLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

// TODO: buff horse dust for portable engines
@Mixin(PortableEngineBlockEntity.class)
public class PortableEngineBlockEntityMixin {
//    @Shadow
//    public PortableEngineInventory inventory;
//
//    @Unique
//    public boolean createHorseDecimation$isHorsePowered() {
//        return this.inventory.slot.getStack().is(ModItems.HORSE_DUST.get());
//    }
//
//    @Inject(method = "getGeneratedSpeed", at = @At("RETURN"), cancellable = true)
//    public void buffHorseDustSpeed(CallbackInfoReturnable<Float> cir) {
//        final float BUFFED_RATIO = 2.5f;
//        float generatedSpeed = cir.getReturnValue();
//
//        cir.setReturnValue(generatedSpeed * (createHorseDecimation$isHorsePowered() ? BUFFED_RATIO : 1f));
//    }
//
//    @Inject(method = "addToGoggleTooltip", at = @At("RETURN"))
//    public void addHorseInfoGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking, CallbackInfoReturnable<Boolean> cir) {
//        if (!createHorseDecimation$isHorsePowered()) return;
//        final Component component = Component
//                .literal("Horse Powered")
//                .withStyle(ChatFormatting.GOLD);
//
//        ModLang.builder().add(component).forGoggles(tooltip);
//    }
}
