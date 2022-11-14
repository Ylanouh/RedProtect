// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RPLogger
{
    Logger l;
    
    RPLogger(final Logger l) {
        this.l = l;
    }
    
    public void info(final String s) {
        this.l.info("RedProtect: [" + s + "]");
    }
    
    public void warning(final String s) {
        this.l.warning("RedProtect: [" + s + "]");
    }
    
    public void severe(final String s) {
        this.l.severe("RedProtect: [" + s + "]");
    }
    
    public void log(final Level level, final String s) {
        this.l.log(level, "RedProtect: [" + s + "]");
    }
    
    public void debug(final String s) {
        if (RedProtect.debugMessages) {
            this.l.info("RedProtect Debug: [" + s + "]");
        }
    }
}
