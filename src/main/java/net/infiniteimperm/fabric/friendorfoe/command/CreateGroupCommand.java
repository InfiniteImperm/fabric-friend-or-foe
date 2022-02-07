package net.infiniteimperm.fabric.friendorfoe.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.infiniteimperm.fabric.friendorfoe.group.GroupManager;
import net.infiniteimperm.fabric.friendorfoe.Configuration;
import net.infiniteimperm.fabric.friendorfoe.group.Group;
import net.minecraft.text.LiteralText;

public class CreateGroupCommand implements com.mojang.brigadier.Command<net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource> {
    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {
        String groupName = StringArgumentType.getString(context, "groupName");
        String colourCode = StringArgumentType.getString(context, "colourCode");
        if ("".equals(groupName) || "".equals(colourCode)) {
            context.getSource().sendError(new LiteralText("§4groupName or colourCode was empty"));
            return 1;
        }
        if (colourCode.length() != 1 || !(('a' <= colourCode.charAt(0) && colourCode.charAt(0) <= 'f') || ('0' <= colourCode.charAt(0) && colourCode.charAt(0) <= '9')))
            context.getSource().sendError(new LiteralText("§4colourCode has to be a number from 0 to 9 or a letter from a to f"));
        if (!GroupManager.getInstance().register(new Group(groupName, "§" + colourCode), true)) {
            context.getSource().sendError(new LiteralText("§4group registration failed"));
            return 2;
        }
        if (!Configuration.getInstance().saveGroups()) {
            context.getSource().sendError(new LiteralText("§4group was not saved"));
            return 3;
        }
        context.getSource().sendFeedback(new LiteralText("§2group §" + colourCode + groupName + " §2successfully created"));
        context.getSource().sendFeedback(new LiteralText("§2type \"/imperm fof " + groupName + " add <playerName>\" to add a player to the group"));
        context.getSource().sendFeedback(new LiteralText("§eMinecraft has to be restarted before the associated commands can be seen."));
        return 0;
    }
}