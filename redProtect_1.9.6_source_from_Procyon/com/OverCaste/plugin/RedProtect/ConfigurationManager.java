// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

public class ConfigurationManager
{
    static void initFiles(final RedProtect plugin) {
        try {
            final File main = new File(RedProtect.pathMain);
            final File data = new File(RedProtect.pathData);
            final File config = new File(RedProtect.pathConfig);
            final File flagConfig = new File(RedProtect.pathFlagConfig);
            if (!main.exists()) {
                main.mkdir();
                RedProtect.logger.info("Created folder: " + RedProtect.pathMain);
            }
            if (!data.exists()) {
                data.mkdir();
                RedProtect.logger.info("Created folder: " + RedProtect.pathData);
            }
            if (!config.exists()) {
                RedProtect.logger.info("Created file: " + RedProtect.pathConfig);
                config.createNewFile();
                final BufferedWriter fr = new BufferedWriter(new FileWriter(config));
                fr.write("#This is the configuration file, feel free to edit it." + RedProtect.lineSeparator);
                fr.write("#Types: Integer: Number without period; Boolean: True or false; Struct: One of the described strings." + RedProtect.lineSeparator);
                fr.write("#---------" + RedProtect.lineSeparator);
                fr.write("#The data type for the regions file, 'ymlgz', 'yml', 'oos', 'oosgz', 'mysql', oosgz is recommended for normal use. (Struct)" + RedProtect.lineSeparator);
                fr.write("#WARNING: YML IS NOT SUPPORTED DUE TO ERRORS IN BUKKIT. MYSQL WILL COME SOON USE OOS FOR NOW." + RedProtect.lineSeparator);
                fr.write("file-type: oosgz" + RedProtect.lineSeparator);
                fr.write("#MySQL DB info, don't mess with this unless you're using mysql. (Leave password empty if you're not using one.)" + RedProtect.lineSeparator);
                fr.write("mysql-db-name: redProtect" + RedProtect.lineSeparator);
                fr.write("mysql-user-name: root" + RedProtect.lineSeparator);
                fr.write("mysql-user-pass:  " + RedProtect.lineSeparator);
                fr.write("mysql-host: localhost" + RedProtect.lineSeparator);
                fr.write("#The way that the blocks drop upon region creation. 'drop' drops items, 'remove' removes the blocks without dropping items, and 'keep' doesn't do anything." + RedProtect.lineSeparator);
                fr.write("drop-type: drop" + RedProtect.lineSeparator);
                fr.write("#If debug messages should be printed to console. (Boolean)" + RedProtect.lineSeparator);
                fr.write("debug-messages: false" + RedProtect.lineSeparator);
                fr.write("#The preferred permissions system, 'bPerms', 'Perms3', 'PEX', 'GM', 'OP', 'SuperPerms', 'Detect' (Struct)" + RedProtect.lineSeparator);
                fr.write("preferred-permissions: Detect" + RedProtect.lineSeparator);
                fr.write("#Limit the amount of blocks a player without RedProtect.unlimited can protect at one time. -1 for unlimited. (Integer)" + RedProtect.lineSeparator);
                fr.write("limit-amount: 400" + RedProtect.lineSeparator);
                fr.write("#Height the region starts at, it goes from sky to this value, so 0 would be full sky to bedrock, and 40 would be sky to half way through terrain." + RedProtect.lineSeparator);
                fr.write("height-start: 0" + RedProtect.lineSeparator);
                fr.write("#The ID of the block that you construct regions out of. EX: 55 = Redstone, 85 = Fence (Integer)" + RedProtect.lineSeparator);
                fr.write("block-id: 55" + RedProtect.lineSeparator);
                fr.write("#The maximum amount of redstone blocks the loop will scan. [Don't make this -1, it's to stop infinite loops.] (Integer)" + RedProtect.lineSeparator);
                fr.write("max-scan: 600" + RedProtect.lineSeparator);
                fr.write("#Should we backup the database between saves in-case of interruption?" + RedProtect.lineSeparator);
                fr.write("backup: true" + RedProtect.lineSeparator);
                fr.write("#The ID of the selector wand." + RedProtect.lineSeparator);
                fr.write("adminWandID: " + RedProtect.adminWandID + RedProtect.lineSeparator);
                fr.write("#The ID of the information wand." + RedProtect.lineSeparator);
                fr.write("infoWandID: " + RedProtect.infoWandID + RedProtect.lineSeparator);
                fr.close();
            }
            if (!flagConfig.exists()) {
                flagConfig.createNewFile();
                final BufferedWriter fr = new BufferedWriter(new FileWriter(flagConfig));
                fr.write("#This is the flag defaults configuration, feel free to edit it." + RedProtect.lineSeparator);
                fr.write("#The flag can have either true or false default value. Users with required permission can manually toggle these in their own regions." + RedProtect.lineSeparator);
                fr.write("#---------" + RedProtect.lineSeparator);
                fr.write("pvp: false" + RedProtect.lineSeparator);
                fr.write("chest: false" + RedProtect.lineSeparator);
                fr.write("lever: true" + RedProtect.lineSeparator);
                fr.write("button: true" + RedProtect.lineSeparator);
                fr.write("door: false" + RedProtect.lineSeparator);
                fr.write("mobs: true" + RedProtect.lineSeparator);
                fr.close();
            }
            Properties props = new Properties();
            FileInputStream propfis = new FileInputStream(RedProtect.pathConfig);
            props.load(propfis);
            String dat = "";
            if ((dat = props.getProperty("debug-messages")) != null) {
                if (dat.equalsIgnoreCase("true")) {
                    RedProtect.debugMessages = true;
                }
                else if (dat.equalsIgnoreCase("false")) {
                    RedProtect.debugMessages = false;
                }
                else {
                    RedProtect.logger.severe("There is a major error in your configuration, 'debug-messages' isn't an acceptable value.");
                    plugin.disable();
                }
            }
            else {
                RedProtect.logger.severe("Configuration option not found: debug-messages! Defaulting to false.");
            }
            if ((dat = props.getProperty("file-type")) != null) {
                if (dat.equalsIgnoreCase("yml")) {
                    RedProtect.logger.debug("Selected mode is yml.");
                    RedProtect.fileType = RedProtect.FILE_TYPE.yml;
                }
                else if (dat.equalsIgnoreCase("ymlgz")) {
                    RedProtect.logger.debug("Selected mode is ymlgz.");
                    RedProtect.fileType = RedProtect.FILE_TYPE.ymlgz;
                }
                else if (dat.equalsIgnoreCase("oos")) {
                    RedProtect.logger.debug("Selected mode is oos.");
                    RedProtect.fileType = RedProtect.FILE_TYPE.oos;
                }
                else if (dat.equalsIgnoreCase("oosgz")) {
                    RedProtect.logger.debug("Selected mode is oosgz.");
                    RedProtect.fileType = RedProtect.FILE_TYPE.oosgz;
                }
                else if (dat.equalsIgnoreCase("mysql")) {
                    RedProtect.logger.debug("Selected mode is mysql.");
                    RedProtect.fileType = RedProtect.FILE_TYPE.mysql;
                }
                else {
                    RedProtect.logger.severe("There is a major error in your configuration, 'file-type' isn't an acceptable value.");
                    plugin.disable();
                }
            }
            else {
                RedProtect.logger.warning("Configuration option not found: file-type! Defaulting to ymlgz.");
            }
            if ((dat = props.getProperty("drop-type")) != null) {
                if (dat.equalsIgnoreCase("keep")) {
                    RedProtect.dropType = RedProtect.DROP_TYPE.keep;
                }
                else if (dat.equalsIgnoreCase("remove")) {
                    RedProtect.dropType = RedProtect.DROP_TYPE.remove;
                }
                else if (dat.equalsIgnoreCase("drop")) {
                    RedProtect.dropType = RedProtect.DROP_TYPE.drop;
                }
                else {
                    RedProtect.dropType = RedProtect.DROP_TYPE.drop;
                    RedProtect.logger.warning("There is an error in your configuration, 'drop-type' isn't an acceptable value.");
                }
            }
            if ((dat = props.getProperty("block-id")) != null) {
                try {
                    RedProtect.blockID = Integer.parseInt(dat);
                }
                catch (NumberFormatException e2) {
                    RedProtect.blockID = 55;
                    RedProtect.logger.warning("There is an error in your configuration, 'block-id' isn't a valid integer. Defaulting to Redstone.");
                }
            }
            else {
                RedProtect.logger.warning("Configuration option not found: block-id! Defaulting to Redstone.");
            }
            if ((dat = props.getProperty("limit-amount")) != null) {
                try {
                    RedProtect.limitAmount = Integer.parseInt(dat);
                }
                catch (NumberFormatException e2) {
                    RedProtect.limitAmount = 400;
                    RedProtect.logger.warning("There is an error in your configuration, 'limit-amount' isn't a valid integer. Defaulting to 400.");
                }
            }
            else {
                RedProtect.logger.warning("Configuration option not found: limit-amount! Defaulting to 400.");
            }
            if ((dat = props.getProperty("height-start")) != null) {
                try {
                    RedProtect.heightStart = Integer.parseInt(dat);
                }
                catch (NumberFormatException e2) {
                    RedProtect.heightStart = 0;
                    RedProtect.logger.warning("There is an error in your configuration, 'height-start' isn't a valid integer. Defaulting to 0.");
                }
            }
            else {
                RedProtect.logger.warning("Configuration option not found: height-start! Defaulting to 0.");
            }
            if ((dat = props.getProperty("max-scan")) != null) {
                try {
                    RedProtect.maxScan = Integer.parseInt(dat);
                }
                catch (NumberFormatException e2) {
                    RedProtect.maxScan = 600;
                    RedProtect.logger.warning("There is an error in your configuration, 'max-scan' isn't a valid integer. Defaulting to 600.");
                }
            }
            else {
                RedProtect.logger.warning("Configuration option not found: max-scan! Defaulting to 600.");
            }
            if ((dat = props.getProperty("mysql-db-name")) != null) {
                RedProtect.mysqlDatabaseName = dat;
            }
            if ((dat = props.getProperty("mysql-user-name")) != null) {
                RedProtect.mysqlUserName = dat;
            }
            if ((dat = props.getProperty("mysql-user-pass")) != null) {
                RedProtect.mysqlUserPass = dat;
            }
            if ((dat = props.getProperty("mysql-host")) != null) {
                RedProtect.mysqlHost = dat;
            }
            if ((dat = props.getProperty("backup")) != null) {
                if (dat.equalsIgnoreCase("true") || dat.equalsIgnoreCase("yes")) {
                    RedProtect.backup = true;
                }
                else {
                    RedProtect.backup = false;
                }
            }
            if ((dat = props.getProperty("adminWandID")) != null) {
                try {
                    RedProtect.adminWandID = Integer.parseInt(dat);
                }
                catch (NumberFormatException e2) {
                    RedProtect.logger.warning("Configuration value 'adminWandID' isn't a valid integer!");
                }
            }
            if ((dat = props.getProperty("infoWandID")) != null) {
                try {
                    RedProtect.infoWandID = Integer.parseInt(dat);
                }
                catch (NumberFormatException e2) {
                    RedProtect.logger.warning("Configuration value 'infoWandID' isn't a valid integer!");
                }
            }
            propfis.close();
            dat = "";
            props = new Properties();
            propfis = new FileInputStream(RedProtect.pathFlagConfig);
            props.load(propfis);
            if ((dat = props.getProperty("pvp")) != null) {
                if (dat.equalsIgnoreCase("true")) {
                    Flags.pvp = true;
                }
                else if (dat.equalsIgnoreCase("false")) {
                    Flags.pvp = false;
                }
            }
            else {
                RedProtect.logger.warning("Configuration value \"pvp\" isn't initalized, defaulting to false.");
            }
            if ((dat = props.getProperty("chest")) != null) {
                if (dat.equalsIgnoreCase("true")) {
                    Flags.chest = true;
                }
                else if (dat.equalsIgnoreCase("false")) {
                    Flags.chest = false;
                }
            }
            else {
                RedProtect.logger.warning("Configuration value \"chest\" isn't initalized, defaulting to false.");
            }
            if ((dat = props.getProperty("lever")) != null) {
                if (dat.equalsIgnoreCase("true")) {
                    Flags.lever = true;
                }
                else if (dat.equalsIgnoreCase("false")) {
                    Flags.lever = false;
                }
            }
            else {
                RedProtect.logger.warning("Configuration value \"lever\" isn't initalized, defaulting to true.");
            }
            if ((dat = props.getProperty("button")) != null) {
                if (dat.equalsIgnoreCase("true")) {
                    Flags.button = true;
                }
                else if (dat.equalsIgnoreCase("false")) {
                    Flags.button = false;
                }
            }
            else {
                RedProtect.logger.warning("Configuration value \"button\" isn't initalized, defaulting to true.");
            }
            if ((dat = props.getProperty("door")) != null) {
                if (dat.equalsIgnoreCase("true")) {
                    Flags.door = true;
                }
                else if (dat.equalsIgnoreCase("false")) {
                    Flags.door = false;
                }
            }
            else {
                RedProtect.logger.warning("Configuration value \"door\" isn't initalized, defaulting to false.");
            }
            if ((dat = props.getProperty("mobs")) != null) {
                if (dat.equalsIgnoreCase("true")) {
                    Flags.mobs = true;
                }
                else if (dat.equalsIgnoreCase("false")) {
                    Flags.mobs = false;
                }
            }
            else {
                RedProtect.logger.warning("Configuration value \"mobs\" isn't initalized, defaulting to true.");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
