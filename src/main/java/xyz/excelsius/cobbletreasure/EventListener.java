package xyz.excelsius.cobbletreasure;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class EventListener implements Listener {

    private CobbleTreasure cobbleTreasure;
    private FileConfiguration config;
    private Double rng;

    public EventListener(CobbleTreasure _cobbleTreasure) {
        cobbleTreasure = _cobbleTreasure;
        config = cobbleTreasure.getConfig();
    }

    public void reloadConfig(FileConfiguration _config) { config = _config; }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {

        List<String> enabledWorlds = config.getStringList("enabled-worlds");
        String currentWorld = event.getPlayer().getWorld().getName();

        if (enabledWorlds.contains(currentWorld) || enabledWorlds.contains("all-worlds")) {
            Block block = event.getBlock();
            if (block.getType().equals(Material.COBBLESTONE)) {
                if (findItem()) {
                    ItemStack item = new ItemStack(pickItem());
                    Player player = event.getPlayer();
                    player.getInventory().addItem(item);
                    player.sendMessage(ChatColor.GRAY + "You found a " + ChatColor.YELLOW + item.getType() + ChatColor.GRAY + " in the rock!");
                }
            }
        }
    }

    public boolean findItem() {
        rng = new Random().nextDouble();
        if(new Random().nextDouble() <= cobbleTreasure.getConfig().getDouble("find-rate")) {
            return true;
        }
        return false;
    }

    public Material pickItem() {
        List<String> items = config.getStringList("items");

        Double lastItem = 0.0;
        Double itemProportion;
        rng = new Random().nextDouble();

        for(String item : items) {
            itemProportion = config.getDouble(item);
            cobbleTreasure.getServer().getConsoleSender().sendMessage("item " + item);
            if(rng <= lastItem + itemProportion) {
                try {
                    return Material.getMaterial(item.toUpperCase());
                } catch (NullPointerException e) {
                    cobbleTreasure.getServer().getConsoleSender().sendMessage(
                            ChatColor.RED + "[CobbleTreasure] An item in the config is not a registered item in the Spigot API"
                    );
                }
            }
            lastItem += itemProportion;
        }
        cobbleTreasure.getServer().getConsoleSender().sendMessage(
                ChatColor.RED + "[CobbleTreasure] pickItem returned AIR! Do the proportions in the config file sum less than 1?"
        );
        return Material.AIR;
    }

}
