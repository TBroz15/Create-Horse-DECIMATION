package dev.tuxebro.create_horse_decimation.config;

import dev.tuxebro.create_horse_decimation.ModBlocks;
import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class ClientConfig extends ConfigBase {
    public final ConfigGroup client =
            group(0, "client", "Client Config");

    public final ConfigBool censorHorseWord =
            b(true, "censorHorseWord", Comments.censorHorseWord);

    public final ConfigBool greetSpecialPlayersOnBoot =
            b(true, "greetSpecialPlayersOnBoot", Comments.greetSpecialPlayersOnBoot);



    public final ConfigGroup horseAbductor = group(1, "horseAbductor", "Horse Abductor 9000");

    public final ConfigBool showParticlesFarAway =
            b(true, "showParticlesFarAway", Comments.showParticlesFarAway);

    public final ConfigBool ambientSounds =
            b(true, "ambientSounds", Comments.ambientSounds);

    public final ConfigInt ticksPerParticle =
            i(1, 1, 1000, "ticksPerParticle", Comments.ticksPerParticle);

    @Override @NotNull
    public String getName() {
        return "client";
    }

    private static class Comments {
        private static final String[] censorHorseWord = {
                "Censors the word \"horse\" into \"h*rse.\"",
                "This applies to almost EVERY possible rendering text of Minecraft.",
                "Restart for full effect!"
        };

        private static final String[] greetSpecialPlayersOnBoot = {
                "Greet some special players through the splash screen on boot.",
        };

        private static final String[] showParticlesFarAway = {
                "Whether to show abducting particles far away.",
                "Recommended to disable this for better FPS performance.",
        };

        private static final String[] ambientSounds = {
                "Whether to make ambient abducting sounds.",
        };

        private static final String[] ticksPerParticle = {
                "The amount of delayed ticks to spawn a particle.",
        };
    }
}
