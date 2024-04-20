package fr.tofuxia.eliosync.events;

import java.util.Optional;
import java.util.UUID;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import fr.tofuxia.eliosync.Config;
import fr.tofuxia.eliosync.Database;
import fr.tofuxia.eliosync.Eliosync;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event.Result;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = Eliosync.MODID)
public class PlayerLoad {

    @SubscribeEvent
    public static void onPlayerLoad(PlayerEvent.LoadFromFile event) {
        CompoundTag playerData = null;
        UUID uuid = UUID.fromString(event.getPlayerUUID());

        if (!Database.contains(uuid)) {
            Eliosync.LOGGER.warn("Player data for {} not found in the database, creating a new entry.", uuid);
            playerData = new CompoundTag();
            playerData = event.getEntity().saveWithoutId(playerData);
            Database.store(uuid, playerData.getAsString());
            return;
        }

        Eliosync.LOGGER.debug("Loading player data for {} from the database.", uuid);

        Optional<String> storedData = Database.load(uuid);
        Eliosync.LOGGER.debug("Loaded data: {}", storedData.isPresent());
        if (!storedData.isPresent()) {
            Eliosync.LOGGER.error(
                    "Failed to load player data for {}, stopping the server to prevent data loss/desync, please check the logs for more information.",
                    uuid);
            event.getEntity().getServer().stopServer();
            return;
        }

        try {
            playerData = TagParser.parseTag(storedData.get());
        } catch (CommandSyntaxException e) {
            Eliosync.LOGGER.error(
                    "Failed to parse player data for {}, stopping the server to prevent data loss/desync, please check the logs for more information.",
                    uuid);
            Eliosync.LOGGER.error(e.getMessage());
            Eliosync.LOGGER.debug(storedData.get());
            event.getEntity().getServer().stopServer();
            return;
        }

        CompoundTag originalData = event.getEntity().saveWithoutId(new CompoundTag());
        Eliosync.LOGGER.warn("Loaded data: {}", storedData.get());
        Eliosync.LOGGER.warn("Original data: {}", originalData.getAsString());

        if (!Config.loadPosition) {
            playerData.remove("Pos");
            playerData.remove("Motion");
            playerData.remove("Rotation");
            playerData.remove("FallDistance");
            playerData.remove("OnGround");
            playerData.remove("FallFlying");
            playerData.remove("Dimension");
        }

        if (!Config.loadAttributes)
            playerData.remove("Attributes");

        if (!Config.loadEnderchest)
            playerData.remove("EnderItems");

        if (!Config.loadInventory)
            playerData.remove("Inventory");

        if (!Config.loadGameType)
            playerData.remove("playerGameType");

        if (!Config.loadAbilities)
            playerData.remove("abilities");

        if (!Config.loadXp) {
            playerData.remove("XpLevel");
            playerData.remove("XpP");
            playerData.remove("XpSeed");
            playerData.remove("XpTotal");
        }

        CompoundTag finalPlayerData = originalData.merge(playerData);
        event.getEntity().load(finalPlayerData);

    }

}
