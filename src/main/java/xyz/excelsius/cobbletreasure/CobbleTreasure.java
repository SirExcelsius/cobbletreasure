package xyz.excelsius.cobbletreasure;

import com.sun.org.apache.xerces.internal.xs.StringList;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CobbleTreasure extends JavaPlugin {

    FileConfiguration config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.setupConfig();

        this.getServer().getConsoleSender().sendMessage(
                "[CobbleTreasure] enabled on world(s): " + ChatColor.GREEN + config.getStringList("enabled-worlds")
        );

        config.options().copyDefaults(true);
        saveConfig();

        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void setupConfig() {
        config = this.getConfig();

        List<String> info = new ArrayList<>();
        info.add("To edit enabled-worlds, follow standard YAML format as seen by default. " +
                "The world must be spelled exactly as the world file is spelled. " +
                "Vanilla Minecraft world is spelled world, but your world may be called skyworld or some such name if using a skyblock plugin. " +
                "If all-worlds is added to the list of enabled-worlds, all worlds will be enabled regardless of what else is in the list. ");
        info.add("find-rate is the probability 0 to 1 that the player will find an item when breaking a cobblestone block. " +
                "find-rate is a Double, so it can have up to 16 decimal digits. ");
        info.add("To add items to what can be found inside cobblestone ");
        info.add("Step 1) add the item to the items list. " +
                "All items must be precise names and in the same format as the default list. ");
        info.add("Step 2) add the item at the bottom and give a proportion value. " +
                "This value will determine the item's chance, 0 to 1, of being found given that an item has been found. " +
                "This value is a Double, so it can have 16 decimal digits. " +
                "The item key must be the exact same as listed in the items list. " +
                "Make sure that the sum of all the item values equals 1. " +
                "A sum less than 1 can result in players finding AIR. " +
                "A sum greater than 1 can result in some items being impossible for players to find.");
        info.add("To remove items from what can be found inside cobblestone, simply remove the item from the list and its corresponding key/value");
        config.addDefault("info", info);

        List<String> enabledWorlds = new ArrayList<>();
        enabledWorlds.add("world");
        enabledWorlds.add("end");
        config.addDefault("enabled-worlds", enabledWorlds);

        config.addDefault("find-rate", .03);

        Map<String, Double> itemProportions = new HashMap<>();
        itemProportions.put("iron_ingot", .5);
        itemProportions.put("redstone", .2);
        itemProportions.put("gold_ingot", .15);
        itemProportions.put("lapis_lazuli", .1);
        itemProportions.put("diamond", .05);

        List<String> items = new ArrayList<>();
        items.add("iron_ingot");
        items.add("redstone");
        items.add("gold_ingot");
        items.add("lapis_lazuli");
        items.add("diamond");

        config.addDefault("items", items);

        for(String itemKey : itemProportions.keySet()) {
            Double itemValue = itemProportions.get(itemKey);
            config.addDefault(itemKey, itemValue);
        }

        this.getServer().getConsoleSender().sendMessage("[CobbleTreasure] Config loaded");
    }
}
