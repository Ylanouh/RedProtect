// 
// Decompiled by Procyon v0.5.36
// 

package com.OverCaste.plugin.RedProtect;

public class StartupException extends RuntimeException
{
    private static final long serialVersionUID = 8630115432743132912L;
    
    public StartupException(final Throwable t) {
        super(t);
    }
    
    public StartupException(final String message) {
        super(message);
    }
    
    public StartupException(final String message, final Throwable t) {
        super(message, t);
    }
}
