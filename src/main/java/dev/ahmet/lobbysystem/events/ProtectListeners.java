package dev.ahmet.lobbysystem.events;

import dev.ahmet.lobbysystem.LobbySystem;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.List;

public class ProtectListeners implements Listener {

    private List<Player> buildList;

    public ProtectListeners() {
        this.buildList = LobbySystem.getInstance().getBuildList();
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!buildList.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!buildList.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!buildList.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityPlace(EntityPlaceEvent event) {
        Player player = event.getPlayer();
        if (!buildList.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!buildList.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof AbstractArrow) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player shooter && event.getEntity() instanceof Player target) {
                shooter.hidePlayer(LobbySystem.getInstance(), target);
                Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), () -> shooter.showPlayer(LobbySystem.getInstance(), target), 150);
            }
        } else {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damager = (Player) event.getEntity();
            if (!damager.getInventory().contains(Material.BOW)) {
                event.setCancelled(true);
            } else {
                event.setDamage(0);
            }
        }
    }

    @EventHandler
    public void onCollect(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (!buildList.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityShoot(EntityShootBowEvent event) {
        if(event.getProjectile() instanceof AbstractArrow arrow) {
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
    }

}
