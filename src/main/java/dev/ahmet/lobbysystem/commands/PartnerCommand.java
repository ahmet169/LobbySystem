package dev.ahmet.lobbysystem.commands;

import dev.ahmet.lobbysystem.LobbySystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PartnerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        String prefix = LobbySystem.getPrefix();

        player.sendMessage(prefix + "§7Unterstütze unseren Partner: https://lumania.net/partner");

        return false;
    }
}
