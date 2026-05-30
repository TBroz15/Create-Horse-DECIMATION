package dev.tuxebro.create_horse_decimation;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.tuxebro.create_horse_decimation.item.HorseDustItem;
import dev.tuxebro.create_horse_decimation.item.HorseGlueBallItem;
import dev.tuxebro.create_horse_decimation.item.HorseJpgItem;
import net.neoforged.neoforge.common.Tags;

public class ModItems {
    private static final CreateRegistrate REGISTRATE = CreateHorseDecimation.registrate();

    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<HorseDustItem> HORSE_DUST = REGISTRATE.item("horse_dust", HorseDustItem::new)
            .tag(Tags.Items.FOODS)
            .register();

    public static final ItemEntry<HorseJpgItem> HORSE_JPG = REGISTRATE.item("horse_jpg", HorseJpgItem::new)
            .lang("horse.jpg")
            .register();

    public static final ItemEntry<HorseGlueBallItem> HORSE_GLUE_BALL = REGISTRATE.item("horse_glue_ball", HorseGlueBallItem::new)
            .lang("Ball of Horse Glue")
            .tag(Tags.Items.SLIME_BALLS)
            .register();

    public static void register() {}
}
