package dev.ahmet.lobbysystem.commands;

import dev.ahmet.lobbysystem.LobbySystem;
import dev.ahmet.lobbysystem.manager.LobbyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {

    private final LobbyManager manager;

    public SpawnCommand() {
        this.manager = LobbySystem.getInstance().getManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        player.teleport(manager.getSpawnpoint());
        player.sendMessage(LobbySystem.getPrefix() + "Du wurdest zum Spawn teleportiert.");

        return false;
    }
}
