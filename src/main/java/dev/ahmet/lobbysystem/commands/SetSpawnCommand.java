package dev.ahmet.lobbysystem.commands;

import dev.ahmet.lobbysystem.LobbySystem;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String prefix = LobbySystem.getPrefix();
        FileConfiguration config = LobbySystem.getInstance().getConfig();
        ConfigurationSection section = config.getConfigurationSection("spawnpoint");

        if (!player.hasPermission("lobby.setspawn")) {
            player.sendMessage("§cDazu hast du keinen Zugriff!");
            return true;
        }

        Location loc = player.getLocation();
        section.set("x", loc.getX());
        section.set("y", loc.getY());
        section.set("z", loc.getZ());
        section.set("yaw", loc.getYaw());
        section.set("pitch", loc.getPitch());
        player.sendMessage(prefix + "Der Spawnpunkt wurde §aerfolgreich §7gesetzt.");

        return false;
    }
}
