package dev.tuxebro.create_horse_decimation.mixin;

import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.User;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.client.resources.SplashManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SplashManager.class)
public class GreetPlayerOnBootSplashTextMixin {
    @Unique
    private boolean createHorseDecimation$isPlayerGreeted = false;

    @Inject(method = "getSplash", at=@At("RETURN"), cancellable = true)
    public void getSplash(CallbackInfoReturnable<SplashRenderer> cir) {
        if (!Config.client.greetSpecialPlayersOnBoot.get()) return;

        if (createHorseDecimation$isPlayerGreeted) return;
        createHorseDecimation$isPlayerGreeted = true;

        var player = Minecraft.getInstance().getUser();
        String greeting = createHorseDecimation$getGreetingByPlayer(player);

        if (greeting.isEmpty()) return;

        cir.setReturnValue(new SplashRenderer(greeting));
    }

    @Unique
    private String createHorseDecimation$getGreetingByPlayer(User player) {
        var playerName = player.getName().toLowerCase();

        if (playerName.contains("decimat")) {
            if (playerName.contains("horse")) return "mod so good, they REALLY named it after you";
            return "mod so good, they (almost) named it after you";
        }

        if (playerName.contains("horse")) {
            return "r u sure you want to decimate your own beings?";
        }

        if (playerName.contains("mod") && playerName.contains("name")) {
            return "omg its called \"Create: Horse DECIMATION\"";
        }

        return switch (playerName) {
            case "dev" -> "hello day vay loper";
            case "tuxebro" -> "hello took see bronze";
            case "tbroz15" -> "you sick fake, \"TuxeBro\" is the REAL TuxeBro";
            case "shalz_" -> "hello salt";
            case "dejojotheawsome" -> "hello the johnathan johnathan the awsome";
            case "rageplaysgames" -> "hello fury fiddles digital entertainment";
            case "yahiamice" -> "hello yaha mouse";
            case "smallishbeans" -> "hello tiny legumes";
            case "jeb_" -> "hello john minecraft, pls fix bedrock :(";
            default -> "";
        };
    }
}
