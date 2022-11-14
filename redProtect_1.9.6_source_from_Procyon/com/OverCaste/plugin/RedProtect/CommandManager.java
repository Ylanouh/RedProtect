// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.Iterator;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;

public class CommandManager implements CommandExecutor
{
    static final String NOT_IN_REGION_MESSAGE;
    static final String NO_PERMISSION_MESSAGE;
    
    static {
        NOT_IN_REGION_MESSAGE = ChatColor.RED + "You need to be in a region or define a region to do that!";
        NO_PERMISSION_MESSAGE = ChatColor.RED + "You don't have permission to do that!";
    }
    
    private static void sendNotInRegionMessage(final Player p) {
        p.sendMessage(CommandManager.NOT_IN_REGION_MESSAGE);
    }
    
    private static void sendNoPermissionMessage(final Player p) {
        p.sendMessage(CommandManager.NO_PERMISSION_MESSAGE);
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You can't use RedProtect from the console!");
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 0) {
            player.sendMessage(ChatColor.AQUA + "redProtect version " + RedProtect.pdf.getVersion());
            player.sendMessage(ChatColor.AQUA + "Developed by (" + ChatColor.GOLD + "ikillforeyou [aka. OverCaste]" + ChatColor.AQUA + ").");
            player.sendMessage(ChatColor.AQUA + "For more information about the commands, type [" + ChatColor.GOLD + "/rp ?" + ChatColor.AQUA + "].");
            player.sendMessage(ChatColor.AQUA + "For a tutorial, type [" + ChatColor.GOLD + "/rp tutorial" + ChatColor.AQUA + "].");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.AQUA + "Available commands to you: ");
                player.sendMessage(ChatColor.AQUA + "------------------------------------");
                if (RedProtect.ph.hasHelpPerm(player, "limit")) {
                    player.sendMessage(ChatColor.GREEN + "/rp limit");
                }
                if (RedProtect.ph.hasHelpPerm(player, "list")) {
                    player.sendMessage(ChatColor.GREEN + "/rp list");
                }
                if (RedProtect.ph.hasHelpPerm(player, "delete")) {
                    player.sendMessage(ChatColor.GREEN + "/rp delete");
                }
                if (RedProtect.ph.hasHelpPerm(player, "info")) {
                    player.sendMessage(ChatColor.GREEN + "/rp info");
                }
                if (RedProtect.ph.hasHelpPerm(player, "addmember")) {
                    player.sendMessage(ChatColor.GREEN + "/rp addmember (player)");
                }
                if (RedProtect.ph.hasHelpPerm(player, "addowner")) {
                    player.sendMessage(ChatColor.GREEN + "/rp addowner (player)");
                }
                if (RedProtect.ph.hasHelpPerm(player, "removemember")) {
                    player.sendMessage(ChatColor.GREEN + "/rp removemember (player)");
                }
                if (RedProtect.ph.hasHelpPerm(player, "removeowner")) {
                    player.sendMessage(ChatColor.GREEN + "/rp removeowner (player)");
                }
                if (RedProtect.ph.hasHelpPerm(player, "rename")) {
                    player.sendMessage(ChatColor.GREEN + "/rp rename (name)");
                }
                if (RedProtect.ph.hasPerm(player, "redprotect.near")) {
                    player.sendMessage(ChatColor.GREEN + "/rp near");
                }
                player.sendMessage(ChatColor.GREEN + "/rp flag");
                player.sendMessage(ChatColor.AQUA + "------------------------------------");
                return true;
            }
            if (args[0].equalsIgnoreCase("limit") || args[0].equalsIgnoreCase("limitremaining") || args[0].equalsIgnoreCase("remaining")) {
                if (!RedProtect.ph.hasPerm(player, "redprotect.own.limit")) {
                    player.sendMessage(ChatColor.RED + "You don't have sufficient permission to do that.");
                    return true;
                }
                final int limit = RedProtect.ph.getPlayerLimit(player);
                if (limit < 0 || RedProtect.ph.hasPerm(player, "redprotect.unlimited")) {
                    player.sendMessage(ChatColor.AQUA + "You have no limit!");
                    return true;
                }
                final int currentUsed = RedProtect.rm.getTotalRegionSize(player.getName());
                player.sendMessage(ChatColor.AQUA + "Your area: (" + ChatColor.GOLD + currentUsed + ChatColor.AQUA + " / " + ChatColor.GOLD + limit + ChatColor.AQUA + ").");
                return true;
            }
            else if (args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("ls")) {
                if (RedProtect.ph.hasPerm(player, "redprotect.own.list")) {
                    final Set<Region> regions = RedProtect.rm.getRegions(player);
                    if (regions.size() == 0) {
                        player.sendMessage(ChatColor.AQUA + "You don't have any regions!");
                    }
                    else {
                        player.sendMessage(ChatColor.AQUA + "Regions you've created:");
                        player.sendMessage(ChatColor.AQUA + "------------------------------------");
                        final Iterator<Region> i = regions.iterator();
                        while (i.hasNext()) {
                            player.sendMessage(ChatColor.AQUA + i.next().info());
                        }
                        player.sendMessage(ChatColor.AQUA + "------------------------------------");
                    }
                    return true;
                }
                player.sendMessage(ChatColor.RED + "You don't have sufficient permission to do that.");
                return true;
            }
            else {
                if (args[0].equalsIgnoreCase("tutorial") || args[0].equalsIgnoreCase("tut")) {
                    player.sendMessage(ChatColor.AQUA + "Tutorial:");
                    player.sendMessage(ChatColor.AQUA + "1. Surround your creation with " + RPUtil.formatName(Material.getMaterial(RedProtect.blockID).name()) + ".");
                    player.sendMessage(ChatColor.AQUA + "2. Place a sign next to your region, with [rp] on the first line.");
                    player.sendMessage(ChatColor.AQUA + "3. Enter the name you want your region to be on the 2nd line, or nothing for an automatic name.");
                    player.sendMessage(ChatColor.AQUA + "4. Enter 2 additional owners, if you want, on lines 3 & 4.");
                    return true;
                }
                if (args[0].equalsIgnoreCase("near") || args[0].equalsIgnoreCase("nr")) {
                    if (RedProtect.ph.hasPerm(player, "redprotect.near")) {
                        final Set<Region> regions = RedProtect.rm.getRegionsNear(player, 30, player.getWorld());
                        if (regions.size() == 0) {
                            player.sendMessage(ChatColor.AQUA + "There are no regions nearby.");
                        }
                        else {
                            final Iterator<Region> i = regions.iterator();
                            player.sendMessage(ChatColor.AQUA + "Regions within 40 blocks: ");
                            player.sendMessage(ChatColor.AQUA + "------------------------------------");
                            while (i.hasNext()) {
                                final Region r = i.next();
                                player.sendMessage(ChatColor.AQUA + "Name: " + ChatColor.GOLD + r.getName() + ChatColor.AQUA + ", Center: [" + ChatColor.GOLD + r.getCenterX() + ChatColor.AQUA + ", " + ChatColor.GOLD + r.getCenterZ() + ChatColor.AQUA + "].");
                            }
                            player.sendMessage(ChatColor.AQUA + "------------------------------------");
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "You don't have permission to do that.");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("flag")) {
                    player.sendMessage(ChatColor.AQUA + "To use the command, type '/rp flag (flag)'. This will toggle the specific flag if you have permission.");
                    player.sendMessage(ChatColor.AQUA + "Type '/rp flag info' to show the status of flags in the region you're currently in.");
                    return true;
                }
            }
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("redefine")) {
                if (!player.hasPermission("redprotect.admin.redefine")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
                    return true;
                }
                final String name = args[1];
                final Region oldRect = RedProtect.rm.getRegion(name, player.getWorld());
                if (oldRect == null) {
                    player.sendMessage(ChatColor.RED + "That region doesn't exist!");
                    return true;
                }
                final RedefineRegionBuilder rb = new RedefineRegionBuilder(player, oldRect, RedProtect.firstLocationSelections.get(player), RedProtect.secondLocationSelections.get(player));
                if (rb.ready()) {
                    final Region r2 = rb.build();
                    player.sendMessage(ChatColor.GREEN + "Successfully created region: " + r2.getName() + ".");
                    RedProtect.rm.remove(oldRect);
                    RedProtect.rm.add(r2, player.getWorld());
                }
                return true;
            }
        }
        else if (args.length <= 3 && args[0].equalsIgnoreCase("define")) {
            if (!player.hasPermission("redprotect.admin.define")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to do that!");
                return true;
            }
            final String name = (args.length >= 2) ? args[1] : "";
            final String creator = (args.length == 3) ? args[2] : player.getName().toLowerCase();
            final RegionBuilder rb2 = new DefineRegionBuilder(player, RedProtect.firstLocationSelections.get(player), RedProtect.secondLocationSelections.get(player), name, creator);
            if (rb2.ready()) {
                final Region r2 = rb2.build();
                player.sendMessage(ChatColor.GREEN + "Successfully created region: " + r2.getName() + ".");
                RedProtect.rm.add(r2, player.getWorld());
            }
            return true;
        }
        if (args.length <= 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del")) {
            if (args.length == 1) {
                handleDelete(player, RedProtect.rm.getRegion(player, player.getWorld()));
                return true;
            }
            if (args.length == 2) {
                handleDelete(player, RedProtect.rm.getRegion(args[1], player.getWorld()));
                return true;
            }
            return false;
        }
        else if (args[0].equalsIgnoreCase("i") || args[0].equalsIgnoreCase("info")) {
            if (args.length == 1) {
                handleInfo(player, RedProtect.rm.getRegion(player, player.getWorld()));
                return true;
            }
            if (args.length == 2) {
                handleInfo(player, RedProtect.rm.getRegion(args[1], player.getWorld()));
                return true;
            }
            return false;
        }
        else if (args[0].equalsIgnoreCase("am") || args[0].equalsIgnoreCase("addmember")) {
            if (args.length == 2) {
                handleAddMember(player, args[1], RedProtect.rm.getRegion(player, player.getWorld()));
                return true;
            }
            if (args.length == 3) {
                handleAddMember(player, args[1], RedProtect.rm.getRegion(args[2], player.getWorld()));
                return true;
            }
            return false;
        }
        else if (args[0].equalsIgnoreCase("ao") || args[0].equalsIgnoreCase("addowner")) {
            if (args.length == 2) {
                handleAddOwner(player, args[1], RedProtect.rm.getRegion(player, player.getWorld()));
                return true;
            }
            if (args.length == 3) {
                handleAddOwner(player, args[1], RedProtect.rm.getRegion(args[2], player.getWorld()));
                return true;
            }
            return false;
        }
        else if (args[0].equalsIgnoreCase("rm") || args[0].equalsIgnoreCase("removemember")) {
            if (args.length == 2) {
                handleRemoveMember(player, args[1], RedProtect.rm.getRegion(player, player.getWorld()));
                return true;
            }
            if (args.length == 3) {
                handleRemoveMember(player, args[1], RedProtect.rm.getRegion(args[2], player.getWorld()));
                return true;
            }
            return false;
        }
        else if (args[0].equalsIgnoreCase("ro") || args[0].equalsIgnoreCase("removeowner")) {
            if (args.length == 2) {
                handleRemoveOwner(player, args[1], RedProtect.rm.getRegion(player, player.getWorld()));
                return true;
            }
            if (args.length == 3) {
                handleRemoveOwner(player, args[1], RedProtect.rm.getRegion(args[2], player.getWorld()));
                return true;
            }
            return false;
        }
        else if (args[0].equalsIgnoreCase("rn") || args[0].equalsIgnoreCase("rename")) {
            if (args.length == 2) {
                handleRename(player, args[1], RedProtect.rm.getRegion(player, player.getWorld()));
                return true;
            }
            if (args.length == 3) {
                handleRename(player, args[1], RedProtect.rm.getRegion(args[2], player.getWorld()));
                return true;
            }
            return false;
        }
        else if (args[0].equalsIgnoreCase("fl") || args[0].equalsIgnoreCase("flag")) {
            if (args.length == 2) {
                handleFlag(player, args[1], RedProtect.rm.getRegion(player, player.getWorld()));
                return true;
            }
            if (args.length == 3) {
                handleFlag(player, args[1], RedProtect.rm.getRegion(args[2], player.getWorld()));
                return true;
            }
            return false;
        }
        else {
            if (!args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("ls")) {
                return false;
            }
            if (args.length == 1) {
                handleList(player, player.getName());
                return true;
            }
            if (args.length == 2) {
                handleList(player, args[1]);
            }
            return false;
        }
    }
    
    public static void handleDelete(final Player p, final Region r) {
        if (RedProtect.ph.hasRegionPerm(p, "delete", r)) {
            if (r == null) {
                sendNotInRegionMessage(p);
                return;
            }
            p.sendMessage(ChatColor.AQUA + "Region successfully deleted.");
            RedProtect.rm.remove(r);
        }
        else {
            sendNoPermissionMessage(p);
        }
    }
    
    public static void handleInfo(final Player p, final Region r) {
        if (RedProtect.ph.hasRegionPerm(p, "info", r)) {
            if (r == null) {
                sendNotInRegionMessage(p);
                return;
            }
            p.sendMessage(r.info());
        }
        else {
            sendNoPermissionMessage(p);
        }
    }
    
    public static void handleAddMember(final Player p, String sVictim, final Region r) {
        if (RedProtect.ph.hasRegionPerm(p, "addmember", r)) {
            if (r == null) {
                sendNotInRegionMessage(p);
                return;
            }
            final Player pVictim = RedProtect.serv.getPlayerExact(sVictim);
            sVictim = sVictim.toLowerCase();
            if (r.isOwner(sVictim)) {
                r.removeOwner(sVictim);
                r.addMember(sVictim);
                if (pVictim != null && pVictim.isOnline()) {
                    pVictim.sendMessage(ChatColor.AQUA + "You have been demoted to member in: " + ChatColor.GOLD + r.getName() + ChatColor.AQUA + ", by: " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + ".");
                }
                p.sendMessage(ChatColor.AQUA + "Demoted player " + ChatColor.GOLD + sVictim + ChatColor.AQUA + " to member in " + ChatColor.GOLD + r.getName() + ChatColor.AQUA + ".");
            }
            else if (!r.isMember(sVictim)) {
                r.addMember(sVictim);
                p.sendMessage(ChatColor.AQUA + "Added " + ChatColor.GOLD + sVictim + ChatColor.AQUA + " as a member.");
                if (pVictim != null && pVictim.isOnline()) {
                    pVictim.sendMessage(ChatColor.AQUA + "You have been added as a member to region: " + ChatColor.GOLD + r.getName() + ChatColor.AQUA + ", by: " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + ".");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + sVictim + " is already a member in this region.");
            }
        }
        else {
            sendNoPermissionMessage(p);
        }
    }
    
    public static void handleAddOwner(final Player p, String sVictim, final Region r) {
        if (RedProtect.ph.hasRegionPerm(p, "addowner", r)) {
            if (r == null) {
                sendNotInRegionMessage(p);
                return;
            }
            final Player pVictim = RedProtect.serv.getPlayerExact(sVictim);
            sVictim = sVictim.toLowerCase();
            if (!r.isOwner(sVictim)) {
                r.addOwner(sVictim);
                p.sendMessage(ChatColor.AQUA + "Added " + ChatColor.GOLD + sVictim + ChatColor.AQUA + " as an owner.");
                if (pVictim != null && pVictim.isOnline()) {
                    pVictim.sendMessage(ChatColor.AQUA + "You have been added as an owner to region: " + ChatColor.GOLD + r.getName() + ChatColor.AQUA + ", by: " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + ".");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "That player is already an owner in this region!");
            }
        }
        else {
            sendNoPermissionMessage(p);
        }
    }
    
    public static void handleRemoveMember(final Player p, String sVictim, final Region r) {
        if (RedProtect.ph.hasRegionPerm(p, "removemember", r)) {
            if (r == null) {
                sendNotInRegionMessage(p);
                return;
            }
            final Player pVictim = RedProtect.serv.getPlayerExact(sVictim);
            sVictim = sVictim.toLowerCase();
            if (r.isMember(sVictim) || r.isOwner(sVictim)) {
                p.sendMessage(ChatColor.AQUA + "Removed " + ChatColor.GOLD + sVictim + ChatColor.AQUA + " from this region.");
                r.removeMember(sVictim);
                if (pVictim != null && pVictim.isOnline()) {
                    pVictim.sendMessage(ChatColor.AQUA + "You have been removed as a member from region: " + ChatColor.GOLD + r.getName() + ChatColor.AQUA + ", by: " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + ".");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + sVictim + " isn't a member of this region.");
            }
        }
        else {
            sendNoPermissionMessage(p);
        }
    }
    
    public static void handleRemoveOwner(final Player p, String sVictim, final Region r) {
        if (RedProtect.ph.hasRegionPerm(p, "removeowner", r)) {
            if (r == null) {
                sendNotInRegionMessage(p);
                return;
            }
            final Player pVictim = RedProtect.serv.getPlayerExact(sVictim);
            sVictim = sVictim.toLowerCase();
            if (r.isOwner(sVictim)) {
                if (r.ownersSize() > 1) {
                    p.sendMessage(ChatColor.AQUA + "Made " + ChatColor.GOLD + sVictim + ChatColor.AQUA + " a member in this region.");
                    r.removeOwner(sVictim);
                    r.addMember(sVictim);
                    if (pVictim != null && pVictim.isOnline()) {
                        pVictim.sendMessage(ChatColor.AQUA + "You have been removed as an owner from region: " + ChatColor.GOLD + r.getName() + ChatColor.AQUA + ", by: " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + ".");
                    }
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "You can't remove " + ChatColor.GOLD + sVictim + ChatColor.AQUA + ", because they are the last owner in this region.");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + sVictim + " isn't an owner in this region.");
            }
        }
        else {
            sendNoPermissionMessage(p);
        }
    }
    
    public static void handleRename(final Player p, final String newName, final Region r) {
        if (RedProtect.ph.hasRegionPerm(p, "rename", r)) {
            if (r == null) {
                sendNotInRegionMessage(p);
                return;
            }
            if (RedProtect.rm.getRegion(newName, p.getWorld()) != null) {
                p.sendMessage(ChatColor.RED + "That name is already taken, please choose another one.");
                return;
            }
            if (newName.length() < 2 || newName.length() > 16) {
                p.sendMessage(ChatColor.RED + "Invalid name. Please enter a 2-16 character name.");
                return;
            }
            if (newName.contains(" ")) {
                p.sendMessage(ChatColor.RED + "The name of the region can't have a space in it.");
                return;
            }
            RedProtect.rm.rename(r, newName, p.getWorld());
            p.sendMessage(ChatColor.AQUA + "Made " + ChatColor.GOLD + newName + ChatColor.AQUA + " the new name for this region.");
        }
        else {
            p.sendMessage(ChatColor.RED + "You don't have sufficient permission to do that.");
        }
    }
    
    public static void handleFlag(final Player p, final String flag, final Region r) {
        if (flag.equalsIgnoreCase("pvp")) {
            if (RedProtect.ph.hasPerm(p, "redprotect.flag.pvp")) {
                if (r == null) {
                    sendNotInRegionMessage(p);
                    return;
                }
                if (r.isOwner(p) || RedProtect.ph.hasPerm(p, "redprotect.admin.flag")) {
                    RedProtect.rm.setFlag(r, 0, !r.getFlag(0), p.getWorld());
                    p.sendMessage(ChatColor.AQUA + "Flag \"pvp\" has been set to " + r.getFlag(0) + ".");
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "You don't have permission to toggle that flag in this region!");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You don't have permission to toggle that flag!");
            }
        }
        else if (flag.equalsIgnoreCase("chest")) {
            if (RedProtect.ph.hasPerm(p, "redprotect.flag.chest")) {
                if (r == null) {
                    sendNotInRegionMessage(p);
                    return;
                }
                if (r.isOwner(p) || RedProtect.ph.hasPerm(p, "redprotect.admin.flag")) {
                    r.setFlag(1, !r.getFlag(1));
                    p.sendMessage(ChatColor.AQUA + "Flag \"chest\" has been set to " + r.getFlag(1) + ".");
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "You don't have permission to toggle that flag in this region!");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You don't have permission to toggle that flag!");
            }
        }
        else if (flag.equalsIgnoreCase("lever")) {
            if (RedProtect.ph.hasPerm(p, "redprotect.flag.lever")) {
                if (r == null) {
                    sendNotInRegionMessage(p);
                    return;
                }
                if (r.isOwner(p) || RedProtect.ph.hasPerm(p, "redprotect.admin.flag")) {
                    r.setFlag(2, !r.getFlag(2));
                    p.sendMessage(ChatColor.AQUA + "Flag \"lever\" has been set to " + r.getFlag(2) + ".");
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "You don't have permission to toggle that flag in this region!");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You don't have permission to toggle that flag!");
            }
        }
        else if (flag.equalsIgnoreCase("button")) {
            if (RedProtect.ph.hasPerm(p, "redprotect.flag.button")) {
                if (r == null) {
                    sendNotInRegionMessage(p);
                    return;
                }
                if (r.isOwner(p) || RedProtect.ph.hasPerm(p, "redprotect.admin.flag")) {
                    r.setFlag(3, !r.getFlag(3));
                    p.sendMessage(ChatColor.AQUA + "Flag \"button\" has been set to " + r.getFlag(3) + ".");
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "You don't have permission to toggle that flag in this region!");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You don't have permission to toggle that flag!");
            }
        }
        else if (flag.equalsIgnoreCase("door")) {
            if (RedProtect.ph.hasPerm(p, "redprotect.flag.door")) {
                if (r == null) {
                    sendNotInRegionMessage(p);
                    return;
                }
                if (r.isOwner(p) || RedProtect.ph.hasPerm(p, "redprotect.admin.flag")) {
                    r.setFlag(4, !r.getFlag(4));
                    p.sendMessage(ChatColor.AQUA + "Flag \"door\" has been set to " + r.getFlag(4) + ".");
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "You don't have permission to toggle that flag in this region!");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You don't have permission to toggle that flag!");
            }
        }
        else if (flag.equalsIgnoreCase("mobs")) {
            if (RedProtect.ph.hasPerm(p, "redprotect.flag.mobs")) {
                if (r == null) {
                    sendNotInRegionMessage(p);
                    return;
                }
                if (r.isOwner(p) || RedProtect.ph.hasPerm(p, "redprotect.admin.flag")) {
                    r.setFlag(5, !r.getFlag(5));
                    p.sendMessage(ChatColor.AQUA + "Flag \"mobs\" has been set to " + r.getFlag(5) + ".");
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "You don't have permission to toggle that flag in this region!");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You don't have permission to toggle that flag!");
            }
        }
        else if (flag.equalsIgnoreCase("passives")) {
            if (RedProtect.ph.hasPerm(p, "redprotect.flag.passives")) {
                if (r == null) {
                    sendNotInRegionMessage(p);
                    return;
                }
                if (r.isOwner(p) || RedProtect.ph.hasPerm(p, "redprotect.admin.flag")) {
                    r.setFlag(6, !r.getFlag(6));
                    p.sendMessage(ChatColor.AQUA + "Flag \"passives\" has been set to " + r.getFlag(6) + ".");
                }
                else {
                    p.sendMessage(ChatColor.AQUA + "You don't have permission to toggle that flag in this region!");
                }
            }
            else {
                p.sendMessage(ChatColor.RED + "You don't have permission to toggle that flag!");
            }
        }
        else if (flag.equalsIgnoreCase("info") || flag.equalsIgnoreCase("i")) {
            if (r == null) {
                sendNotInRegionMessage(p);
                return;
            }
            p.sendMessage(ChatColor.AQUA + "Flag values: (" + r.getFlagInfo() + ChatColor.AQUA + ")");
        }
        else {
            p.sendMessage(ChatColor.AQUA + "List of flags: [pvp, chest, lever, button, door, mobs, passives]");
        }
    }
    
    public static void handleList(final Player player, final String name) {
        if (RedProtect.ph.hasPerm(player, "redprotect.admin.list")) {
            final Set<Region> regions = RedProtect.rm.getRegions(name);
            final int length = regions.size();
            if (length == 0) {
                player.sendMessage(ChatColor.AQUA + "That player has no regions!");
            }
            else {
                player.sendMessage(ChatColor.AQUA + "Regions created:");
                player.sendMessage(ChatColor.AQUA + "------------------------------------");
                final Iterator<Region> i = regions.iterator();
                while (i.hasNext()) {
                    player.sendMessage(ChatColor.AQUA + i.next().info());
                }
                player.sendMessage(ChatColor.AQUA + "------------------------------------");
            }
            return;
        }
        player.sendMessage(ChatColor.RED + "You don't have sufficient permission to do that.");
    }
}
