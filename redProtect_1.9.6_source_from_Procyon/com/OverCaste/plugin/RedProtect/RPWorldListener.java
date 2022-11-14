// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.World;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.Listener;

public class RPWorldListener implements Listener
{
    RedProtect plugin;
    
    public RPWorldListener(final RedProtect plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldLoad(final WorldLoadEvent e) {
        final World w = e.getWorld();
        try {
            RedProtect.rm.load(w);
            RedProtect.logger.debug("World loaded: " + w.getName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onWorldUnload(final WorldUnloadEvent e) {
        final World w = e.getWorld();
        try {
            RedProtect.rm.unload(w);
            RedProtect.logger.debug("World unloaded: " + w.getName());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
