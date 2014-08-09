package net.coasterman10.spectate;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
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

public class SpectateAPI extends JavaPlugin {
    private static SpectateAPI instance;

    private Collection<UUID> spectators = new HashSet<UUID>();

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    /**
     * Puts a player into spectator mode. The player will be allowed to fly and hidden from all other players, as well
     * as unable to interact with the world in any way.
     * 
     * @param p The player to put into spectator mode
     */
    public static void addSpectator(Player p) {
        for (Player other : Bukkit.getOnlinePlayers())
            other.hidePlayer(p);
        p.setAllowFlight(true);
        instance.spectators.add(p.getUniqueId());
    }

    /**
     * Removes a player from spectator mode. The player will be no longer hidden or allowed to fly, and once again
     * allowed to interact with the world.
     * 
     * @param p The player to remove from spectator mode
     */
    public static void removeSpectator(Player p) {
        for (Player other : Bukkit.getOnlinePlayers())
            other.showPlayer(p);
        p.setAllowFlight(false);
        instance.spectators.remove(p.getUniqueId());
    }

    /**
     * Checks whether a player is in spectator mode
     * 
     * @param p The player to check
     * @return Whether the player is a spectator
     */
    public static boolean isSpectator(Player p) {
        return instance.spectators.contains(p.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removeSpectator(e.getPlayer());
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
