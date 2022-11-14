// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.Listener;

public class RPPlayerListener implements Listener
{
    RedProtect plugin;
    
    public RPPlayerListener(final RedProtect plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        final Block b = event.getClickedBlock();
        if (b == null) {
            return;
        }
        Region r = null;
        final Material itemInHand = p.getItemInHand().getType();
        if (p.getItemInHand().getTypeId() == RedProtect.adminWandID) {
            if (event.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) {
                if (p.hasPermission("redprotect.magicwand")) {
                    RedProtect.secondLocationSelections.put(p, b.getLocation());
                    p.sendMessage(ChatColor.AQUA + "Set the second magic wand location to (" + ChatColor.GOLD + b.getLocation().getBlockX() + ChatColor.AQUA + ", " + ChatColor.GOLD + b.getLocation().getBlockY() + ChatColor.AQUA + ", " + ChatColor.GOLD + b.getLocation().getBlockZ() + ChatColor.AQUA + ").");
                    event.setCancelled(true);
                    return;
                }
            }
            else if (event.getAction().equals((Object)Action.LEFT_CLICK_BLOCK) && p.hasPermission("redprotect.magicwand")) {
                RedProtect.firstLocationSelections.put(p, b.getLocation());
                p.sendMessage(ChatColor.AQUA + "Set the first magic wand location to (" + ChatColor.GOLD + b.getLocation().getBlockX() + ChatColor.AQUA + ", " + ChatColor.GOLD + b.getLocation().getBlockY() + ChatColor.AQUA + ", " + ChatColor.GOLD + b.getLocation().getBlockZ() + ChatColor.AQUA + ").");
                event.setCancelled(true);
                return;
            }
        }
        if (p.getItemInHand().getTypeId() == RedProtect.infoWandID) {
            if (event.getAction().equals((Object)Action.RIGHT_CLICK_AIR)) {
                r = RedProtect.rm.getRegion(p.getLocation());
            }
            else if (event.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK)) {
                r = RedProtect.rm.getRegion(b.getLocation());
            }
            if (p.hasPermission("redprotect.infowand")) {
                if (r == null) {
                    p.sendMessage(ChatColor.RED + "There is no region at that block's location!");
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "--------------- [" + ChatColor.GOLD + r.getName() + ChatColor.AQUA + "] ---------------");
                    p.sendMessage(r.info());
                    p.sendMessage(r.getFlagInfo());
                }
                event.setCancelled(true);
                return;
            }
        }
        if (b.getType().equals((Object)Material.CHEST)) {
            r = RedProtect.rm.getRegion(b.getLocation());
            if (r == null) {
                return;
            }
            if (!r.canChest(p)) {
                if (!RedProtect.ph.hasPerm(p, "redprotect.bypass")) {
                    p.sendMessage(ChatColor.RED + "You can't open this chest!");
                    event.setCancelled(true);
                }
                else {
                    p.sendMessage(ChatColor.YELLOW + "Opened locked chest in " + r.getCreator() + "'s region.");
                }
            }
        }
        else if (b.getType().equals((Object)Material.DISPENSER)) {
            r = RedProtect.rm.getRegion(b.getLocation());
            if (r == null) {
                return;
            }
            if (!r.canChest(p)) {
                if (!RedProtect.ph.hasPerm(p, "redprotect.bypass")) {
                    p.sendMessage(ChatColor.RED + "You can't open this dispenser!");
                    event.setCancelled(true);
                }
                else {
                    p.sendMessage(ChatColor.YELLOW + "Opened locked dispenser in " + r.getCreator() + "'s region.");
                }
            }
        }
        else if (b.getType().equals((Object)Material.FURNACE) || b.getType().equals((Object)Material.BURNING_FURNACE)) {
            r = RedProtect.rm.getRegion(b.getLocation());
            if (r == null) {
                return;
            }
            if (!r.canChest(p)) {
                if (!RedProtect.ph.hasPerm(p, "redprotect.bypass")) {
                    p.sendMessage(ChatColor.RED + "You can't open this furnace!");
                    event.setCancelled(true);
                }
                else {
                    p.sendMessage(ChatColor.YELLOW + "Opened locked furnace in " + r.getCreator() + "'s region.");
                }
            }
        }
        else if (b.getType().equals((Object)Material.LEVER)) {
            r = RedProtect.rm.getRegion(b.getLocation());
            if (r == null) {
                return;
            }
            if (!r.canLever(p)) {
                if (!RedProtect.ph.hasPerm(p, "redprotect.bypass")) {
                    p.sendMessage(ChatColor.RED + "You can't toggle this lever!");
                    event.setCancelled(true);
                }
                else {
                    p.sendMessage(ChatColor.YELLOW + "Toggled locked lever in " + r.getCreator() + "'s region.");
                }
            }
        }
        else if (b.getType().equals((Object)Material.STONE_BUTTON) || b.getType().equals((Object)Material.WOOD_BUTTON)) {
            r = RedProtect.rm.getRegion(b.getLocation());
            if (r == null) {
                return;
            }
            if (!r.canButton(p)) {
                if (!RedProtect.ph.hasPerm(p, "redprotect.bypass")) {
                    p.sendMessage(ChatColor.RED + "You can't activate this button!");
                    event.setCancelled(true);
                }
                else {
                    p.sendMessage(ChatColor.YELLOW + "Activated locked button in " + r.getCreator() + "'s region.");
                }
            }
        }
        else if (b.getType().equals((Object)Material.WOODEN_DOOR)) {
            r = RedProtect.rm.getRegion(b.getLocation());
            if (r == null) {
                return;
            }
            if (!r.canDoor(p)) {
                if (!RedProtect.ph.hasPerm(p, "redprotect.bypass")) {
                    p.sendMessage(ChatColor.RED + "You can't open this door!");
                    event.setCancelled(true);
                }
                else {
                    p.sendMessage(ChatColor.YELLOW + "Opened locked door in " + r.getCreator() + "'s region.");
                }
            }
        }
        if ((itemInHand.equals((Object)Material.FLINT_AND_STEEL) || itemInHand.equals((Object)Material.WATER_BUCKET) || itemInHand.equals((Object)Material.LAVA_BUCKET) || itemInHand.equals((Object)Material.PAINTING) || itemInHand.equals((Object)Material.ITEM_FRAME)) && !RedProtect.rm.canBuild(p, b, b.getWorld())) {
            p.sendMessage(ChatColor.RED + "You can't use that here!");
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEntityEvent event) {
        final Entity e = event.getRightClicked();
        final Player p = event.getPlayer();
        if (e instanceof ItemFrame) {
            final Region r = RedProtect.rm.getRegion(e.getLocation());
            if (r != null && !r.canBuild(p)) {
                p.sendMessage(ChatColor.RED + "You can't edit that item frame here!");
                event.setCancelled(true);
            }
        }
    }
}
