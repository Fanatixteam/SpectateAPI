package net.coasterman10.spectate;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Spectate extends JavaPlugin {
    private static Spectate instance;

    private Collection<UUID> spectators = new HashSet<UUID>();

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static void addSpectator(Player p) {
        addSpectator(p.getUniqueId());
    }

    public static void addSpectator(UUID id) {
        instance.spectators.add(id);
    }

    public static void removeSpectator(Player p) {
        addSpectator(p.getUniqueId());
    }

    public static void removeSpectator(UUID id) {
        instance.spectators.remove(id);
    }

    public static boolean isSpectator(Player p) {
        return isSpectator(p.getUniqueId());
    }

    public static boolean isSpectator(UUID id) {
        return instance.spectators.contains(id);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        spectators.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (isSpectator(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (isSpectator(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (isSpectator(e.getPlayer()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && isSpectator((Player) e.getEntity()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof Player && isSpectator((Player) e.getEntity()))
                || e.getDamager() instanceof Player && isSpectator((Player) e.getDamager()))
            e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityInteract(EntityInteractEvent e) {
        if (e.getEntity() instanceof Player && isSpectator((Player) e.getEntity()))
            e.setCancelled(true);
    }
}
