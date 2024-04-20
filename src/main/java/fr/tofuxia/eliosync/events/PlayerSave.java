package fr.tofuxia.eliosync.events;

import java.util.Optional;
import java.util.UUID;

import fr.tofuxia.eliosync.Config;
import fr.tofuxia.eliosync.Database;
import fr.tofuxia.eliosync.Eliosync;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = Eliosync.MODID)
public class PlayerSave {

    @SubscribeEvent
    public static void onPlayerSave(PlayerEvent.SaveToFile event) {
        UUID uuid = UUID.fromString(event.getPlayerUUID());

        CompoundTag playerData = new CompoundTag();
        event.getEntity().saveWithoutId(playerData);

        Optional<String> storedData = Database.load(uuid);
        if (!storedData.isPresent()) {
            Database.store(uuid, playerData.getAsString());
            return;
        }

        CompoundTag storedTag;
        try {
            storedTag = TagParser.parseTag(storedData.get());
        } catch (Exception e) {
            Eliosync.LOGGER.error(
                    "Failed to parse player data for {}, stopping the server to prevent data loss/desync, please check the logs for more information.",
                    uuid);
            Eliosync.LOGGER.error(e.getMessage());
            Eliosync.LOGGER.debug(storedData.get());
            event.getEntity().getServer().stopServer();
            return;
        }

        if (!Config.saveAbilities)
            playerData.remove("abilities");

        if (!Config.saveAttributes)
            playerData.remove("Attributes");

        if (!Config.saveEnderchest)
            playerData.remove("EnderItems");

        if (!Config.saveGameType)
            playerData.remove("playerGameType");

        if (!Config.saveInventory)
            playerData.remove("Inventory");

        if (!Config.savePosition) {
            playerData.remove("Pos");
            playerData.remove("Motion");
            playerData.remove("Rotation");
            playerData.remove("FallDistance");
            playerData.remove("OnGround");
            playerData.remove("FallFlying");
            playerData.remove("Dimension");
        }

        if (!Config.saveXp) {
            playerData.remove("XpLevel");
            playerData.remove("XpP");
            playerData.remove("XpSeed");
            playerData.remove("XpTotal");
        }

        storedTag = storedTag.merge(playerData);
        Database.store(uuid, storedTag.getAsString());

    }

}
