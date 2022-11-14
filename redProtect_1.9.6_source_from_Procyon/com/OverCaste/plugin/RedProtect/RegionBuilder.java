// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.block.SignChangeEvent;

public abstract class RegionBuilder
{
    Region r;
    
    public RegionBuilder() {
        this.r = null;
    }
    
    public boolean ready() {
        return this.r != null;
    }
    
    public Region build() {
        return this.r;
    }
    
    void setErrorSign(final SignChangeEvent e, final String error) {
        e.setLine(0, ChatColor.RED + "[RP] Error");
        this.setError(e.getPlayer(), error);
    }
    
    void setError(final Player p, final String error) {
        p.sendMessage(ChatColor.RED + "[RP] There was an error creating that region! (" + error + ")");
    }
}
