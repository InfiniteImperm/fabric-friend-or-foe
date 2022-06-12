package net.infiniteimperm.fabric.friendorfoe.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.infiniteimperm.fabric.friendorfoe.FriendOrFoe;
import net.minecraft.text.LiteralText;

public class DisplayHelpCommand implements com.mojang.brigadier.Command<net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource> {
    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(new LiteralText("§benabled: " + FriendOrFoe.getInstance().isEnabled()));
        context.getSource().sendFeedback(new LiteralText("§b\"/imperm fof enable\" enables the graphical changes this mod implements"));
        context.getSource().sendFeedback(new LiteralText("§b\"/imperm fof disable\" disables the graphical changes this mod implements"));
        context.getSource().sendFeedback(new LiteralText("§b\"/imperm fof <groupName> add <playerName>\" adds a player to a group"));
        context.getSource().sendFeedback(new LiteralText("§b\"/imperm fof friend remove <playerName>\" removes a friend"));
        context.getSource().sendFeedback(new LiteralText("§b\"/imperm fof group create <groupname> <colourCode>\" creates a new group. the colourCode must be a number from 0 to 9 or a letter from a to f"));
        context.getSource().sendFeedback(new LiteralText("§b\"/imperm fof group delete <groupname>\" deletes a group."));
        return 0;
    }
}
