package pl.moderr.impact.economy.util;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ColorUtil {
    public static final char IMPACT = '\u26A1';
    @Contract("_ -> new")
    public static @NotNull String color(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    @Contract("_ -> new")
    public static @NotNull String colorWithPrefix(String text){
        return ChatColor.translateAlternateColorCodes('&', "&8[&e&l" + IMPACT + "&8]&r " + text);
    }
}
