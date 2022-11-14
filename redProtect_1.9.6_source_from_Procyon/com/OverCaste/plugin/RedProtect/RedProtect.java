// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.Material;
import java.io.File;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.HashMap;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class RedProtect extends JavaPlugin
{
    public static PluginDescriptionFile pdf;
    PluginManager pm;
    static RedProtect plugin;
    RPBlockListener bListener;
    RPPlayerListener pListener;
    RPEntityListener eListener;
    RPWorldListener wListener;
    CommandManager cManager;
    static RegionManager rm;
    static RPPermissionHandler ph;
    static RPLogger logger;
    static final String lineSeparator;
    static Server serv;
    static final HashMap<Player, Location> firstLocationSelections;
    static final HashMap<Player, Location> secondLocationSelections;
    static final String pathMain;
    static final String pathData;
    static final String pathConfig;
    static final String pathFlagConfig;
    static FILE_TYPE fileType;
    static DROP_TYPE dropType;
    static boolean debugMessages;
    static int limitAmount;
    static int blockID;
    static int maxScan;
    static int heightStart;
    static String mysqlUserName;
    static String mysqlUserPass;
    static String mysqlDatabaseName;
    static String mysqlHost;
    static boolean backup;
    static int adminWandID;
    static int infoWandID;
    
    static {
        RedProtect.logger = null;
        lineSeparator = System.getProperty("line.separator");
        firstLocationSelections = new HashMap<Player, Location>();
        secondLocationSelections = new HashMap<Player, Location>();
        pathMain = "plugins" + File.separator + "redProtect" + File.separator;
        pathData = String.valueOf(RedProtect.pathMain) + File.separator + "data" + File.separator;
        pathConfig = String.valueOf(RedProtect.pathMain) + File.separator + "Config.txt";
        pathFlagConfig = String.valueOf(RedProtect.pathMain) + File.separator + "Flags.txt";
        RedProtect.fileType = FILE_TYPE.yml;
        RedProtect.dropType = DROP_TYPE.drop;
        RedProtect.debugMessages = false;
        RedProtect.limitAmount = 400;
        RedProtect.blockID = 55;
        RedProtect.maxScan = 600;
        RedProtect.heightStart = 50;
        RedProtect.mysqlUserName = "root";
        RedProtect.mysqlUserPass = "pass";
        RedProtect.mysqlDatabaseName = "mcRedProtect";
        RedProtect.mysqlHost = "localhost";
        RedProtect.backup = true;
        RedProtect.adminWandID = Material.FEATHER.getId();
        RedProtect.infoWandID = Material.STRING.getId();
    }
    
    public void onDisable() {
        RedProtect.rm.saveAll();
        RedProtect.logger.info(String.valueOf(RedProtect.pdf.getName()) + " disabled.");
    }
    
    public void onEnable() {
        try {
            (RedProtect.plugin = this).initVars();
            RPUtil.init(this);
            ConfigurationManager.initFiles(this);
            RedProtect.rm.loadAll();
            this.pm.registerEvents((Listener)this.bListener, (Plugin)this);
            this.pm.registerEvents((Listener)this.pListener, (Plugin)this);
            this.pm.registerEvents((Listener)this.eListener, (Plugin)this);
            this.pm.registerEvents((Listener)this.wListener, (Plugin)this);
            this.getCommand("RedProtect").setExecutor((CommandExecutor)this.cManager);
            System.out.println(String.valueOf(RedProtect.pdf.getFullName()) + " enabled.");
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error enabling redProtect, plugin will shut down.");
            this.disable();
        }
    }
    
    public void disable() {
        super.setEnabled(false);
    }
    
    void initVars() throws Exception {
        RedProtect.serv = this.getServer();
        RedProtect.logger = new RPLogger(RedProtect.serv.getLogger());
        RedProtect.pdf = this.getDescription();
        this.pm = RedProtect.serv.getPluginManager();
        this.bListener = new RPBlockListener(this);
        this.pListener = new RPPlayerListener(this);
        this.eListener = new RPEntityListener(this);
        this.wListener = new RPWorldListener(this);
        this.cManager = new CommandManager();
        RedProtect.ph = new RPPermissionHandler();
        RedProtect.rm = new RegionManager();
    }
    
    public RegionManager getGlobalRegionManager() {
        return RedProtect.rm;
    }
    
    enum DROP_TYPE
    {
        drop("drop", 0), 
        remove("remove", 1), 
        keep("keep", 2);
        
        private DROP_TYPE(final String name, final int ordinal) {
        }
    }
    
    enum FILE_TYPE
    {
        yml("yml", 0), 
        ymlgz("ymlgz", 1), 
        oos("oos", 2), 
        oosgz("oosgz", 3), 
        mysql("mysql", 4);
        
        private FILE_TYPE(final String name, final int ordinal) {
        }
    }
}
