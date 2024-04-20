package fr.tofuxia.eliosync;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Eliosync.MODID)
public class Eliosync {

    public static final String MODID = "eliosync";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Eliosync(IEventBus modEventBus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

}
