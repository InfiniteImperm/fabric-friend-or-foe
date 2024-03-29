package net.infiniteimperm.fabric.friendorfoe.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.infiniteimperm.fabric.friendorfoe.Configuration;
import net.infiniteimperm.fabric.friendorfoe.group.Group;
import net.infiniteimperm.fabric.friendorfoe.group.GroupManager;
import net.minecraft.text.LiteralText;

public class DeleteGroupCommand implements com.mojang.brigadier.Command<FabricClientCommandSource> {

    private final String groupName;

    public DeleteGroupCommand(String groupName) {
        this.groupName = groupName;
    }
    @Override
    public int run(CommandContext<FabricClientCommandSource> context) {
        Group group = GroupManager.getInstance().delete(groupName);
        if( group == null ) {
            context.getSource().sendError(new LiteralText("§4group could not be deleted"));
            return 1;
        }
        context.getSource().sendFeedback(new LiteralText("§2group §" + group.getColourCode() + groupName + " §2successfully deleted"));
        context.getSource().sendFeedback(new LiteralText("§eMinecraft has to be restarted before the associated commands are removed."));
        return 0;
    }
}