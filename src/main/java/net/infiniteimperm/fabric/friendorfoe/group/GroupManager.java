package net.infiniteimperm.fabric.friendorfoe.group;

import net.infiniteimperm.fabric.friendorfoe.FriendOrFoe;
import net.infiniteimperm.fabric.friendorfoe.command.FriendOrFoeCommandFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class GroupManager {

    private static GroupManager INSTANCE = null;

    public static GroupManager getInstance() {
        if (INSTANCE == null) INSTANCE = new GroupManager();
        return INSTANCE;
    }

    private final Map<String, Group> groups;
    private final PlayerNameCache playerNameCache;
    private final Map<String, String> playerToGroupMap;

    public GroupManager() {
        groups = new HashMap<>();
        playerNameCache = new PlayerNameCache();
        playerToGroupMap = new HashMap<>();
    }

    private File getGroupFile(String groupName) {
        return Paths.get(FriendOrFoe.CONFIG_BASE_PATH.getAbsolutePath(), groupName + ".group").toFile();
    }

    private boolean addGroupEntry(String uuid, String groupName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getGroupFile(groupName), true))) {
            writer.write(uuid + "\n");
            writer.flush();
        } catch (IOException e) {
            FriendOrFoe.LOGGER.error(e);
            return false;
        }
        return true;
    }

    private boolean saveGroup(String groupName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getGroupFile(groupName), false))) {
            for (String uuid : playerToGroupMap.keySet()) {
                if (playerToGroupMap.get(uuid).equals(groupName)) writer.write(uuid + "\n");
            }
            writer.flush();
        } catch (IOException e) {
            FriendOrFoe.LOGGER.error(e);
            return false;
        }
        return true;
    }

    public boolean register(Group group, boolean override) {
        if (groups.containsKey(group.getName())) return false;
        groups.put(group.getName(), group);
        FriendOrFoeCommandFactory.register(group.getName(), "add", uuid -> GroupManager.getInstance().set(uuid, group.getName()));
        if (override) saveGroup(group.getName());
        return true;
    }

    public boolean set(String uuid, String target) {
        if (uuid == null || "".equals(uuid) || target == null || "".equals(target)) return false;
        FriendOrFoe.LOGGER.info("player " + uuid + " is in group \"" + playerToGroupMap.get(uuid) + "\" and target is \"" + target + "\"");
        if (target.equals(playerToGroupMap.get(uuid))) {
            FriendOrFoe.LOGGER.info("nothing to do");
            return true;
        }
        String previousGroup = playerToGroupMap.put(uuid, target);
        playerNameCache.clear(uuid);
        if (previousGroup != null && !"".equals(previousGroup) && !saveGroup(previousGroup)) return false;
        return addGroupEntry(uuid, target);
    }

    public PlayerNameCache getPlayerNameCache() {
        return playerNameCache;
    }

    public Group getGroup(String uuid) {
        String groupName = playerToGroupMap.get(uuid);
        if (groupName == null) return null;
        return groups.get(groupName);
    }

    public Collection<Group> getGroups() {
        return Collections.unmodifiableCollection(new ArrayList<>(groups.values()));
    }
}
