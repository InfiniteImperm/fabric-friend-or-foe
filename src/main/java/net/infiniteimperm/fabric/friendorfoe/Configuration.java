package net.infiniteimperm.fabric.friendorfoe;

import net.infiniteimperm.fabric.friendorfoe.group.GroupManager;
import net.infiniteimperm.fabric.friendorfoe.group.Group;

import java.io.*;
import java.nio.file.Paths;

public class Configuration {

    private static Configuration INSTANCE = null;

    public static Configuration getInstance() {
        if (INSTANCE == null) INSTANCE = new Configuration();
        return INSTANCE;
    }

    private void loadGroup(File directory, String groupName, String colourCode) throws IOException {
        File file = Paths.get(directory.getAbsolutePath(), groupName + ".group").toFile();
        if (!file.exists() && !file.createNewFile())
            throw new IOException();
        GroupManager.getInstance().register(new Group(groupName, colourCode), false);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!"".equals(line))
                    GroupManager.getInstance().set(line, groupName);
            }
        }
    }

    private File getGroupConfigFile() {
        return Paths.get(FriendOrFoe.CONFIG_BASE_PATH.getAbsolutePath(), "groups.config").toFile();
    }

    public void saveBaseConfiguration() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FriendOrFoe.BASE_CONFIGURATION_FILE))) {
            writer.write("enabled: " + FriendOrFoe.getInstance().isEnabled());
        } catch (IOException e) {
            FriendOrFoe.LOGGER.error("friend-or-foe: couldn't safe base configuration");
            FriendOrFoe.LOGGER.error(e);
        }
    }

    public void loadBaseConfiguration() throws IOException {
        if (!FriendOrFoe.BASE_CONFIGURATION_FILE.exists() && !FriendOrFoe.BASE_CONFIGURATION_FILE.createNewFile())
            throw new IOException();
        try (BufferedReader reader = new BufferedReader(new FileReader(FriendOrFoe.BASE_CONFIGURATION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!"".equals(line)) {
                    String[] split = line.split(":");
                    if (split.length == 2) {
                        switch (split[0]) {
                            case "enabled":
                                if ("true".equals(split[1].trim()))
                                    FriendOrFoe.getInstance().enable();
                                else
                                    FriendOrFoe.getInstance().disable();
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    public void loadGroups() throws IOException {
        File configFile = getGroupConfigFile();
        if (!configFile.exists() && !configFile.createNewFile())
            throw new IOException("can't create group.config");
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");
                if (split.length == 2) {
                    loadGroup(FriendOrFoe.CONFIG_BASE_PATH, split[0], split[1]);
                }
            }
        }
    }

    public boolean saveGroups() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getGroupConfigFile()))) {
            for (Group group : GroupManager.getInstance().getGroups())
                writer.write(group.getName() + ";" + group.getColourCode() + "\n");
            writer.flush();
        } catch (IOException e) {
            FriendOrFoe.LOGGER.error(e);
            return false;
        }
        return true;
    }

}
