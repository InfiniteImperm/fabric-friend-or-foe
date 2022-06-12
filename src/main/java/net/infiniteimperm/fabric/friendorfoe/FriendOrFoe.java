package net.infiniteimperm.fabric.friendorfoe;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.loader.api.FabricLoader;
import net.infiniteimperm.fabric.friendorfoe.command.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class FriendOrFoe implements ModInitializer {

    private static FriendOrFoe INSTANCE = null;

    public static FriendOrFoe getInstance() {
        if (INSTANCE == null) INSTANCE = new FriendOrFoe();
        return INSTANCE;
    }

    public static final Logger LOGGER = LogManager.getLogger("fabric-friend-or-foe");
    public final static File CONFIG_BASE_PATH = Paths.get(FabricLoader.getInstance().getConfigDir().toFile().getAbsolutePath(), "imperm", "FriendOrFoe").toFile();
    public final static File BASE_CONFIGURATION_FILE = Paths.get(CONFIG_BASE_PATH.getAbsolutePath(), "base.config").toFile();

    private boolean enabled;

    public FriendOrFoe() {
        this.enabled = true;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("initializing");
        if (!CONFIG_BASE_PATH.exists() && !CONFIG_BASE_PATH.mkdirs())
            LOGGER.error("directory " + CONFIG_BASE_PATH.getAbsolutePath() + " has not been created.");
        try {
            Configuration.getInstance().loadBaseConfiguration();
            Configuration.getInstance().loadGroups();
        } catch (IOException e) {
            LOGGER.error("couldn't create or access a configuration file");
            LOGGER.error(e);
            return;
        }

        LOGGER.info("adding commands");
        ClientCommandManager.DISPATCHER.register(literal("imperm").then(literal("fof").executes(new DisplayHelpCommand())));
        ClientCommandManager.DISPATCHER.register(literal("imperm").then(literal("fof").then(literal("help").executes(new DisplayHelpCommand()))));
        ClientCommandManager.DISPATCHER.register(literal("imperm").then(literal("fof").then(literal("group").then(literal("create").then(argument("groupName", StringArgumentType.word()).then(argument("colourCode", StringArgumentType.word()).executes(new CreateGroupCommand())))))));
        ClientCommandManager.DISPATCHER.register(literal("imperm").then(literal("fof").then(literal("group").then(literal("delete").then(argument("groupName", StringArgumentType.word()).executes(new DeleteGroupCommand()))))));
        ClientCommandManager.DISPATCHER.register(literal("imperm").then(literal("fof").then(literal("enable").executes(new EnableCommand()))));
        ClientCommandManager.DISPATCHER.register(literal("imperm").then(literal("fof").then(literal("disable").executes(new DisableCommand()))));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }
}
