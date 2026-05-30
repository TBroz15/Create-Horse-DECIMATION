package dev.tuxebro.create_horse_decimation.event;

import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import dev.tuxebro.create_horse_decimation.config.Config;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber
public class HorseBlockCrasherHandler {
    public static int blocksBrokenThisTick = 0;
    private static final int THRESHOLD = 6720;

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (Config.server.survivalFriendlyEnabled.get()) return;

        if (blocksBrokenThisTick >= THRESHOLD) {
            crash();
        }

        blocksBrokenThisTick = 0;
    }

    private static void crash() {
        CreateHorseDecimation.LOGGER.error("{} Horse Blocks are broken in one tick! time to explode and combust into flames", blocksBrokenThisTick);

        String[] errorMsg = {
                "conspicuous amount of destroyed honse blocs crashed the servr",
                "[PLEASE READ ME NOW!!!] btw that crash a direct reference to this video: https://youtu.be/75P6mlA-KEI?t=1393 (ofc the creator of Create: Horse DECIMATION is a big yahiamice fan)",
                "",
                "if you dont want this:",
                "1. Go to your minecraft's (or your dedicated server's) directory",
                "2. then open /config/create_horse_decimation-server.toml",
                "3. replace \"survivalFriendlyEnabled = false\" with \"survivalFriendlyEnabled = true\"",
                "4. finally restart your instance/server",
                "",
                "pls do not go to my github repository and complain this exact issue, in which i made this as an stupid easter egg lmao"
        };

        CrashReport crashreport = CrashReport.forThrowable(
                new RuntimeException(errorMsg[0]),
                String.join("\n", errorMsg)
        );

        throw new ReportedException(crashreport);
    }
}
