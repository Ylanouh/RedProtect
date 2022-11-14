// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.Plugin;
import net.milkbowl.vault.Vault;
import org.bukkit.Bukkit;
import net.milkbowl.vault.chat.Chat;

public class RPPermissionHandler
{
    final boolean vaultFound;
    final Chat permission;
    
    public RPPermissionHandler() throws Exception {
        final Plugin p = Bukkit.getPluginManager().getPlugin("Vault");
        if (p == null || !(p instanceof Vault)) {
            RedProtect.logger.warning("Vault not found, player limits will be set to the default.");
            this.vaultFound = false;
            this.permission = null;
        }
        else {
            final RegisteredServiceProvider<Chat> provider = (RegisteredServiceProvider<Chat>)Bukkit.getServer().getServicesManager().getRegistration((Class)Chat.class);
            if (provider == null) {
                RedProtect.logger.warning("Vault couldn't find an acceptable permission plugin! Player limits set to default.");
                this.vaultFound = false;
                this.permission = null;
                return;
            }
            this.vaultFound = true;
            this.permission = (Chat)provider.getProvider();
        }
    }
    
    public boolean hasPerm(final Player p, final String perm) {
        return p != null && p.hasPermission(perm);
    }
    
    public boolean hasPerm(final String pl, final String perm) {
        final Player p = Bukkit.getServer().getPlayerExact(pl);
        return p != null && p.hasPermission(perm);
    }
    
    public boolean hasRegionPerm(final Player p, final String s, final Region poly) {
        final String adminperm = "redprotect.admin." + s;
        final String userperm = "redprotect.own." + s;
        if (poly == null) {
            return this.hasPerm(p, adminperm) || this.hasPerm(p, userperm);
        }
        return this.hasPerm(p, adminperm) || (this.hasPerm(p, userperm) && poly.isOwner(p));
    }
    
    public boolean hasHelpPerm(final Player p, final String s) {
        final String adminperm = "redprotect.admin." + s;
        final String userperm = "redprotect.own." + s;
        return this.hasPerm(p, adminperm) || this.hasPerm(p, userperm);
    }
    
    public int getPlayerLimit(final Player p) {
        if (!this.vaultFound) {
            return RedProtect.limitAmount;
        }
        return this.permission.getPlayerInfoInteger(p, "maxregionsize", RedProtect.limitAmount);
    }
}
