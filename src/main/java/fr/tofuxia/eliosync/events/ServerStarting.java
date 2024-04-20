package fr.tofuxia.eliosync.events;

import fr.tofuxia.eliosync.Database;
import fr.tofuxia.eliosync.Eliosync;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod.EventBusSubscriber(modid = Eliosync.MODID)
public class ServerStarting {

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        if(event.getServer().isSingleplayer()) return;
        Database.connect();
        Database.prepare();
        if (!Database.isReady()) {
            Eliosync.LOGGER.error(
                    "Database is not ready, stopping the server to prevent data loss/desync, please check the logs for more information.");
            Eliosync.LOGGER.warn("If this is the first time you are running the server, please check the config file.");
            event.getServer().stopServer();
        }

    }

}