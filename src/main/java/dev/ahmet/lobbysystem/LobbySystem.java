package dev.ahmet.lobbysystem;

import dev.ahmet.lobbysystem.commands.BuildCommand;
import dev.ahmet.lobbysystem.commands.PartnerCommand;
import dev.ahmet.lobbysystem.commands.SetSpawnCommand;
import dev.ahmet.lobbysystem.commands.SpawnCommand;
import dev.ahmet.lobbysystem.database.Database;
import dev.ahmet.lobbysystem.events.*;
import dev.ahmet.lobbysystem.manager.LobbyManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class LobbySystem extends JavaPlugin {

    private static LobbySystem instance;
    private Database database;
    private LobbyManager manager;
    private List<Player> buildList;

    @Override
    public void onEnable() {
        instance = this;
        database = new Database();
        database.createTables();
        manager = new LobbyManager();
        buildList = new ArrayList<>();
        saveDefaultConfig();
        startBroadcastTimer(getServer().getScheduler());
        registerAll(Bukkit.getPluginManager());

        getLogger().info("Lumania LobbySystem by 169ahmet");
    }

    public static String getPrefix() {return "§b§lLUMANIA §8• §7";}

    public LobbyManager getManager() {
        return manager;
    }

    public List<Player> getBuildList() {
        return buildList;
    }

    public static LobbySystem getInstance() {
        return instance;
    }

    private void startBroadcastTimer(BukkitScheduler scheduler) {
        int scheduleId = scheduler.scheduleSyncDelayedTask(instance, () -> {
            if (getConfig().getBoolean("broadcasts-enabled")) {
                Set<String> broadcastsList = getConfig().getConfigurationSection("broadcasts").getKeys(false);
                String broadcastId = getRandomElement(broadcastsList);
                ConfigurationSection broadcast = getConfig().getConfigurationSection("broadcasts." + broadcastId);

                for (String message : broadcast.getStringList("messages")) {
                    for(Player player : getServer().getOnlinePlayers()) {
                        if(manager.getOption(player, "broadcast")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }

                    }
                }

                if (broadcast.getString("sound") != null) {
                    for (Player player : getServer().getOnlinePlayers()) {
                        if(manager.getOption(player, "broadcast")) {
                            try {
                                player.playSound(player.getLocation(), Sound.valueOf(broadcast.getString("sound")), 5, 5);
                            } catch (Exception e) {
                                throw new RuntimeException("An error occured, ", e);
                            }
                        }
                    }
                }
            }

            startBroadcastTimer(scheduler);
        }, getConfig().getLong("broadcast-interval"));
    }

    private String getRandomElement(Set<String> set) {
        int index = new Random().nextInt(set.size());
        Iterator<String> iter = set.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    public void registerAll(PluginManager pm) {
        pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new InventoryClickListener(), this);
        pm.registerEvents(new PlayerMoveListener(), this);
        pm.registerEvents(new ProtectListeners(), this);
        pm.registerEvents(new GadgetEvents(), this);
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("build").setExecutor(new BuildCommand());
        getCommand("partner").setExecutor(new PartnerCommand());
    }
}
