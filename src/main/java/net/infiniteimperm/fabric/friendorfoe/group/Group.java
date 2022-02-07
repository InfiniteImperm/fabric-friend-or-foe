package net.infiniteimperm.fabric.friendorfoe.group;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Group {

    @NotNull
    private final String name;
    @NotNull
    private final String colourCode;

    public Group(String name, String colourCode) {
        if (name == null || "".equals(name) || colourCode == null || "".equals(colourCode))
            throw new IllegalArgumentException();
        this.name = name;
        this.colourCode = colourCode;
    }

    public String getName() {
        return name;
    }

    public String getColourCode() {
        return colourCode;
    }
}
