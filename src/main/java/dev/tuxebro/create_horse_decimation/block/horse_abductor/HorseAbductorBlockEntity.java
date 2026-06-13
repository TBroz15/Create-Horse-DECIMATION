package dev.tuxebro.create_horse_decimation.block.horse_abductor;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import dev.tuxebro.create_horse_decimation.ModBlockEntityTypes;
import dev.tuxebro.create_horse_decimation.ModItems;
import dev.tuxebro.create_horse_decimation.compat.ModCompat;
import dev.tuxebro.create_horse_decimation.config.Config;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

// https://c.tenor.com/f2Wn5IYjODIAAAAd/tenor.gif
public class HorseAbductorBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {
    private final HorseAbductorInventoryHandler inventory = new HorseAbductorInventoryHandler(27, ()->{
        this.setChanged();
        if (level == null) return;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    });

    private final Set<UUID> suckingHorsesID   = new HashSet<>();
    private final Set<UUID> abductingHorsesID = new HashSet<>();
    private int range = 0;

    private CachedSuckinator cachedSuckinator = recalculateSuckinator();

    public HorseAbductorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        setLazyTickRate(5);
    }

    public void serverTick(ServerLevel level) {
        for (UUID horseID : abductingHorsesID) {
            var probablyHorse = level.getEntity(horseID);
            if (probablyHorse == null) continue;
            if (!(probablyHorse instanceof Horse horse)) continue;
            if (horse.isDeadOrDying()) continue;
            if (inventory.isFull()) continue;

            var stack = new ItemStack(ModItems.HORSE_JPG.get());

            CompoundTag entityTag = new CompoundTag();
            horse.saveWithoutId(entityTag);
            String entityTypeId = BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HORSE).toString();
            entityTag.putString("id", entityTypeId);
            CustomData customData = CustomData.of(entityTag);
            stack.set(DataComponents.ENTITY_DATA, customData);

            horse.discard();

            ItemHandlerHelper.insertItem(inventory, stack, false);
            inventory.checkOnChange();

            var addedPitch = (float) (ThreadLocalRandom.current().nextInt(11) * 0.05);

            level.playSound(
                    null,
                    cachedSuckinator.pos,
                    SoundEvents.HORSE_HURT,
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    0.2F, (float) 0.75 + addedPitch
            );

            level.playSound(
                    null,
                    cachedSuckinator.pos,
                    AllSoundEvents.FWOOMP.getMainEvent(),
                    net.minecraft.sounds.SoundSource.BLOCKS,
                    0.6F, (float) 1.5 + addedPitch
            );
        }

        if (suckingHorsesID.isEmpty()) return;

        var globalPos = cachedSuckinator.isInsideOfSubLevel ?
                ModCompat.Sable.getLevelPosFromSubLevelPos(cachedSuckinator.pos, this) :
                cachedSuckinator.pos;
        var globalCenter = VecHelper.getCenterOf(globalPos);

        for (Iterator<UUID> iterator = suckingHorsesID.iterator(); iterator.hasNext();) {
            UUID horseID = iterator.next();

            var horse = level.getEntity(horseID);
            if (horse == null) continue;

            Vec3 diff = horse.position().subtract(globalCenter);
            double horseDistance = diff.length();

            if (horseDistance > range) {
                iterator.remove();
                continue;
            }

            if (horseDistance < 1.5f) continue;

            Vec3 pullVec = diff
                    .normalize()
                    .scale((range - horseDistance) * -1);

            horse.setDeltaMovement(horse.getDeltaMovement()
                    .add(pullVec.scale(getPullScale(range))));
            horse.fallDistance = 0;
            horse.hurtMarked = true;
        }
    }

    public static float getPullScale(int range) {
        if (range <= 15)
            return 1f/24f;

        if (range <= 30)
            return 1f/48f;

        return 1f / 96f;
    }

    @OnlyIn(Dist.CLIENT)
    public void clientTick(Level level) {
        var gameTime = level.getGameTime();

        if (gameTime % Config.client.ticksPerParticle.get() == 0) {
            AbductorParticleSpawner.spawn(
                    level,
                    cachedSuckinator.center,
                    cachedSuckinator.direction.getOpposite(),
                    cachedSuckinator.abductBox,
                    0.3);

            AbductorParticleSpawner.spawn(
                    level,
                    cachedSuckinator.center,
                    cachedSuckinator.direction.getOpposite(),
                    cachedSuckinator.suckBox,
                    getVelocityScaleByRange(range));
        }

        if (!Config.client.ambientSounds.get()) return;
        if (gameTime % 15 == 0)
            level.playLocalSound(getBlockPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 0.25f, 0.5f, false);
        if (gameTime % 5 == 0)
            level.playLocalSound(getBlockPos(), SoundEvents.WITHER_SHOOT, SoundSource.BLOCKS, 0.02f, 0.5f, false);
    }

    public float getVelocityScaleByRange(int range) {
        return (float) range / 15;
    }

    @Override
    public void tick() {
        super.tick();

        if (!isValidToTick()) return;

        if (level instanceof ServerLevel serverLevel)
            serverTick(serverLevel);

        if (level.isClientSide)
            clientTick(level);
    }

    public void lazyServerTick(ServerLevel level) {
        List<Horse> suckingHorses = level.getEntitiesOfClass(Horse.class, cachedSuckinator.suckBox);
        for (Horse horse : suckingHorses) {
            if (horse.isDeadOrDying()) continue;
            suckingHorsesID.add(horse.getUUID());
        }

        List<Horse> abductingHorses = level.getEntitiesOfClass(Horse.class, cachedSuckinator.abductBox);
        for (Horse horse : abductingHorses) {
            if (horse.isDeadOrDying()) continue;
            abductingHorsesID.add(horse.getUUID());
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        suckingHorsesID.clear();
        abductingHorsesID.clear();

        if (!isValidToTick()) return;

        if (level instanceof ServerLevel serverLevel)
            lazyServerTick(serverLevel);

//        if (level instanceof ClientLevel clientLevel)
//            lazyClientTick(clientLevel);
    }

    public boolean isValidToTick() {
        if (level == null) return false;
        if (range == 0) return false;
        if (inventory.isFull()) {
            inventory.checkOnChange();
            return false;
        };

        return true;
    }

    void updateRange(int range) {
        this.range = range;
        this.cachedSuckinator = recalculateSuckinator();
    }

    public int getRange() {
        return this.range;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        inventory.recalcSlots();

        tooltip.add(Component.literal("    Horses: ").append(Component.literal(""+inventory.getOccupiedSlots())));
        tooltip.add(Component.literal("    Range: "+ getRange() +" "));

        return true;
    }

    private record CachedSuckinator(
        Direction direction,
        BlockPos pos,
        Vec3 center,
        boolean isInsideOfSubLevel,

        int width,
        AABB suckBox,

        int abductWidth,
        AABB abductBox
    ) {}

    private CachedSuckinator recalculateSuckinator() {
        var direction = getBlockState().getValue(EncasedFanBlock.FACING);
        var pos = getBlockPos().relative(direction);
        var center = VecHelper.getCenterOf(pos);
        var isInsideOfSubLevel = ModCompat.Sable.isBlockEntityInSubLevel(this);

        Vec3 directionVec = Vec3.atLowerCornerOf(direction.getNormal());
        Vec3 expansionVec = directionVec.scale(range);

        var suckWidth = getRange() / 2;
        var suckBox = new AABB(center, center)
                .expandTowards(expansionVec)
                .inflate(
                        direction.getAxis() == Direction.Axis.X ? 0 : suckWidth,
                        direction.getAxis() == Direction.Axis.Y ? 0 : suckWidth,
                        direction.getAxis() == Direction.Axis.Z ? 0 : suckWidth);

        var abductWidth = 2;
        var abductBox = new AABB(pos)
                .expandTowards(directionVec)
                .inflate(
                        direction.getAxis() == Direction.Axis.X ? 0 : abductWidth,
                        direction.getAxis() == Direction.Axis.Y ? 0 : abductWidth,
                        direction.getAxis() == Direction.Axis.Z ? 0 : abductWidth);

        return new CachedSuckinator(
                direction, pos, center, isInsideOfSubLevel,
                suckWidth, suckBox,
                abductWidth, abductBox
        );
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(tag, registries, clientPacket);

        tag.put("Inventory", inventory.serializeNBT(registries));
        tag.putInt("Range", range);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(tag, registries, clientPacket);

        if (tag.contains("Inventory"))
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));

        if (tag.contains("Range"))
            updateRange(tag.getInt("Range"));
    }

    private static class AbductorParticleSpawner {
        @OnlyIn(Dist.CLIENT)
        public static void spawn(Level level, Vec3 origin, Direction direction, AABB box, double velocityScale) {
            if (!(level instanceof ClientLevel clientLevel)) return;

            RandomSource random = level.getRandom();

            double spawnX = Mth.lerp(random.nextDouble(), box.minX, box.maxX);
            double spawnY = Mth.lerp(random.nextDouble(), box.minY, box.maxY);
            double spawnZ = Mth.lerp(random.nextDouble(), box.minZ, box.maxZ);

            switch (direction) {
                case DOWN  -> spawnY = box.maxY;
                case UP    -> spawnY = box.minY;
                case NORTH -> spawnZ = box.maxZ;
                case SOUTH -> spawnZ = box.minZ;
                case WEST  -> spawnX = box.maxX;
                case EAST  -> spawnX = box.minX;
            }

            Vec3 spawnPoint = new Vec3(spawnX, spawnY, spawnZ);

            spawn(clientLevel, spawnPoint, origin, velocityScale);
        }

        @OnlyIn(Dist.CLIENT)
        private static void spawn(ClientLevel clientLevel, Vec3 posA, Vec3 posB, double velocityScale) {
            Vec3 direction = posB.subtract(posA);
            double distance = direction.length();

            if (distance == 0) return;

            Vec3 velocity = direction.normalize().scale(velocityScale);

            clientLevel.addAlwaysVisibleParticle(
                    ParticleTypes.CLOUD, Config.client.showParticlesFarAway.get(),
                    posA.x, posA.y, posA.z,
                    velocity.x, velocity.y, velocity.z
            );
        }
    }

    public HorseAbductorInventoryHandler inventoryProvider() {
        return inventory;
    }

    public IItemHandler inventoryProvider(@Nullable Direction side) {
        return this.inventory;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntityTypes.HORSE_ABDUCTOR_BLOCK.get(),
                HorseAbductorBlockEntity::inventoryProvider
        );
    }

}
