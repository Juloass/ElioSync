package fr.tofuxia.eliosync;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@Mod.EventBusSubscriber(modid = Eliosync.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        // Database Config
        private static final ModConfigSpec.ConfigValue<String> JBDC_URL = BUILDER
                        .comment("The url of the database in the format jdbc:mysql://localhost:3306/")
                        .define("jdbcUrl", "jdbc:mysql://localhost:3306/");

        private static final ModConfigSpec.ConfigValue<String> USERNAME = BUILDER
                        .comment("The username of the database")
                        .define("username", "root");

        private static final ModConfigSpec.ConfigValue<String> PASSWORD = BUILDER
                        .comment("The password of the database")
                        .define("password", "p4$$w0rd");

        // Sync Config
        private static final ModConfigSpec.BooleanValue SAVE_POSITION = BUILDER
                        .comment("Whether to save coordinates (and dimension) changes from this server to the database")
                        .define("savePosition", true);
        private static final ModConfigSpec.BooleanValue LOAD_POSITION = BUILDER
                        .comment("Whether to load coordinates (and dimension) from the database")
                        .define("loadPosition", true);

        private static final ModConfigSpec.BooleanValue SAVE_ATTRIBUTES = BUILDER
                        .comment("Whether to save attributes changes from this server to the database (https://minecraft.wiki/w/Attribute)")
                        .define("saveAttributes", true);
        private static final ModConfigSpec.BooleanValue LOAD_ATTRIBUTES = BUILDER
                        .comment("Whether to load attributes from the database ")
                        .define("loadAttributes", true);

        private static final ModConfigSpec.BooleanValue SAVE_ENDERCHEST = BUILDER
                        .comment("Whether to save enderchest contents changes from this server to the database")
                        .define("saveEnderchest", true);
        private static final ModConfigSpec.BooleanValue LOAD_ENDERCHEST = BUILDER
                        .comment("Whether to load enderchest contents from the database")
                        .define("loadEnderchest", true);

        private static final ModConfigSpec.BooleanValue SAVE_INVENTORY = BUILDER
                        .comment("Whether to save inventory changes from this server to the database")
                        .define("saveInventory", true);
        private static final ModConfigSpec.BooleanValue LOAD_INVENTORY = BUILDER
                        .comment("Whether to load inventory from the database")
                        .define("loadInventory", true);

        private static final ModConfigSpec.BooleanValue SAVE_GAMETYPE = BUILDER
                        .comment("Whether to save gametype changes from this server to the database (https://minecraft.wiki/w/Game_mode)")
                        .define("saveGameType", true);
        private static final ModConfigSpec.BooleanValue LOAD_GAMETYPE = BUILDER
                        .comment("Whether to load gametype from the database")
                        .define("loadGameType", true);

        private static final ModConfigSpec.BooleanValue SAVE_ABILITIES = BUILDER
                        .comment("Whether to save abilities changes from this server to the database (https://minecraft.wiki/w/Player#Entity_data)")
                        .define("saveAbilities", true);
        private static final ModConfigSpec.BooleanValue LOAD_ABILITIES = BUILDER
                        .comment("Whether to load abilities from the database")
                        .define("loadAbilities", true);

        private static final ModConfigSpec.BooleanValue SAVE_XP = BUILDER
                        .comment("Whether to save experience changes from this server to the database")
                        .define("saveXp", true);
        private static final ModConfigSpec.BooleanValue LOAD_XP = BUILDER
                        .comment("Whether to load experience from the database")
                        .define("loadXp", true);

        static final ModConfigSpec SPEC = BUILDER.build();

        // Database Config
        public static String jdbcUrl;
        public static String username;
        public static String password;

        // Sync Config
        public static boolean savePosition;
        public static boolean loadPosition;

        public static boolean saveAttributes;
        public static boolean loadAttributes;

        public static boolean saveEnderchest;
        public static boolean loadEnderchest;

        public static boolean saveInventory;
        public static boolean loadInventory;

        public static boolean saveGameType;
        public static boolean loadGameType;

        public static boolean saveAbilities;
        public static boolean loadAbilities;

        public static boolean saveXp;
        public static boolean loadXp;

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
                // Database Config
                jdbcUrl = JBDC_URL.get();
                username = USERNAME.get();
                password = PASSWORD.get();
                // Sync Config
                savePosition = SAVE_POSITION.get();
                loadPosition = LOAD_POSITION.get();
                saveAttributes = SAVE_ATTRIBUTES.get();
                loadAttributes = LOAD_ATTRIBUTES.get();
                saveEnderchest = SAVE_ENDERCHEST.get();
                loadEnderchest = LOAD_ENDERCHEST.get();
                saveInventory = SAVE_INVENTORY.get();
                loadInventory = LOAD_INVENTORY.get();
                saveGameType = SAVE_GAMETYPE.get();
                loadGameType = LOAD_GAMETYPE.get();
                saveAbilities = SAVE_ABILITIES.get();
                loadAbilities = LOAD_ABILITIES.get();
                saveXp = SAVE_XP.get();
                loadXp = LOAD_XP.get();
        }
}
