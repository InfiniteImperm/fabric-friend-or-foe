package net.infiniteimperm.fabric.friendorfoe.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.infiniteimperm.fabric.friendorfoe.FriendOrFoe;

import java.util.function.Function;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class FriendOrFoeCommandFactory {

    public static void register(String target, String action, Function<String, Boolean> function) {
        FriendOrFoe.LOGGER.info("friend-or-foe: adding command /imperm fof " + target + " " + action + " <playerName>");
        ClientCommandManager.DISPATCHER.register(
                literal("imperm").then(
                        literal("fof").then(
                                literal(target).then(
                                        literal(action).then(
                                                argument("playerName", StringArgumentType.word()).executes(new FriendOrFoeCommand(function))
                                        )
                                )
                        )
                )
        );
    }

}
