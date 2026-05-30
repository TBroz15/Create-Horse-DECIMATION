package dev.tuxebro.create_horse_decimation.event;

import com.google.common.cache.CacheBuilder;
import dev.tuxebro.create_horse_decimation.CreateHorseDecimation;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@EventBusSubscriber(modid = CreateHorseDecimation.MOD_ID)
public class TempKeepInventoryHandler {
    public static final Set<UUID> players = ConcurrentHashMap.newKeySet();

    private static final ConcurrentMap<UUID, ListTag> playerTempInventories = CacheBuilder.newBuilder()
            .weakKeys()
            .<UUID, ListTag>build()
            .asMap();

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        if (!players.contains(player.getUUID())) return;

        ListTag tag = new ListTag();
        player.getInventory().save(tag);
        playerTempInventories.put(player.getUUID(), tag);
    }

    @SubscribeEvent
    public static void onPlayerDrops(LivingDropsEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (player.level().isClientSide()) return;
        if (!players.contains(player.getUUID())) return;

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        if (!players.contains(oldPlayer.getUUID())) return;
        if (!playerTempInventories.containsKey(oldPlayer.getUUID())) return;

        ListTag tag = playerTempInventories.get(oldPlayer.getUUID());
        newPlayer.getInventory().load(tag);

        playerTempInventories.remove(oldPlayer.getUUID());
        players.remove(oldPlayer.getUUID());
    }
}
