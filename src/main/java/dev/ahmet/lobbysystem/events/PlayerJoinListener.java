package dev.ahmet.lobbysystem.events;

import dev.ahmet.lobbysystem.LobbySystem;
import dev.ahmet.lobbysystem.manager.LobbyManager;
import dev.ahmet.lobbysystem.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class PlayerJoinListener implements Listener {

    private final LobbyManager manager;

    public PlayerJoinListener() {
        this.manager = LobbySystem.getInstance().getManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Inventory inv = player.getInventory();

        inv.clear();
        inv.setItem(1, new ItemBuilder(Material.COMPASS).setName("§a§lNavigator").toItemStack());
        inv.setItem(3, new ItemBuilder(Material.FIREWORK_ROCKET).setName("§d§lSpielzeuge").toItemStack());
        inv.setItem(5, new ItemBuilder(Material.STRUCTURE_VOID).setName("§c§lKein Spielzeug ausgewählt").toItemStack());
        inv.setItem(7, new ItemBuilder(Material.COMPARATOR).setName("§c§lEinstellungen").toItemStack());

        List<String> messages = new ArrayList<>();
        messages.add(" ");
        messages.add("§b§lLUMANIA §8• §a" + player.getName());
        messages.add("§b§lLUMANIA §8• §7Herzlich Willkommen!");
        messages.add("§b§lLUMANIA §8• §7Discord: https://dc.lumania.net/");
        messages.add(" ");

        event.joinMessage(Component.text(""));

        for (String message : messages) {
            player.sendMessage(message);
        }

        if (!manager.isRegistered(player)) {
            manager.createPlayer(player);
        }

        if (!manager.getOption(player, "hider")) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(onlinePlayer);
            }
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!manager.getOption(onlinePlayer, "hider")) {
                onlinePlayer.hidePlayer(LobbySystem.getInstance(), player);
            }
        }


        player.teleport(manager.getSpawnpoint());
    }
}
