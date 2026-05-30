package dev.tuxebro.create_horse_decimation.ponders;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.logistics.depot.EjectorBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.tuxebro.create_horse_decimation.ModBlocks;
import dev.tuxebro.create_horse_decimation.ModItems;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class ModPonders {
    public static void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?,?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.addStoryBoard(AllBlocks.CRUSHING_WHEEL, "crushing_horses", ModPonders::crushingHorses);
        HELPER.addStoryBoard(ModBlocks.HORSE_ABDUCTOR_BLOCK, "abducting_horses", ModPonders::horseAbducting);
    }

    public static void horseAbducting(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("abducting_horses", "Abducting Horses");

        scene.world().showSection(util.select().fromTo(0, 0, 0, 4,0,4), Direction.UP);

        List<ElementLink<EntityElement>> evilAssHorses = new ArrayList<>();
        evilAssHorses.add(createEvilAssHorse(scene, util, 2, 0, 2, 180));
        evilAssHorses.add(createEvilAssHorse(scene, util, 3, 0, 1, 135));
        evilAssHorses.add(createEvilAssHorse(scene, util, 0, 0, 1, 195));

        scene.overlay().showText(60)
                .pointAt(new Vec3(2, 2, 2))
                .placeNearTarget()
                .text("This is a herd of evil and intimidating Horses.");

        scene.idle(60);
        scene.addKeyframe();

        scene.world().showSection(util.select().fromTo(2, 0, 4, 2,2,4), Direction.NORTH);

        scene.overlay().showText(80)
                .pointAt(new Vec3(2, 3.5, 4))
                .placeNearTarget()
                .text("To power it, Horse Abductors requires powered Encased Fans that face in front of any vents.");

        scene.idle(80);

        scene.world().showSection(util.select().fromTo(2, 3, 4, 2,3,4), Direction.NORTH);
        scene.idle(2);
        scene.world().showSection(util.select().fromTo(2, 4, 4, 2,4,4), Direction.NORTH);
        scene.idle(2);
        scene.world().showSection(util.select().fromTo(2, 4, 5, 2,0,5), Direction.NORTH);

        scene.effects()
                .emitParticles(
                        new Vec3(2.5, 2.5, 0),
                        scene.effects().simpleParticleEmitter(ParticleTypes.CLOUD, new Vec3(0,0,0.3)), 2, 30);

        scene.idle(20);
        scene.addKeyframe();

        scene.world().showSection(util.select().fromTo(1, 0, 4, 1,4,4), Direction.NORTH);

        var suckinatorPos = new Vec3(2.5,1.5,3.5);

        yeet(scene, evilAssHorses.getFirst(), new Vec3(2.5,1,2.5), suckinatorPos, 1, 10);
        scene.world().modifyEntity(evilAssHorses.getFirst(), Entity::discard);

        yeet(scene, evilAssHorses.get(1), new Vec3(3.5,1,1.5), suckinatorPos, 1, 10);
        scene.world().modifyEntity(evilAssHorses.get(1), Entity::discard);

        yeet(scene, evilAssHorses.getLast(), new Vec3(0.5,1,1.5), suckinatorPos, 1, 10);
        scene.world().modifyEntity(evilAssHorses.getLast(), Entity::discard);

        var depotPos = util.grid().at(1,1,4);

        scene.idle(10);

        ItemStack stack = new ItemStack(ModItems.HORSE_JPG.get());
        scene.world()
                .createItemOnBeltLike(depotPos, Direction.NORTH, stack);

        scene.idle(10);

        scene.overlay().showText(40)
                .pointAt(new Vec3(1, 2.5, 4))
                .placeNearTarget()
                .text("horse image");

        scene.idle(40);
        scene.markAsFinished();
    }

    public static void crushingHorses(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("crushing_horses", "Decimating Horses with Crushing Wheels");
        scene.configureBasePlate(0,0,5);

        scene.world().showSection(util.select().fromTo(0, 0, 0, 4,0,4), Direction.UP);

        ElementLink<EntityElement> evilAssHorse =
                createEvilAssHorse(scene, util, 2,0,2, 180);

        scene.overlay().showText(40)
                .pointAt(new Vec3(2, 2, 2))
                .placeNearTarget()
                .text("This is an evil and intimidating Horse.");
        scene.idle(40);

        scene.overlay().showText(80)
                .independent(0)
                .text("The goal of Create: Horse DECIMATION is to...");
        scene.idle(30);

        scene.overlay().showText(50)
                .independent(32)
                .text("y'know, decimate evil and intimidating horses?");
        scene.idle(50);

        scene.idle(5);

        scene.addKeyframe();

        scene.world().showSection(util.select().fromTo(3, 1, 1, 1,3,4), Direction.UP);

        yeet(builder, evilAssHorse,
                new Vec3(2,0.5,2),
                new Vec3(2,2,1),1, 5);

        scene.idle(10);
        scene.world().modifyBlockEntity(util.grid().at(2,1,1), EjectorBlockEntity.class, ejector -> ejector.activateDeferred());

        yeet(builder, evilAssHorse,
                new Vec3(2,2,1),
                new Vec3(2,3,3),3, 20);

        scene.overlay().showText(54)
                .pointAt(new Vec3(2, 3.5, 3))
                .placeNearTarget()
                .text("Crushing horses will drop...");

        scene.world().modifyEntity(evilAssHorse, horse -> {
            horse.animateHurt(1);
        });

        scene.idle(12);

        scene.world()
                .hideSection(util.select()
                        .fromTo(3, 1, 3, 2,1,1), Direction.DOWN);

        var depotPos = util.grid().at(2,0,3);
        scene.world()
                .setBlock(depotPos, AllBlocks.DEPOT.get().defaultBlockState(), true);

        scene.world().modifyEntity(evilAssHorse, horse -> {
            horse.animateHurt(1);
        });

        scene.idle(12);
        scene.world().modifyEntity(evilAssHorse, horse -> {
            horse.animateHurt(1);

        });

        scene.idle(12);
        scene.world().modifyEntity(evilAssHorse, horse ->
                horse.animateHurt(1));

        scene.idle(12);
        scene.world().modifyEntity(evilAssHorse, Entity::discard);

        scene.effects()
                .emitParticles(
                        new Vec3(2, 4, 3),
                        scene.effects().simpleParticleEmitter(ParticleTypes.POOF, Vec3.ZERO), 15, 1);

        ItemStack stack = new ItemStack(ModItems.HORSE_DUST.get());

        scene.world()
                .createItemOnBeltLike(depotPos, Direction.NORTH, stack);

        scene.idle(20);

        scene.overlay()
                .showControls(util.vector().topOf(depotPos), Pointing.UP, 40)
                .withItem(stack);

        scene.overlay().showText(40)
                .pointAt(new Vec3(2, 1.5, 3))
                .placeNearTarget()
                .text("horse dust");

        scene.idle(40);

        scene.markAsFinished();
        // Animation code
    }

    private static ElementLink<EntityElement> createEvilAssHorse(CreateSceneBuilder scene, SceneBuildingUtil util,
                                                                   int x, int y, int z, int yRot) {
        return scene.world().createEntity(w -> {
            Horse horse = EntityType.HORSE.create(w);
            if (horse == null) return horse;

            Vec3 p = util.vector().topOf(util.grid().at(x,y,z));

            horse.setPos(p.x, p.y, p.z);
            horse.xo = p.x;
            horse.yo = p.y;
            horse.zo = p.z;

            horse.yRotO = yRot;
            horse.setYRot(yRot);
            horse.yHeadRotO = yRot;
            horse.yHeadRot = yRot;

            return horse;
        });
    }

    public static void yeet(SceneBuilder scene, ElementLink<EntityElement> entityLink, Vec3 start, Vec3 end, double peakHeight, int durationTicks) {
        for (int i = 1; i <= durationTicks; i++) {
            double t = (double) i / durationTicks; // Progress from 0.0 to 1.0

            // Linear interpolation for X and Z axes
            double x = start.x + (end.x - start.x) * t;
            double z = start.z + (end.z - start.z) * t;

            // Parabola formula for Y axis height
            double yBase = start.y + (end.y - start.y) * t;
            double arcY = 4 * peakHeight * t * (1 - t);
            double y = yBase + arcY;

            // Move 1 step
            scene.world().modifyEntity(entityLink, horse -> {
                horse.setPos(x,y,z);
            });

            scene.idle(1);
        }
    }
}
