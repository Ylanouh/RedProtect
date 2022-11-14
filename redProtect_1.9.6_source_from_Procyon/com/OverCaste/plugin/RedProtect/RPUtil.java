// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.Iterator;
import java.util.List;
import org.bukkit.Server;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;

public class RPUtil
{
    public static RedProtect plugin;
    
    public static void init(final RedProtect plugin) {
        RPUtil.plugin = plugin;
    }
    
    public static boolean isFileEmpty(final String s) {
        final File f = new File(s);
        if (!f.isFile()) {
            return true;
        }
        try {
            final FileInputStream fis = new FileInputStream(s);
            final int b = fis.read();
            if (b != -1) {
                fis.close();
                return false;
            }
            fis.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return true;
    }
    
    static String formatName(final String name) {
        final String s = name.substring(1).toLowerCase();
        final String fs = name.substring(0, 1).toUpperCase();
        String ret = String.valueOf(fs) + s;
        ret = ret.replace("_", " ");
        return ret;
    }
    
    static Server getServer() {
        return RPUtil.plugin.getServer();
    }
    
    static int[] toIntArray(final List<Integer> list) {
        final int[] ret = new int[list.size()];
        int i = 0;
        for (final Integer e : list) {
            ret[i++] = e;
        }
        return ret;
    }
}
