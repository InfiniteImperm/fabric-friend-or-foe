package net.infiniteimperm.fabric.friendorfoe.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.infiniteimperm.fabric.friendorfoe.Configuration;
import net.infiniteimperm.fabric.friendorfoe.FriendOrFoe;
import net.minecraft.text.LiteralText;

public class EnableCommand implements com.mojang.brigadier.Command<net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource> {
    @Override
    public int run(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        FriendOrFoe.getInstance().enable();
        context.getSource().sendFeedback(new LiteralText("§aenabled friend-or-foe"));
        Configuration.getInstance().saveBaseConfiguration();
        return 0;
    }
}
