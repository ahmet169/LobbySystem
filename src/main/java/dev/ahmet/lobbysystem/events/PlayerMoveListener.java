package dev.ahmet.lobbysystem.events;

import dev.ahmet.lobbysystem.LobbySystem;
import dev.ahmet.lobbysystem.manager.LobbyManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final LobbyManager manager;

    public PlayerMoveListener() {
        this.manager = LobbySystem.getInstance().getManager();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Material m = player.getLocation().getBlock().getType();
        if(m == Material.LEGACY_STATIONARY_WATER || m == Material.WATER) {
            player.teleport(manager.getSpawnpoint());
        }

    }
}
