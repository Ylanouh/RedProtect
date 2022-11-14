// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.Iterator;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.Location;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

public class RPEntityListener implements Listener
{
    RedProtect plugin;
    static final String noPvPMsg;
    static final String noPassiveHurtMsg;
    
    static {
        noPvPMsg = ChatColor.RED + "You can't PvP in this region!";
        noPassiveHurtMsg = ChatColor.RED + "You can't hurt passive entities in this region!";
    }
    
    public RPEntityListener(final RedProtect plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityTarget(final EntityTargetEvent e) {
        final Entity target = e.getTarget();
        if (target == null) {
            return;
        }
        final Region r = RedProtect.rm.getRegion(target.getLocation());
        if (r != null && !r.canMobs()) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        final Entity e = (Entity)event.getEntity();
        if (e == null) {
            return;
        }
        if (e instanceof Monster) {
            final Region r = RedProtect.rm.getRegion(e.getLocation());
            if (r != null && !r.canMobs() && event.getSpawnReason().equals((Object)CreatureSpawnEvent.SpawnReason.NATURAL)) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event instanceof EntityDamageByEntityEvent) {
            final EntityDamageByEntityEvent de = (EntityDamageByEntityEvent)event;
            final Entity e1 = de.getEntity();
            Entity e2 = de.getDamager();
            if (e2 == null) {
                return;
            }
            if (e2 instanceof Arrow) {
                Arrow a = (Arrow)e2;
                e2 = (Entity)a.getShooter();
                a = null;
                if (e2 == null) {
                    return;
                }
            }
            final Region r1 = RedProtect.rm.getRegion(e1.getLocation());
            final Region r2 = RedProtect.rm.getRegion(e2.getLocation());
            if (e1 instanceof Player) {
                if (e2 instanceof Player) {
                    final Player p2 = (Player)e2;
                    if (r1 != null) {
                        if (r2 != null) {
                            if (!r1.canPVP(p2) || !r2.canPVP(p2)) {
                                event.setCancelled(true);
                                p2.sendMessage(RPEntityListener.noPvPMsg);
                            }
                        }
                        else if (!r1.canPVP(p2)) {
                            event.setCancelled(true);
                            p2.sendMessage(RPEntityListener.noPvPMsg);
                        }
                    }
                    else if (r2 != null && !r2.canPVP(p2)) {
                        event.setCancelled(true);
                        p2.sendMessage(RPEntityListener.noPvPMsg);
                    }
                }
            }
            else if (e1 instanceof Animals || e1 instanceof Villager) {
                final Region r3 = RedProtect.rm.getRegion(e1.getLocation());
                if (r3 != null) {
                    if (e2 instanceof Player) {
                        final Player p3 = (Player)e2;
                        if (!r3.canHurtPassives(p3)) {
                            event.setCancelled(true);
                            p3.sendMessage(RPEntityListener.noPassiveHurtMsg);
                        }
                    }
                    else if (!r3.getFlag(6)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onHangingDamaged(final HangingBreakByEntityEvent event) {
        final Entity remover = event.getRemover();
        if (remover instanceof Player) {
            final Player player = (Player)remover;
            final Location loc = event.getEntity().getLocation();
            final Region r = RedProtect.rm.getRegion(loc);
            if (r != null && !r.canBuild(player)) {
                player.sendMessage(ChatColor.RED + "You can't build here!");
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPotionSplash(final PotionSplashEvent event) {
        final LivingEntity thrower = event.getPotion().getShooter();
        for (final PotionEffect e : event.getPotion().getEffects()) {
            final PotionEffectType t = e.getType();
            if (!t.equals((Object)PotionEffectType.BLINDNESS) && !t.equals((Object)PotionEffectType.CONFUSION) && !t.equals((Object)PotionEffectType.HARM) && !t.equals((Object)PotionEffectType.HUNGER) && !t.equals((Object)PotionEffectType.POISON) && !t.equals((Object)PotionEffectType.SLOW) && !t.equals((Object)PotionEffectType.SLOW_DIGGING) && !t.equals((Object)PotionEffectType.WEAKNESS) && !t.equals((Object)PotionEffectType.WITHER)) {
                return;
            }
        }
        Player shooter;
        if (thrower instanceof Player) {
            shooter = (Player)thrower;
        }
        else {
            shooter = null;
        }
        for (final Entity e2 : event.getAffectedEntities()) {
            final Region r = RedProtect.rm.getRegion(e2.getLocation());
            if (r != null && !r.canPVP(shooter)) {
                event.setCancelled(true);
            }
        }
    }
}
