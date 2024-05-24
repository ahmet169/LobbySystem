package dev.ahmet.lobbysystem.events;

import dev.ahmet.lobbysystem.LobbySystem;
import dev.ahmet.lobbysystem.manager.LobbyManager;
import dev.ahmet.lobbysystem.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerInteractListener implements Listener {

    private final ItemStack grayGlass = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("").toItemStack();
    private final LobbyManager manager;
    private final List<Player> cooldown;

    public PlayerInteractListener() {
        this.manager = LobbySystem.getInstance().getManager();
        this.cooldown = new ArrayList<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Inventory playerInv = player.getInventory();
        ItemStack item = event.getItem();

        if (item == null) return;
        if (item.getType() == Material.AIR) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
                return;

            if (item.getItemMeta().getDisplayName()
                    .equalsIgnoreCase("§a§lNavigator")) openNavigator(player);

            if (item.getItemMeta().getDisplayName()
                    .equalsIgnoreCase("§d§lSpielzeuge")) openGadgets(player);

            if (item.getItemMeta().getDisplayName()
                    .equalsIgnoreCase("§c§lEinstellungen")) openSettings(player);

            if (item.getType().equals(Material.SNOWBALL)) {
                ItemStack snowBall = new ItemBuilder(Material.SNOWBALL).setAmount(2).setName("§b§lSchneeball").toItemStack();
                playerInv.setItem(5, snowBall);
            }

            if(item.getType().equals(Material.ENDER_PEARL)) {
                ItemStack enderPearl = new ItemBuilder(Material.ENDER_PEARL).setAmount(2).setName("§9§lRauchgranate").toItemStack();
                player.getInventory().setItem(5, enderPearl);
            }

            if(item.getType().equals(Material.FIREWORK_ROCKET)) {
                event.setCancelled(true);
            }

            if(item.getType().equals(Material.BLAZE_ROD)) {
                if(!cooldown.contains(player)) {
                    player.addPotionEffect(PotionEffectType.LEVITATION.createEffect(80, 1).withParticles(false));
                    cooldown.add(player);
                    Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), () -> cooldown.remove(player), 100);
                } else {
                    player.sendMessage(LobbySystem.getPrefix() + "Bitte warte einen Moment.");
                }
            }

            if(item.getType().equals(Material.FEATHER)) {
                if(!cooldown.contains(player)) {
                    player.setVelocity(player.getLocation().getDirection().setY(1));
                    cooldown.add(player);
                    Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), () -> cooldown.remove(player), 100);
                } else {
                    player.sendMessage(LobbySystem.getPrefix() + "Bitte warte einen Moment.");
                }
            }

            if(item.getType().equals(Material.BOW)) {
                playerInv.setItem(33, new ItemBuilder(Material.ARROW).setName("").toItemStack());
                if(!cooldown.contains(player)) {
                    cooldown.add(player);
                    Bukkit.getScheduler().runTaskLater(LobbySystem.getInstance(), () -> cooldown.remove(player), 100);
                }
            }
        }

    }

    public void openGadgets(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 5, "§8§lSpielzeuge");
        Inventory playerInv = player.getInventory();
        fillInv(inv);

        for (int i = 10; i < 35; i++) {
            if (i == 17 || i == 18 || i == 26 || i == 27) {
                continue;
            }
            inv.setItem(i, grayGlass);
        }

        var snowBall = new ItemBuilder(Material.SNOWBALL).setName("§b§lSchneeball");
        var bow = new ItemBuilder(Material.BOW).setName("§d§lGeisterbogen").setUnbreakable(true).addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        var rod = new ItemBuilder(Material.FISHING_ROD).setName("§e§lGreifhaken").setUnbreakable(true).addItemFlag(ItemFlag.HIDE_UNBREAKABLE);
        var enderPearl = new ItemBuilder(Material.ENDER_PEARL).setName("§9§lRauchgranate");
        var blazeRod = new ItemBuilder(Material.BLAZE_ROD).setName("§c§lZauberstab");
        var feather = new ItemBuilder(Material.FEATHER).setName("§f§lSprungfeder");

        if (playerInv.contains(Material.FISHING_ROD)) {
            inv.setItem(11, rod.addLore(Arrays.asList("§7Bereits ausgewählt")).toItemStack());
        } else {
            inv.setItem(11, rod.addLore(Arrays.asList("§aAuswählen")).toItemStack());
        }

        if (playerInv.contains(Material.ENDER_PEARL)) {
            inv.setItem(13, enderPearl.addLore(Arrays.asList("§7Bereits ausgewählt")).toItemStack());
        } else {
            inv.setItem(13, enderPearl.addLore(Arrays.asList("§aAuswählen")).toItemStack());
        }

        if (playerInv.contains(Material.SNOWBALL)) {
            inv.setItem(15, snowBall.addLore(Arrays.asList("§7Bereits ausgewählt")).toItemStack());
        } else {
            inv.setItem(15, snowBall.addLore(Arrays.asList("§aAuswählen")).toItemStack());
        }

        if (playerInv.contains(Material.FEATHER)) {
            inv.setItem(29, feather.addLore(Arrays.asList("§7Bereits ausgewählt")).toItemStack());
        } else {
            inv.setItem(29, feather.addLore(Arrays.asList("§aAuswählen")).toItemStack());
        }

        if (playerInv.contains(Material.BOW)) {
            inv.setItem(31, bow.addLore(Arrays.asList("§7Bereits ausgewählt")).toItemStack());
        } else {
            inv.setItem(31, bow.addLore(Arrays.asList("§aAuswählen")).toItemStack());
        }

        if (playerInv.contains(Material.BLAZE_ROD)) {
            inv.setItem(33, blazeRod.addLore(Arrays.asList("§7Bereits ausgewählt")).toItemStack());
        } else {
            inv.setItem(33, blazeRod.addLore(Arrays.asList("§aAuswählen")).toItemStack());
        }

        player.openInventory(inv);
    }

    public void openSettings(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, "§8§lEinstellungen");
        fillInv(inv);

        for (int i = 10; i < 17; i++) {
            inv.setItem(i, grayGlass);
        }

        if (manager.getOption(player, "hider")) {
            inv.setItem(11, new ItemBuilder(Material.LIME_DYE).setName("§d§lSichtbarkeit").addLore(Arrays.asList("§aAlle Spieler sichtbar")).toItemStack());
        } else {
            inv.setItem(11, new ItemBuilder(Material.RED_DYE).setName("§d§lSichtbarkeit").addLore(Arrays.asList("§cKeine Spieler sichtbar")).toItemStack());
        }

        if (manager.getOption(player, "broadcast")) {
            inv.setItem(13, new ItemBuilder(Material.BELL).setName("§2§lBroadcast-Nachrichten").addLore(Arrays.asList("§aAktiviert")).toItemStack());
        } else {
            inv.setItem(13, new ItemBuilder(Material.BELL).setName("§2§lBroadcast-Nachrichten").addLore(Arrays.asList("§cDeaktiviert")).toItemStack());
        }

        inv.setItem(15, new ItemBuilder(Material.PUFFERFISH).setName("§c§lKommt bald").toItemStack());
        player.openInventory(inv);
    }

    public void openNavigator(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, "§8§lNavigator");

        fillInv(inv);

        for (int i = 10; i < 17; i++) {
            inv.setItem(i, grayGlass);
        }

        ItemStack cbItem = new ItemBuilder(Material.OAK_SAPLING).setName("§b§lCitybuild").addLore(Arrays.asList("§7Klicke um auf den Server zu connecten.")).toItemStack();
        ItemStack spawnItem = new ItemBuilder(Material.RAW_GOLD_BLOCK).setName("§a§lLobby-Spawn").addLore(Arrays.asList("§7Klicke um dich zum Lobby-Spawn zu teleportieren.")).toItemStack();
        inv.setItem(12, cbItem);
        inv.setItem(14, spawnItem);
        player.openInventory(inv);
    }

    public Inventory fillInv(Inventory inv) {
        ItemStack blackGlass = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("").toItemStack();
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, blackGlass);
        }
        return inv;
    }

}
