package dev.tuxebro.create_horse_decimation.config;

import net.createmod.catnip.config.ConfigBase;
import org.jetbrains.annotations.NotNull;

public class ServerConfig extends ConfigBase {
    public final ConfigBase.ConfigGroup server =
            group(0, "server", "Server Config");

    public final ConfigBool survivalFriendlyEnabled =
            b(false, "survivalFriendlyEnabled", Comments.survivalFriendlyEnabled);

    public final ConfigBool horseDupe =
            b(false, "horseDupeGlitchWorkingUnpatchedWowzersDangWutDaHailOmgNoWay", Comments.horseDupe);

    public final ConfigGroup crushingWheels = group(1, "crushingWheels", "Crushing Wheels");

    public final ConfigInt ticksPerHorseDetection =
            i(5,1,100, "ticksPerHorseDetection", Comments.ticksPerHorseDetection);

    @Override @NotNull
    public String getName() {
        return "server";
    }

    private static class Comments {
        private static final String[] survivalFriendlyEnabled = {
                "Makes the mod boring and not so cool",
                "(cus someone complained for uhh, unnatural stuff)",
                "(just figure it out why i made this as an option)"
        };

        private static final String[] horseDupe = {
                "Horse Abductor will dupe horses when moved",
                "(such as pistons, simulated contraptions, etc.)"
        };

        private static final String[] ticksPerHorseDetection = {
                "The amount of delayed ticks to detect horses to crush.",
        };
    }
}
