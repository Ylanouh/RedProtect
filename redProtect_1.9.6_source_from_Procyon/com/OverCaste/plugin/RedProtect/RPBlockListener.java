// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.Listener;

public class RPBlockListener implements Listener
{
    RedProtect plugin;
    
    public RPBlockListener(final RedProtect plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignChange(final SignChangeEvent e) {
        final Block b = e.getBlock();
        final Player p = e.getPlayer();
        if (e.isCancelled() || b == null) {
            this.setErrorSign(e, p, "The block you placed was null!");
            return;
        }
        final String[] lines = e.getLines();
        final String line = lines[0].toLowerCase();
        if (!line.equals("[rp]") && !line.equals("[p]") && !line.equals("[protect]")) {
            return;
        }
        if (lines.length != 4) {
            this.setErrorSign(e, p, "The number of lines on your sign is wrong!");
            return;
        }
        if (!RedProtect.ph.hasPerm(p, "redprotect.create")) {
            this.setErrorSign(e, p, "You don't have permission to make regions!");
            return;
        }
        final RegionBuilder rb = new EncompassRegionBuilder(e);
        if (rb.ready()) {
            final Region r = rb.build();
            e.setLine(0, ChatColor.GREEN + "[RP]: Done.");
            p.sendMessage(ChatColor.AQUA + "Created a region with name: " + ChatColor.GOLD + r.getName() + ChatColor.AQUA + ", with you as owner.");
            RedProtect.rm.add(r, p.getWorld());
        }
    }
    
    void setErrorSign(final SignChangeEvent e, final Player p, final String error) {
        e.setLine(0, ChatColor.RED + "[RP]: Error");
        p.sendMessage(ChatColor.RED + "[RP] ERROR:" + error);
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(final BlockPlaceEvent e) {
        try {
            final Block b = e.getBlock();
            final Player p = e.getPlayer();
            if (!RedProtect.rm.canBuild(p, b, p.getWorld())) {
                p.sendMessage(ChatColor.RED + "You can't build here!");
                e.setCancelled(true);
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(final BlockBreakEvent e) {
        final Player p = e.getPlayer();
        final Block b = e.getBlock();
        if (!RedProtect.rm.canBuild(p, b, p.getWorld())) {
            p.sendMessage(ChatColor.RED + "You can't build here!");
            e.setCancelled(true);
        }
    }
}
