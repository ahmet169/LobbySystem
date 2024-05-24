package dev.ahmet.lobbysystem.events;

import dev.ahmet.lobbysystem.LobbySystem;
import org.bukkit.*;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class GadgetEvents implements Listener {

    private final List<Player> cooldown;

    public GadgetEvents() {
        this.cooldown = new ArrayList<>();
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();
        PlayerFishEvent.State state = event.getState();
        if (!cooldown.contains(player)) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.FISHING_ROD) && (state == PlayerFishEvent.State.REEL_IN || state == PlayerFishEvent.State.IN_GROUND)) {
                Location l1 = player.getLocation();
                Location l2 = event.getHook().getLocation();
                Vector v = l2.toVector().subtract(l1.toVector());
                player.setVelocity(v.multiply(0.05));
                cooldown.add(player);

                Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        cooldown.remove(player);
                    }
                }, 60);
            }
        } else {
            event.setCancelled(true);
            player.sendMessage(LobbySystem.getPrefix() + "Bitte warte einen Moment.");
        }
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player player && event.getEntity() instanceof EnderPearl) {
            if(!cooldown.contains(player)) {
                cooldown.add(player);
                Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), () -> cooldown.remove(player), 100);
            } else {
                player.sendMessage(LobbySystem.getPrefix() + "Bitte warte einen Moment.");
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player && event.getEntity() instanceof EnderPearl) {
            Player player = (Player) event.getEntity().getShooter();
            Location loc = event.getEntity().getLocation();
            Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(255, 0, 0), Color.fromRGB(255, 255, 255), 1.0F);
            loc.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, loc, 50, dustTransition);
        }
    }

    @EventHandler
    public void onPearl(PlayerTeleportEvent event) {
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            event.setCancelled(true);
        }
    }


}
