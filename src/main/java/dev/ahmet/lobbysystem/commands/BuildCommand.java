package dev.ahmet.lobbysystem.commands;

import dev.ahmet.lobbysystem.LobbySystem;
import dev.ahmet.lobbysystem.utils.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BuildCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String prefix = LobbySystem.getPrefix();
        List<Player> buildList = LobbySystem.getInstance().getBuildList();
        Inventory inv = player.getInventory();

        if(!player.hasPermission("lobby.build")) {
            player.sendMessage("§cDazu hast du keinen Zugriff!");
            return true;
        }

        if(buildList.contains(player)) {
            player.sendMessage(prefix + "Du bist nun §cnicht mehr §7im §bBaumodus§7.");
            player.setGameMode(GameMode.SURVIVAL);
            inv.clear();
            inv.setItem(1, new ItemBuilder(Material.COMPASS).setName("§a§lNavigator").toItemStack());
            inv.setItem(3, new ItemBuilder(Material.FIREWORK_ROCKET).setName("§d§lSpielzeuge").toItemStack());
            inv.setItem(5, new ItemBuilder(Material.STRUCTURE_VOID).setName("§c§lKein Spielzeug ausgewählt").toItemStack());
            inv.setItem(7, new ItemBuilder(Material.COMPARATOR).setName("§c§lEinstellungen").toItemStack());
            buildList.remove(player);
        } else {
            player.sendMessage(prefix + "Du bist nun im §bBaumodus§7.");
            player.setGameMode(GameMode.CREATIVE);
            inv.clear();
            buildList.add(player);
        }

        return false;
    }
}
