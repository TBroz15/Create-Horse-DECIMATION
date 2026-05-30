package dev.tuxebro.create_horse_decimation.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class ClientConfig extends ConfigBase {
    public final ConfigGroup client =
            group(0, "client", "Client Config");

    public final ConfigBool censorHorseWord =
            b(true, "censorHorseWord", Comments.censorHorseWord);

    public final ConfigBool greetSpecialPlayersOnBoot =
            b(true, "greetSpecialPlayersOnBoot", Comments.greetSpecialPlayersOnBoot);

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
    }
}
