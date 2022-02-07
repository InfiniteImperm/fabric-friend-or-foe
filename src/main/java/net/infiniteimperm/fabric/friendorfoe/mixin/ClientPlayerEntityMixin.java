package net.infiniteimperm.fabric.friendorfoe.mixin;

import net.infiniteimperm.fabric.friendorfoe.FriendOrFoe;
import net.infiniteimperm.fabric.friendorfoe.group.GroupManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(at = @At("HEAD"), method = "getDisplayName", cancellable = true)
    private void getDisplayNameMixin(CallbackInfoReturnable info) {
        if (FriendOrFoe.getInstance().isEnabled()) {
            info.setReturnValue(GroupManager.getInstance().getPlayerNameCache().getPlayerName((PlayerEntity) (Object) this));
            info.cancel();
        }
    }
}