package net.infiniteimperm.fabric.friendorfoe.mixin;

import net.infiniteimperm.fabric.friendorfoe.FriendOrFoe;
import net.infiniteimperm.fabric.friendorfoe.group.GroupManager;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(at = @At("HEAD"), method = "getPlayerName", cancellable = true)
    private void getPlayerNameMixin(PlayerListEntry entry, CallbackInfoReturnable info) {
        if (FriendOrFoe.getInstance().isEnabled()) {
            info.setReturnValue(GroupManager.getInstance().getPlayerNameCache().getPlayerName(entry));
            info.cancel();
        }
    }
}
