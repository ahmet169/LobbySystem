package dev.ahmet.lobbysystem.events;

import dev.ahmet.lobbysystem.LobbySystem;
import dev.ahmet.lobbysystem.manager.LobbyManager;
import dev.ahmet.lobbysystem.utils.ItemBuilder;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InventoryClickListener implements Listener {

    private final PlayerManager playerManager;
    private final LobbyManager manager;

    public InventoryClickListener() {
        this.playerManager = InjectionLayer.ext().instance(ServiceRegistry.class).firstProvider(PlayerManager.class);
        this.manager = LobbySystem.getInstance().getManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInv = event.getClickedInventory();
        Inventory playerInv = player.getInventory();
        ItemStack currentItem = event.getCurrentItem();
        Material currentMaterial = currentItem.getType();

        if(event.getView().getTitle().equals("§8§lNavigator")) event.setCancelled(true);
        if(event.getView().getTitle().equals("§8§lEinstellungen")) event.setCancelled(true);
        if(event.getView().getTitle().equals("§8§lSpielzeuge")) event.setCancelled(true);

        if(currentMaterial.equals(Material.OAK_SAPLING)) {
            event.setCancelled(true);
            playerManager.playerExecutor(player.getUniqueId()).connect("Citybuild-1");
        }

        if(currentMaterial.equals(Material.GLOWSTONE)) {
            event.setCancelled(true);
            player.closeInventory();
            player.teleport(manager.getSpawnpoint());
        }

        if(currentMaterial.equals(Material.LIME_DYE)) {
            clickedInv.setItem(11, new ItemBuilder(Material.RED_DYE).setName("§d§lSichtbarkeit").addLore(Arrays.asList("§cKeine Spieler sichtbar")).toItemStack());
            manager.updateOption(player, "hider", false);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(LobbySystem.getInstance(), onlinePlayer);
            }
        }

        if(currentMaterial.equals(Material.RED_DYE)) {
            clickedInv.setItem(11, new ItemBuilder(Material.LIME_DYE).setName("§d§lSichtbarkeit").addLore(Arrays.asList("§aAlle Spieler sichtbar")).toItemStack());
            manager.updateOption(player, "hider", true);
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                player.showPlayer(LobbySystem.getInstance(), onlinePlayer);
            }
        }

        if(currentItem.getLore().contains("§aAktiviert")) {
            clickedInv.setItem(13, new ItemBuilder(Material.BELL).setName("§2§lBroadcast-Nachrichten").addLore(Arrays.asList("§cDeaktiviert")).toItemStack());
            manager.updateOption(player, "broadcast", false);
        }

        if(currentItem.getLore().contains("§cDeaktiviert")) {
            clickedInv.setItem(13, new ItemBuilder(Material.BELL).setName("§2§lBroadcast-Nachrichten").addLore(Arrays.asList("§aAktiviert")).toItemStack());
            manager.updateOption(player, "broadcast", true);
        }


        /******************************************************************/

        ItemStack rod = new ItemBuilder(Material.FISHING_ROD).setName("§e§lGreifhaken").toItemStack();
        ItemStack enderPearl = new ItemBuilder(Material.ENDER_PEARL).setName("§9§lRauchgranate").toItemStack();
        ItemStack snowBall = new ItemBuilder(Material.SNOWBALL).setName("§b§lSchneeball").toItemStack();
        ItemStack feather = new ItemBuilder(Material.FEATHER).setName("§f§lSprungfeder").toItemStack();
        ItemStack bow = new ItemBuilder(Material.BOW).setName("§5§lGeisterbogen").toItemStack();
        ItemStack blazeRod = new ItemBuilder(Material.BLAZE_ROD).setName("§5§lZauberstab").toItemStack();

        if(currentItem.getType().equals(Material.FISHING_ROD)) {
            playerInv.setItem(5, rod);
            playerInv.close();
        }

        if(currentItem.getType().equals(Material.ENDER_PEARL)) {
            playerInv.setItem(5, enderPearl);
            playerInv.close();
        }

        if(currentItem.getType().equals(Material.SNOWBALL)) {
            playerInv.setItem(5, snowBall);
            playerInv.close();
        }

        if(currentItem.getType().equals(Material.FEATHER)) {
            playerInv.setItem(5, feather);
            playerInv.close();
        }

        if(currentItem.getType().equals(Material.BOW)) {
            playerInv.setItem(5, bow);
            playerInv.close();
        }

        if(currentItem.getType().equals(Material.BLAZE_ROD)) {
            playerInv.setItem(5, blazeRod);
            playerInv.close();
        }

    }
}
