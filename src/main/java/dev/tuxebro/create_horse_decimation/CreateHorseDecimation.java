package dev.tuxebro.create_horse_decimation;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.tuxebro.create_horse_decimation.block.horse_abductor.HorseAbductorBlockEntity;
import dev.tuxebro.create_horse_decimation.config.Config;
import dev.tuxebro.create_horse_decimation.config.ScreenManager;
import dev.tuxebro.create_horse_decimation.datagen.ModDatagen;
import dev.tuxebro.create_horse_decimation.ponders.ModPonderPlugin;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;

@Mod(CreateHorseDecimation.MOD_ID)
public class CreateHorseDecimation {
    public static final String MOD_ID = "create_horse_decimation";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .defaultCreativeTab((ResourceKey<CreativeModeTab>) null) // FIXME: THIS IS TUXEBRO, IF YOU ARE GOING TO COPY MY CODE WHICH I CAN LET YOU, ALWAYS INCLUDE THIS LINE SO YOUR CUSTOM ADDON'S CREATIVE TAB WONT CRASH WHEN ENTERING THE INVENTORY LIKE WHY TF DO I ADD THIS?????????????? I WASTED 3 HOURSE TRYING TO FIX THOSE "ItemStack already exists in the tab's list" ERRORSSSS GRAAAAAAAAAAAAAHHHHHH https://media.tenor.com/zrEcHfcgTNQAAAAe/alpha-wolf-alpha.png
            .setTooltipModifierFactory(item ->
                    new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                            .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
            );

    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public CreateHorseDecimation(IEventBus modEventBus, ModContainer modContainer, Dist dist) {
        LOGGER.info("getting ready for decimating evil and intimidating horses >:)");

        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(EventPriority.HIGHEST, ModDatagen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, ModDatagen::gatherData);
        modEventBus.addListener(RegisterCapabilitiesEvent.class, CreateHorseDecimation::registerCapabilities);

        REGISTRATE.registerEventListeners(modEventBus);

        ModCreativeModeTabs.register(modEventBus);
        Config.register(modContainer);

        ModBlocks.register();
        ModBlockEntityTypes.register();
        ModFluids.register();
        ModItems.register();
        ModSoundEvents.register(modEventBus);

        if (dist == Dist.CLIENT) {
            ScreenManager.registerConfigScreen(modContainer);
        }

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ModPartialModels.register();
        PonderIndex.addPlugin(new ModPonderPlugin());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.HORSE_GLUE.getSource(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.HORSE_GLUE.get(), RenderType.translucent());
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        HorseAbductorBlockEntity.registerCapabilities(event);
    }

    public static CreateRegistrate registrate() {
        if (!STACK_WALKER.getCallerClass().getPackageName().startsWith("dev.tuxebro.create_horse_decimation"))
            throw new UnsupportedOperationException("Other mods are not permitted to use Create: Horse DECIMATION's registrate instance.");
        return REGISTRATE;
    }

    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
