package com.turt2live.antishare.events;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AntiShare Event Dispatcher
 *
 * @author turt2live
 */
// TODO: Unit test
public class EventDispatcher {

    private static ConcurrentMap<Class<Event>, List<Method>> listeners = new ConcurrentHashMap<Class<Event>, List<Method>>();

    /**
     * Registers a class with the dispatcher. Invalid methods are silently ignored.
     * <p/>
     * A "valid" method is one which has the {@link com.turt2live.antishare.events.EventListener}
     * annotation as well as accepts a single argument of type {@link Event}.
     *
     * @param clazz the class to register. Null input is ignored.
     */
    public static void register(Class<?> clazz) {
        if (clazz == null) return;
        for (Method method : clazz.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())
                    && !Modifier.isStatic(method.getModifiers())
                    && method.isAnnotationPresent(EventListener.class)) {
                Class<?>[] paramaters = method.getParameterTypes();
                if (paramaters != null && paramaters.length == 1) {
                    Class<?>[] interfaces = paramaters[0].getInterfaces();
                    if (interfaces != null) {
                        for (Class<?> inter : interfaces) {
                            if (inter == Event.class) {
                                List<Method> methods = getList((Class<Event>) paramaters[0]);
                                methods.add(method);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes a class from the dispatcher
     *
     * @param clazz the class to de-register. Null input is ignored.
     */
    public static void deregister(Class<?> clazz) {
        if (clazz == null) return;
        for (Method method : clazz.getMethods()) {
            if (Modifier.isPublic(method.getModifiers())
                    && !Modifier.isStatic(method.getModifiers())
                    && method.isAnnotationPresent(EventListener.class)) {
                Class<?>[] paramaters = method.getParameterTypes();
                if (paramaters != null && paramaters.length == 1) {
                    Class<?>[] interfaces = paramaters[0].getInterfaces();
                    if (interfaces != null) {
                        for (Class<?> inter : interfaces) {
                            if (inter == Event.class) {
                                List<Method> methods = getList((Class<Event>) paramaters[0]);
                                methods.remove(method);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @see #register(Class)
     */
    public static void register(Object object) {
        if (object == null) return;
        if (object instanceof Class) register((Class) object);
        else register(object.getClass());
    }

    /**
     * @see #deregister(Class)
     */
    public static void deregister(Object object) {
        if (object == null) return;
        if (object instanceof Class) deregister((Class) object);
        else deregister(object.getClass());
    }

    private static List<Method> getList(Class<Event> eventClass) {
        if (!listeners.containsKey(eventClass)) listeners.put(eventClass, new CopyOnWriteArrayList<Method>());
        return listeners.get(eventClass);
    }

    /**
     * Dispatches an event to all listeners
     *
     * @param event the event to dispatch
     */
    public static void dispatch(Event event) {
        if (event == null) throw new IllegalArgumentException("Event is null");
        List<Method> methods = listeners.get(event.getClass());
        if (methods == null) return; // Don't fire nothing
        for (Method method : methods) {
            try {
                method.invoke(event, event);
            } catch (Exception e) {
                throw new RuntimeException("Cannot access method!");
            }
        }
    }

}
