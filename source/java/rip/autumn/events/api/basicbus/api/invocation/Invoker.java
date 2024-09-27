// 
// Decompiled by Procyon v0.5.36
// 

package rip.autumn.events.api.basicbus.api.invocation;

import java.lang.reflect.Method;

@FunctionalInterface
public interface Invoker
{
    void invoke(final Object p0, final Method p1, final Object... p2);
}
