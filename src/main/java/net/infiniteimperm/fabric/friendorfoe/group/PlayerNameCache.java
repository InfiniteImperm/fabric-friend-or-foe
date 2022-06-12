package net.infiniteimperm.fabric.friendorfoe.group;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class PlayerNameCache {

    private final Map<String, Text> textCache;

    public PlayerNameCache() {
        textCache = new HashMap<>();
    }

    private String replacePlayerName(String uuid, String playerName) {
        Group group = GroupManager.getInstance().getGroup(uuid);
        if (group == null) return playerName;
        return group.getColourCode() + playerName;
    }

    public Text getPlayerName(String uuid, String name) {
        synchronized (textCache) {
            if (!textCache.containsKey(uuid))
                textCache.put(uuid, new LiteralText(replacePlayerName(uuid, name)));
            return textCache.get(uuid);
        }
    }

    private Text getPlayerName(GameProfile gameProfile) {
        if (gameProfile != null && gameProfile.getName() != null && !"".equals(gameProfile.getName()) && gameProfile.getId() != null)
            return getPlayerName(gameProfile.getId().toString(), gameProfile.getName());
        return getDefaultPlayerName();
    }

    public Text getDefaultPlayerName() {
        return new LiteralText("unknown playerName");
    }

    public Text getPlayerName(PlayerListEntry playerListEntry) {
        if (playerListEntry != null) {
            return getPlayerName(playerListEntry.getProfile());
        }
        return getDefaultPlayerName();
    }

    public Text getPlayerName(PlayerEntity abstractClientPlayerEntity) {
        if (abstractClientPlayerEntity != null)
            return getPlayerName(abstractClientPlayerEntity.getGameProfile());
        return getDefaultPlayerName();
    }

    public void clear(String uuid) {
        synchronized (textCache) {
            textCache.remove(uuid);
        }
    }
}
