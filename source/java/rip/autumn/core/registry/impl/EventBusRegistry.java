package rip.autumn.core.registry.impl;

import rip.autumn.events.api.basicbus.api.bus.Bus;
import rip.autumn.events.api.basicbus.api.bus.impl.EventBusImpl;
import rip.autumn.events.api.basicbus.api.invocation.impl.ReflectionInvoker;
import rip.autumn.core.registry.Registry;

public final class EventBusRegistry implements Registry {
   public final Bus eventBus = new EventBusImpl(new ReflectionInvoker());
}
